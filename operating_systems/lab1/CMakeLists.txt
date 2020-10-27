cmake_minimum_required(VERSION 3.17)
project(lab)

set(CMAKE_CXX_STANDARD 20)

set(TOP_DIR ${CMAKE_SOURCE_DIR})
set(SRC_DIR ${TOP_DIR}/src)

set(DEMO_LIB_NAME demofuncs)
add_library(${DEMO_LIB_NAME} INTERFACE)
target_include_directories(${DEMO_LIB_NAME} INTERFACE demolib)

set(SRC_LIST
        ${SRC_DIR}/manager.hpp
        )

set(LIB_NAME ${PROJECT_NAME}core)
add_library(${LIB_NAME} INTERFACE)
target_sources(${LIB_NAME} INTERFACE ${SRC_LIST})

add_executable(manager main.cpp)
target_link_libraries(manager PRIVATE ${LIB_NAME} ws2_32)

add_executable(worker ${SRC_DIR}/worker.cpp)
target_link_libraries(worker PRIVATE ${DEMO_LIB_NAME} ws2_32)

add_dependencies(manager worker)
