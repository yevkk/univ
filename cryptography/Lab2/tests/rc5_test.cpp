#define CATCH_CONFIG_MAIN

#include "catch.hpp"
#include "../rc5.hpp"

std::uint32_t rand_num(std::uint32_t min, std::uint32_t max) {
    static std::random_device rd;
    static std::seed_seq seed{rd(), static_cast<unsigned int>(time(nullptr))};
    static std::mt19937_64 gen(seed);
    std::uniform_int_distribution<std::uint32_t> dist(min, max);

    return dist(gen);
}

TEST_CASE("RC5 defined tests", "[rc5]") {
    RC5 rc5{};
    std::uint32_t *enc;

    struct test_data {
        std::vector<int> key_symbols;
        std::uint32_t *input;
        std::uint32_t *cipher;
    };

    std::string{'9', '0', '0'};

    std::vector<test_data> tests = {
            test_data{
                    std::vector<int>(0, 16),
                    new std::uint32_t[2]{0, 0},
                    new std::uint32_t[2]{0xEEDBA521, 0x6D8F4B15}
            },
            test_data{
                    {0x91, 0x5F, 0x46, 0x19, 0xBE, 0x41, 0xB2, 0x51, 0x63, 0x55, 0xA5, 0x01, 0x10, 0xA9, 0xCE, 0x91},
                    new std::uint32_t[2]{0xEEDBA521, 0x6D8F4B15},
                    new std::uint32_t[2]{0xAC13C0F7, 0x52892B5B}
            },
            test_data{
                    {0x78, 0x33, 0x48, 0xE7, 0x5A, 0xEB, 0x0F, 0x2F, 0xD7, 0xB1, 0x69, 0xBB, 0x8D, 0xC1, 0x67, 0x87},
                    new std::uint32_t[2]{0xAC13C0F7, 0x52892B5B},
                    new std::uint32_t[2]{0xB7B3422F, 0x92FC6903}
            },
            test_data{
                    {0xDC, 0x49, 0xDB, 0x13, 0x75, 0xA5, 0x58, 0x4F, 0x64, 0x85, 0xB4, 0x13, 0xB5, 0xF1, 0x2B, 0xAF},
                    new std::uint32_t[2]{0xB7B3422F, 0x92FC6903},
                    new std::uint32_t[2]{0xB278C165, 0xCC97D184}
            },
            test_data{
                    {0x52, 0x69, 0xF1, 0x49, 0xD4, 0x1B, 0xA0, 0x15, 0x24, 0x97, 0x57, 0x4D, 0x7F, 0x15, 0x31, 0x25},
                    new std::uint32_t[2]{0xB278C165, 0xCC97D184},
                    new std::uint32_t[2]{0x15E444EB, 0x249831DA}
            }
    };

    for (const auto &test : tests) {
        rc5.setup(std::string(test.key_symbols.begin(), test.key_symbols.end()));
        enc = rc5.encrypt(test.input);
        REQUIRE(enc[0] == test.cipher[0]);
        REQUIRE(enc[1] == test.cipher[1]);
    }
}

TEST_CASE("RC5 enc-dec tests", "[rc5]") {
    RC5 rc5{};
    std::uint32_t input[2];
    std::uint32_t * output;
    std::vector<int> key = {0x52, 0x69, 0xF1, 0x49, 0xD4, 0x1B, 0xA0, 0x15, 0x24, 0x97, 0x57, 0x4D, 0x7F, 0x15, 0x31, 0x25};
    rc5.setup(std::string(key.begin(), key.end()));

    for (int i = 0; i < 20; i++) {
        input[0] = rand_num(0, 0xffff);
        input[1] = rand_num(0, 0xffff);
        output = rc5.decrypt(rc5.encrypt(input));
        REQUIRE(output[0] == input[0]);
        REQUIRE(output[1] == input[1]);
    }
}