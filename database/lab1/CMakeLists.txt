cmake_minimum_required(VERSION 3.16)
project(lab C)
project(example C)

set(CMAKE_C_STANDARD 11)

set(SRC_LIST db.c db.h)

add_executable(lab main.c console_interface.c ${SRC_LIST})

add_executable(example example.c ${SRC_LIST})

