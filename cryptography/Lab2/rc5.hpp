#pragma once

#include <cstdint>
#include <cmath>

class RC5 {
private:
    static std::uint32_t P() {
        switch (w) {
            case 16:
                return 0xB7E1;
            case 32:
                return 0xB7E15163; //3084996963
            default:
                return 0;
        };
    }

    static std::uint32_t Q() {
        switch (w) {
            case 16:
                return 0x9E37;
            case 32:
                return 0x9E3779B9; //2654435769
            default:
                return 0;
        };
    }

    static std::uint32_t lcs(std::uint32_t value, std::size_t count) {
        return (value << count | value >> (32 - count));
    }

    static std::uint32_t rcs(std::uint32_t value, std::uint32_t count) {
        return (value >> count | value << (32 - count));
    }

    static constexpr int w = 32;            //length of a word in bits
    static constexpr int u = w / 8;         //length of a word in bytes

    static constexpr int b = 16;            //length of a key in bytes
    std::uint8_t K[b];                      //key

    static constexpr int c = 4;             //length of the key in words (or 1, if b = 0)
    std::uint32_t L[c];                     //key in words

    static constexpr int r = 12;            //number of rounds
    static constexpr int t = 2 * (r + 1);   //the number of round subkeys required.
    std::uint32_t S[t];                     //round subkey words

public:
    void setup(std::string key) {
        for (int i = 0; i < key.length(); i++) {
            K[i] = key[i];
        }

        for (int i = b - 1; i > -1; i--) {
            L[i / u] = (L[i / u] << 8) + K[i];
        }

        S[0] = P();
        for (int i = 1; i < t; i++) {
            S[i] = S[i - 1] + Q();
        }

        std::uint32_t A, B;
        int i, j, k;
        for (A = B = i = j = k = 0; k < 3 * t; k++, i = (i + 1) % t, j = (j + 1) % c) {
            A = S[i] = lcs(S[i] + A + B, 3);
            B = L[j] = lcs(L[j] + A + B, (A + B));
        }
    }

    std::uint32_t * encrypt(const std::uint32_t * input) {
        auto cipher = new std::uint32_t[2];
        auto A = input[0] + S[0];
        auto B = input[1] + S[1];

        for (int i = 1; i <= r; i++) {
            A = lcs(A ^ B, B) + S[2 * i];
            B = lcs(B ^ A, A) + S[2 * i + 1];
        }

        cipher[0] = A;
        cipher[1] = B;

        return cipher;
    }

    std::uint32_t * decrypt(const std::uint32_t * cipher) {
        auto output = new std::uint32_t[2];
        auto A = cipher[0];
        auto B = cipher[1];

        for (int i = r; i > 0; i--) {
            B = rcs(B - S[2 * i + 1], A) ^ A;
            A = rcs(A - S[2 * i], B) ^ B;
        }

        output[0] = A - S[0];
        output[1] = B - S[1];

        return output;
    }
};
