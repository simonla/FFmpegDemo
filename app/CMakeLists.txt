cmake_minimum_required(VERSION 3.4.1)

file(GLOB my_source src/main/cpp/*.cpp src/main/cpp/*.c)
set(distribution_DIR ${CMAKE_SOURCE_DIR}/libs)
set(CURRENT_DIR ${CMAKE_SOURCE_DIR})

add_library(native-lib
        SHARED
        ${my_source}
        )

add_library(avcodec SHARED IMPORTED)
add_library(avdevice SHARED IMPORTED)
add_library(avfilter SHARED IMPORTED)
add_library(avformat SHARED IMPORTED)
add_library(avutil SHARED IMPORTED)
add_library(swscale SHARED IMPORTED)
add_library(swresample SHARED IMPORTED)

set_target_properties(swresample
        PROPERTIES IMPORTED_LOCATION
        ${CURRENT_DIR}/libs/${ANDROID_ABI}/libswresample.so
        )
set_target_properties(avcodec
        PROPERTIES IMPORTED_LOCATION
        ${CURRENT_DIR}/libs/${ANDROID_ABI}/libavcodec.so
        )
set_target_properties(avdevice
        PROPERTIES IMPORTED_LOCATION
        ${CURRENT_DIR}/libs/${ANDROID_ABI}/libavdevice.so
        )
set_target_properties(avfilter
        PROPERTIES IMPORTED_LOCATION
        ${CURRENT_DIR}/libs/${ANDROID_ABI}/libavfilter.so
        )
set_target_properties(avformat
        PROPERTIES IMPORTED_LOCATION
        ${CURRENT_DIR}/libs/${ANDROID_ABI}/libavformat.so
        )
set_target_properties(avutil
        PROPERTIES IMPORTED_LOCATION
        ${CURRENT_DIR}/libs/${ANDROID_ABI}/libavutil.so
        )
set_target_properties(swscale
        PROPERTIES IMPORTED_LOCATION
        ${CURRENT_DIR}/libs/${ANDROID_ABI}/libswscale.so
        )

include_directories(libs/include)
target_link_libraries(native-lib
        -landroid
        avcodec avdevice avfilter avformat avutil swscale swresample
        log
        android
        OpenSLES
        )