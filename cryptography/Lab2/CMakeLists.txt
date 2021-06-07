cmake_minimum_required(VERSION 3.19)
project(RC5)

set(TOP_DIR ${CMAKE_SOURCE_DIR})

set(CMAKE_CXX_STANDARD 14)

add_executable(RC5 main.cpp rc5.hpp)

add_subdirectory(${TOP_DIR}/tests)