# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

project("openclnative")
add_library(native-lib SHARED
                native-lib.cpp
                libopencl.c
                refNR.cpp
                openCLNR.cpp
        )


find_library(log-lib  log)

target_link_libraries(native-lib ${log-lib} jnigraphics)
target_include_directories(native-lib PRIVATE ${CMAKE_CURRENT_SOURCE_DIR}/include)