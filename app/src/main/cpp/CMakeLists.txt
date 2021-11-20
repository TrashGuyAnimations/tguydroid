# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.15.2)

# Declares and names the project.

project(tguydroid C)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        tguydroid

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        tguydroid.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

include(FetchContent)

FetchContent_Declare(
        libtguy
        GIT_REPOSITORY https://github.com/Wirtos/libtguy
        GIT_TAG cace87ff8620c5d8df9b8929c779448603a7bf2e
)

FetchContent_MakeAvailable(libtguy)
# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        tguydroid

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        TGuy::TGuy
        )