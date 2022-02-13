from libc.stdlib cimport malloc

cdef extern from "c_code.c":
    char *f(int *keys, int *values, int size)

def compute(input_data):
    cdef int size = len(input_data)
    cdef int *keys = <int *> malloc(size * sizeof(char))
    cdef int *values = <int *> malloc(size * sizeof(char))

    for i, key in enumerate(input_data):
        keys[i] = key
        values[i] = input_data[key]

    cdef char *cresult
    cresult = f(keys, values, size)

    return str(cresult.decode("utf-8"))
