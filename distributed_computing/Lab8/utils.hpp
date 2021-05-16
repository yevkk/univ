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
            matrix[i] = rand_double(-100, 100);
        }
    }

    void fill_matrix(double *matrix, int size, double value) {
        for (std::size_t i = 0; i < size * size; i++) {
            matrix[i] = value;
        }
    }

    void init(double *&aMatrix, double *&bMatrix, double *&resMatrix, int &size) {
        std::cout << "Matrix size: ";
        std::cin >> size;

        fill_matrix(aMatrix = new double[size * size], size);
        fill_matrix(bMatrix = new double[size * size], size);
        fill_matrix(resMatrix = new double[size * size], size, 0);
    }

    void on_shutdown(const double *aMatrix, const double *bMatrix, const double *resMatrix) {
        delete[] aMatrix;
        delete[] bMatrix;
        delete[] resMatrix;
    }

    void print_matrix(double *matrix, int size, std::ostream &os = std::cout) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                os << matrix[i * size + j] << ' ';
            }
            os << '\n';
        }
    }
}