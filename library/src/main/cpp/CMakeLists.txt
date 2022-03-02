cmake_minimum_required(VERSION 3.10.2)

project(stackblur LANGUAGES C CXX)

set(CMAKE_SYSROOT /data/data/com.termux/files/home/ndk-r21e-sysroot/sysroot)

add_library(stackblur SHARED stackblur.cpp)
target_compile_options(stackblur PUBLIC -ffast-math -O3 -funroll-loops)

find_library(log-lib log)
target_link_libraries(stackblur ${log-lib} jnigraphics)
