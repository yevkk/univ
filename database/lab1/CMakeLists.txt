cmake_minimum_required(VERSION 3.16)
project(lab C)

set(CMAKE_C_STANDARD 11)

set(SRC_LIST db.c db.h console_interface.cpp)

add_executable(lab main.c ${SRC_LIST})
