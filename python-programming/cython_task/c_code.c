#include <stdint.h>
#include <stdlib.h>
// #include <stdio.h>

char *f(int *keys, int *values, int size) {
    if (size == 0) {
        return NULL;
    }

    uint64_t a = 1, b = 1;
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < values[i]; j++) {
            a *= keys[i];
        }
        for (int j = 0; j < keys[i]; j++) {
            b *= values[i];
        }
    }

    uint64_t max, tmp;
    if (a > b) {
        max = tmp = a;
    } else {
        max = tmp = b;
    }

    int char_count = 0;
    while (tmp > 0) {
        tmp /= 10;
        char_count++;
    }

    char *res = (char *) malloc((char_count + 1) * sizeof(char));
    char char_digits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    for (int i = 0; i < char_count; i++) {
        res[char_count - 1 - i] = char_digits[max % 10];
        max /= 10;
    }
    res[char_count - 1] = 0;

    return res;
}

/* testing purposes
int main() {
    int keys[3] = {2, 3, 5};
    int vals[3] = {10, 7, 3};
    char *res = f(keys, vals, 3);

    for (int i = 0; i < 9; i++) {
        printf("%d", res[i]);
    }
    printf("\n");
    return 0;
}
*/