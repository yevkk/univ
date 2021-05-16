#include "utils.hpp"
#include "mpi/mpi.h"

#include <iostream>

void calculation(double *A, double *B, double *C, int size) {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                C[i * size + j] += A[i * size + k] * B[k * size + j];
            }
        }
    }
}

int main() {
    double *aMatrix = nullptr;
    double *bMatrix = nullptr;
    double *resMatrix = nullptr;
    int size;
    time_t start, finish;
    double duration;

    std::cout << "Serial multiplication:\n";
    utils::init(aMatrix, bMatrix, resMatrix, size);

    start = clock();
    calculation(aMatrix, bMatrix, resMatrix, size);
    finish = clock();
    duration = (finish - start) / double(CLOCKS_PER_SEC);

    std::cout << "Time: " << duration;

    utils::on_shutdown(aMatrix, bMatrix, resMatrix);
    return 0;
}