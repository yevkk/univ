#include "src/manager.hpp"

#include <iostream>
#include <algorithm>
#include <vector>
#include <string>

bool is_number(const std::string &s) {
    return !s.empty() &&
           std::find_if(s.begin(), s.end(), [](char c) { return !std::isdigit(c); }) == s.end();
}

int main(int argc, char *argv[]) {
    const std::vector op_names{"AND", "OR"};

    int x_arg;
    std::string op = "";

    if (argc < 2) {
        std::cout << "Input argument:\n";
        std::cin >> x_arg;
    } else {
        x_arg = is_number(argv[1]) ? std::stoi(argv[1]) : 0;
    }

    if (argc >= 3) {
        op = argv[2];
    }

    while (std::find(op_names.cbegin(), op_names.cend(), op) == op_names.cend()) {
        std::cout << "Input valid binary operation name\n";
        std::cout << "Options: ";
        for (const auto &item : op_names) {
            std::cout << item << " ";
        }
        std::cout << "\n";
        std::cin >> op;
    }

    return 0;
}
