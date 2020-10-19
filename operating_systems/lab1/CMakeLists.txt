cmake_minimum_required(VERSION 3.17)
project(lab)

set(CMAKE_CXX_STANDARD 20)

set(TOP_DIR ${CMAKE_SOURCE_DIR})

set(DEMO_LIB_NAME demofuncs)
add_library(${DEMO_LIB_NAME} INTERFACE)
target_include_directories(${DEMO_LIB_NAME} INTERFACE demolib)

add_executable(lab main.cpp)
target_link_libraries(lab PRIVATE ${DEMO_LIB_NAME})


