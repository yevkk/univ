#include <iostream>
#include "rc5.hpp"

int main() {
    RC5 rc5;
    rc5.setup(std::string(16, '\0'));
    std::uint32_t source[] = {0, 0};
    auto res = rc5.encrypt(source);
    std::cout << std::hex << res[0] << std::endl;
    std::cout << std::hex << res[1] << std::endl;

    auto dec = rc5.decrypt(res);
    std::cout << std::dec << dec[0] << std::endl;
    std::cout << std::dec << dec[1] << std::endl;
}
