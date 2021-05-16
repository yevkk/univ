#include "utils.hpp"
#include "mpi/mpi.h"

#include <iostream>

int procNum;
int procRank;
int gridSize;
MPI_Comm gridComm;
MPI_Comm colComm;
MPI_Comm rowComm;
int gridCoords[2];

void init(double *&aMatrix, double *&bMatrix, double *&cMatrix,
          double *&aBlock, double *&bBlock, double *&cBlock,
          double *&aMatrixBlock, int &size, int &blockSize) {
    if (procRank == 0) {
        std::cout << "Matrix size: ";
        std::cin >> size;
    }

    MPI_Bcast(&size, 1, MPI_INT, 0, MPI_COMM_WORLD);
    blockSize = size / gridSize;

    aBlock = new double[blockSize * blockSize];
    bBlock = new double[blockSize * blockSize];
    utils::fill_matrix(cBlock = new double[blockSize * blockSize], blockSize, 0);
    aMatrixBlock = new double[blockSize * blockSize];

    if (procRank == 0) {
        utils::fill_matrix(aMatrix = new double[size * size], size);
        utils::fill_matrix(bMatrix = new double[size * size], size);
        utils::fill_matrix(cMatrix = new double[size * size], size, 0);
    }
}

void on_shutdown(const double *aMatrix, const double *bMatrix, const double *cMatrix,
                 const double *aBlock, const double *bBlock, const double *cBlock, const double *aMatrixBlock) {
    if (procRank == 0) {
        delete[] aMatrix;
        delete[] bMatrix;
        delete[] cMatrix;
    }

    delete[] aBlock;
    delete[] bBlock;
    delete[] cBlock;
    delete[] aMatrixBlock;
}

void createGridCommunicators() {
    int dimSize[] = {gridSize, gridSize};
    int periodic[] = {1, 1};
    int subDims[2];

    MPI_Dims_create(procNum, 2, dimSize);
    MPI_Cart_create(MPI_COMM_WORLD, 2, dimSize, periodic, 1, &gridComm);
    MPI_Cart_coords(gridComm, procRank, 2, gridCoords);

    subDims[0] = 0;
    subDims[1] = 1;
    MPI_Cart_sub(gridComm, subDims, &rowComm);

    subDims[0] = 1;
    subDims[1] = 0;
    MPI_Cart_sub(gridComm, subDims, &colComm);
}

void checkerboardMatrixScatter(double *matrix, double *matrixBlock, int size, int blockSize) {
    auto *matrixRow = new double[blockSize * size];
    if (gridCoords[1] == 0) {
        MPI_Scatter(matrix, blockSize * size, MPI_DOUBLE, matrixRow, blockSize * size, MPI_DOUBLE, 0, colComm);
    }

    for (int i = 0; i < blockSize; i++) {
        MPI_Scatter(&matrixRow[i * size], blockSize, MPI_DOUBLE, &(matrixBlock[i * blockSize]), blockSize, MPI_DOUBLE,
                    0, rowComm);
    }
    delete[] matrixRow;
}

void dataDistribution(double *aMatrix, double *bMatrix, double *aMatrixBlock, double *bBlock, int size, int blockSize) {
    checkerboardMatrixScatter(aMatrix, aMatrixBlock, size, blockSize);
    checkerboardMatrixScatter(bMatrix, bBlock, size, blockSize);
}

void testBlocks(double *block, int blockSize, std::string str) {
    MPI_Barrier(MPI_COMM_WORLD);
    if (procRank == 0) {
        std::cout << str << std::endl;
    }
    for (int i = 0; i < procNum; i++) {
        if (procRank == i) {
            std::cout << "Proc rank = " << procRank << std::endl;
            utils::print_matrix(block, blockSize);
            std::cout << std::endl;
        }
        MPI_Barrier(MPI_COMM_WORLD);
    }
}

void aBlockCommunication(int i, double *aBlock, double *aMatrixBlock, int blockSize) {
    int pivot = (gridCoords[0] + i) % gridSize;

    if (gridCoords[1] == pivot) {
        for (int i = 0; i < blockSize * blockSize; i++) {
            aBlock[i] = aMatrixBlock[i];
        }
    }

    MPI_Bcast(aBlock, blockSize * blockSize, MPI_DOUBLE, pivot, rowComm);
}

void bBlockCommunication(double *bBlock, int blockSize, MPI_Comm columnComm) {
    MPI_Status status;
    int nextProc = (gridCoords[0] == gridSize - 1) ? 0 : gridCoords[0] + 1;
    int prevProc = (gridCoords[0] == 0) ? gridSize - 1 : gridCoords[0] - 1;

    MPI_Sendrecv_replace(bBlock, blockSize * blockSize, MPI_DOUBLE, nextProc, 0, prevProc, 0, columnComm, &status);
}

void resultCalculation(double *aBlock, double *aMatrixBlock, double *bBlock, double *cBlock, int blockSize) {
    for (int i = 0; i < gridSize; i++) {
        aBlockCommunication(i, aBlock, aMatrixBlock, blockSize);
        utils::serialCalculation(aBlock, bBlock, cBlock, blockSize);
        bBlockCommunication(bBlock, blockSize, colComm);
    }
}

void resultCollection(double *cMatrix, double *cBlock, int size, int blockSize) {
    auto *resRow = new double [size * blockSize];
    for (int i = 0; i < blockSize; i++) {
        MPI_Gather(&cBlock[i * blockSize], blockSize, MPI_DOUBLE, &resRow[i * size], blockSize, MPI_DOUBLE, 0, rowComm);
    }

    if (gridCoords[1] == 0) {
        MPI_Gather(resRow, blockSize * size, MPI_DOUBLE, cMatrix, blockSize * size, MPI_DOUBLE, 0, colComm);
    }
    delete [] resRow;
}

int main(int argc, char *argv[]) {
    double *aMatrix = nullptr;
    double *bMatrix = nullptr;
    double *cMatrix = nullptr;
    int size;

    double *aMatrixBlock;
    double *aBlock;
    double *bBlock;
    double *cBlock;
    int blockSize;

    double start, finish, duration;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &procNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &procRank);

    gridSize = sqrt((double) procNum);
    if (procNum != gridSize * gridSize) {
        return 1;
    }


    if (procRank == 0) {
        std::cout << "Parallel Fox multiplication:\n";
    }
    createGridCommunicators();
    init(aMatrix, bMatrix, cMatrix, aBlock, bBlock, cBlock, aMatrixBlock, size, blockSize);
    dataDistribution(aMatrix, bMatrix, aMatrixBlock, bBlock, size, blockSize);

    start = MPI_Wtime();
    resultCalculation(aBlock, aMatrixBlock, bBlock, cBlock, blockSize);
    resultCollection(cMatrix, cBlock, size, blockSize);
    finish = MPI_Wtime();
    if (procRank == 0) {
//        utils::print_matrix(cMatrix, size);
        utils::testResult(aMatrix, bMatrix, cMatrix, size);
        std::cout << "Time: " << (finish - start) << std::endl;
    }



    on_shutdown(aMatrix, bMatrix, cMatrix, aBlock, bBlock, cBlock, aMatrixBlock);
    MPI_Finalize();
    return 0;
}