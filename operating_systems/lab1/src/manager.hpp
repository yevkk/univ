#ifndef LAB_MANAGER_HPP
#define LAB_MANAGER_HPP

#include <string>

namespace spos::lab1 {
    class Manager {
    public:
        Manager(const std::string &op, int x_arg) :
                _x_arg{x_arg}, _op{op} {}

    private:
        int _x_arg;
        std::string _op;
    };
}

#endif //LAB_MANAGER_HPP
