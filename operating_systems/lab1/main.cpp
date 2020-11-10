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
    const std::vector op_names{"AND", "OR", "MIN"};
    const std::vector cancellation_names{"KEYBOARD", "PROMPT"};

    while (true) {
        int x_arg;
        std::string x_arg_str;
        std::cout << "Enter argument:\n";
        std::cout << "(Non-numerical input considered as 0)\n";
        std::cin >> x_arg_str;
        x_arg = is_number(x_arg_str) ? std::stoi(x_arg_str) : 0;


        std::string op_name;
        while (std::find(op_names.cbegin(), op_names.cend(), op_name) == op_names.cend()) {
            std::cout << "Enter valid binary operation name\n";
            std::cout << "Options: ";
            for (const auto &item : op_names) {
                std::cout << item << " ";
            }
            std::cout << "\n";
            std::cin >> op_name;
        }

        std::string cancellation_name;
        while (std::find(cancellation_names.cbegin(), cancellation_names.cend(), cancellation_name) ==
               cancellation_names.cend()) {
            std::cout << "Enter valid cancellation type name\n";
            std::cout << "Options: ";
            for (const auto &item : cancellation_names) {
                std::cout << item << " ";
            }
            std::cout << "\n";
            std::cin >> cancellation_name;
        }

        if (cancellation_name == "KEYBOARD") {
            spos::lab1::Manager<spos::lab1::CancellationType::KEYBOARD> mgr{op_name, x_arg};
            mgr.run();
        } else if (cancellation_name == "PROMPT") {
            spos::lab1::Manager<spos::lab1::CancellationType::PROMPT> mgr{op_name, x_arg};
            mgr.run();
        }

        std::cout << "\nChoose Option:\n \ta) continue\n \tb) stop\n";
        char input = ' ';
        while (input != 'a' && input != 'b') {
            std::cin >> input;
        }
        if (input == 'b') {
            break;
        }
    }

    return 0;
}
