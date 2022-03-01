cmake_minimum_required(VERSION 3.18.5)

project(stackblur LANGUAGES C CXX)

set(CMAKE_SYSROOT /data/data/com.termux/files/home/ndk-r21e-sysroot/sysroot)

add_library(stackblur SHARED stackblur.cpp)
target_compile_options(stackblur PUBLIC -ffast-math -O3 -funroll-loops)

target_link_libraries(stackblur log jnigraphics)
