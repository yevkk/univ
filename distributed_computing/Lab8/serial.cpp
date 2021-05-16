#include "utils.hpp"
#include "mpi/mpi.h"

#include <iostream>

void init(double *&aMatrix, double *&bMatrix, double *&resMatrix, int &size) {
    std::cout << "Matrix size: ";
    std::cin >> size;

    utils::fill_matrix(aMatrix = new double[size * size], size);
    utils::fill_matrix(bMatrix = new double[size * size], size);
    utils::fill_matrix(resMatrix = new double[size * size], size, 0);
}

void on_shutdown(const double *aMatrix, const double *bMatrix, const double *resMatrix) {
    delete[] aMatrix;
    delete[] bMatrix;
    delete[] resMatrix;
}

int main() {
    double *aMatrix = nullptr;
    double *bMatrix = nullptr;
    double *resMatrix = nullptr;
    int size;
    time_t start, finish;
    double duration;

    std::cout << "Serial multiplication:\n";
    init(aMatrix, bMatrix, resMatrix, size);

    start = clock();
    utils::serialCalculation(aMatrix, bMatrix, resMatrix, size);
    finish = clock();
    duration = (finish - start) / double(CLOCKS_PER_SEC);

    std::cout << "Time: " << duration;

    on_shutdown(aMatrix, bMatrix, resMatrix);
    return 0;
}