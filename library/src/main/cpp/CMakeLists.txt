cmake_minimum_required(VERSION 3.10.2)

project(stackblur)

#
# stackblur
#
add_library(stackblur SHARED stackblur.cpp)
target_compile_options(stackblur PUBLIC -ffast-math -Os -funroll-loops -ffast-math -fno-strict-aliasing -fno-math-errno)
target_link_libraries(stackblur jnigraphics log c++_static)
