#include "utils.hpp"
#include "mpi/mpi.h"

#include <iostream>

int procNum;
int procRank;
int blockSize;
MPI_Comm seqComm;
int coords[1];

void init(double *&aMatrix, double *&bMatrix, double *&cMatrix,
          double *&aBlock, double *&bBlock, double *&cBlock, int &size) {
    if (procRank == 0) {
        std::cout << "Matrix size: ";
        std::cin >> size;
    }

    MPI_Bcast(&size, 1, MPI_INT, 0, MPI_COMM_WORLD);
    blockSize = size / procNum;

    aBlock = new double[blockSize * size];
    bBlock = new double[blockSize * size];
    cBlock = new double[blockSize * size];

    for (int i = 0; i < blockSize; i++) {
        for (int j = 0; j < blockSize; j++) {
            cBlock[i * blockSize + j] = 0;
        }
    }


    if (procRank == 0) {
        utils::fill_matrix(aMatrix = new double[size * size], size);
        utils::fill_matrix(bMatrix = new double[size * size], size);
        utils::fill_matrix(cMatrix = new double[blockSize * blockSize], blockSize, 0);
    }
}

void on_shutdown(const double *aMatrix, const double *bMatrix, const double *cMatrix,
                 const double *aBlock, const double *bBlock, const double *cBlock) {
    if (procRank == 0) {
        delete[] aMatrix;
        delete[] bMatrix;
        delete[] cMatrix;
    }

    delete[] aBlock;
    delete[] bBlock;
    delete[] cBlock;
}

void createGridCommunicators() {
    int dimSize[] = {procNum};
    int periodic[] = {1};

    MPI_Dims_create(procNum, 1, dimSize);
    MPI_Cart_create(MPI_COMM_WORLD, 1, dimSize, periodic, 0, &seqComm);
    MPI_Cart_coords(seqComm, procRank, 1, coords);
}

void dataDistribution(double *aMatrix, double *bMatrix, double *aBlock, double *bBlock, int size) {
    MPI_Scatter(aMatrix, blockSize * size, MPI_DOUBLE, aBlock, blockSize * size, MPI_DOUBLE, 0, seqComm);
    MPI_Scatter(bMatrix, blockSize * size, MPI_DOUBLE, bBlock, blockSize * size, MPI_DOUBLE, 0, seqComm);
}

void testBlocks(double *block, int size1, int size2, std::string str) {
    MPI_Barrier(MPI_COMM_WORLD);
    if (procRank == 0) {
        std::cout << str << std::endl;
    }
    for (int i = 0; i < procNum; i++) {
        if (procRank == i) {
            std::cout << "Proc rank = " << procRank << std::endl;
            for (int j = 0; j < size1; j++) {
                for (int k = 0; k < size2; k++) {
                    std::cout << block[j * size2 + k] << ' ';
                }
                std::cout << '\n';
            }
            std::cout << std::endl;
        }
        MPI_Barrier(MPI_COMM_WORLD);
    }
}

void bBlockCommunication(double *bBlock, int size) {
    MPI_Status status;
    int nextProc = (procRank + 1) % procNum;
    int prevProc = (procNum + procRank - 1) % procNum;

    MPI_Sendrecv_replace(bBlock, blockSize * size, MPI_DOUBLE, nextProc, 0, prevProc, 0, seqComm, &status);
}

void resultCalculation(double *aBlock, double *bBlock, double *cBlock, int size) {
    for (int i = 0; i < procNum; i++) {
        for (int r = 0; r < blockSize; r++) {
            for (int c = 0; c < size; c++) {
                for (int k = 0; k < blockSize; k++) {
                    int offset = ((procRank + i) % procNum) * blockSize;
                    cBlock[r * blockSize + c] = aBlock[r * blockSize + offset + k] * bBlock[k * blockSize + c];
                }
            }
        }

        bBlockCommunication(bBlock, size);
    }
}

void resultCollection(double *cMatrix, double *cBlock, int size) {
    testBlocks(cBlock, blockSize, size, "C par");
    MPI_Gather(cBlock, blockSize * size, MPI_DOUBLE, cMatrix, blockSize * size, MPI_DOUBLE, 0, MPI_COMM_WORLD);
}


int main(int argc, char *argv[]) {
    double *aMatrix = nullptr;
    double *bMatrix = nullptr;
    double *cMatrix = nullptr;
    int size;

    double *aBlock;
    double *bBlock;
    double *cBlock;

    double start, finish, duration;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &procNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &procRank);

    if (procRank == 0) {
        std::cout << "Parallel striped multiplication:\n";
    }
    createGridCommunicators();
    init(aMatrix, bMatrix, cMatrix, aBlock, bBlock, cBlock, size);
    dataDistribution(aMatrix, bMatrix, aBlock, bBlock, size);

    start = MPI_Wtime();
    resultCalculation(aBlock, bBlock, cBlock, size);
    resultCollection(cMatrix, cBlock, size);
    finish = MPI_Wtime();
    if (procRank == 0) {
        utils::testResult(aMatrix, bMatrix, cMatrix, size);
        std::cout << "Time: " << (finish - start) << std::endl;
    }


    on_shutdown(aMatrix, bMatrix, cMatrix, aBlock, bBlock, cBlock);
    MPI_Finalize();
    return 0;
}