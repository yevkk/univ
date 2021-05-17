#pragma once

#include <iostream>
#include <random>
#include <ctime>

namespace utils {
    double rand_double(double min, double max) {
        static std::random_device rd;
        static std::seed_seq seed{rd(), static_cast<unsigned int>(time(nullptr))};
        static std::mt19937_64 gen(seed);
        std::uniform_real_distribution<double> dist(min, max);

        return dist(gen);
    }

    void fill_matrix(double *matrix, int size) {
        for (std::size_t i = 0; i < size * size; i++) {
            matrix[i] = rand_double(-10, 100);
        }
    }

    void fill_matrix(double *matrix, int size, double value) {
        for (std::size_t i = 0; i < size * size; i++) {
            matrix[i] = value;
        }
    }

    void print_matrix(double *matrix, int size, std::ostream &os = std::cout) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                os << matrix[i * size + j] << ' ';
            }
            os << '\n';
        }
    }

    void serialCalculation(double *A, double *B, double *C, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    C[i * size + j] += A[i * size + k] * B[k * size + j];
                }
            }
        }
    }

    void testResult(double *A, double *B, double *C, int size) {
        double *serialResult;
        fill_matrix(serialResult = new double [size * size], size, 0);
        double e = 1.e-6;
        bool equal = true;

        serialCalculation(A, B, serialResult, size);
        std::cout << "C ser\n";
        print_matrix(serialResult, size);
        std::cout << "\n";
        for (int i = 0; i < size * size; i++) {
            if (fabs(serialResult[i] - C[i]) >= e) {
                equal = false;
                break;
            }
        }

        std::cout << std::boolalpha << equal << std::endl;

        delete [] serialResult;
    }
}