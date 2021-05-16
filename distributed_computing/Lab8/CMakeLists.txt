cmake_minimum_required(VERSION 3.19)
project(Lab8)

set(CMAKE_CXX_STANDARD 17)
set(MPI_DIR ${CMAKE_CURRENT_SOURCE_DIR}/mpi)
set(MPI_LIB msmpi)

add_executable(Lab8 main.cpp)
target_link_directories(Lab8 PRIVATE ${MPI_DIR})
target_link_libraries(Lab8 PRIVATE ${MPI_LIB})

add_executable(serial serial.cpp)
target_link_directories(serial PRIVATE ${MPI_DIR})
target_link_libraries(serial PRIVATE ${MPI_LIB})