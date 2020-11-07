cmake_minimum_required(VERSION 3.17)
project(nogen)

include(miod_support.cmake)

set(MIOD_TAGS feature_a feature_b)
miod_declare_tags("${MIOD_TAGS}" "" ON)

set(MIOD_SOURCES mod/feature_a.miod)

if(MIOD_TAG_feature_b)
list(APPEND MIOD_SOURCES feature_b.miod)
endif()

if(MIOD_TAG_feature_a)
list(APPEND MIOD_SOURCES feature_a.miod)
endif()

miod_add_compiler_command("${MIOD_TAGS}" "${CMAKE_CURRENT_LIST_DIR}" "${MIOD_SOURCES}" "${CMAKE_CURRENT_BINARY_DIR}" GENERATED_C_SOURCES)

add_library(miodlib "${GENERATED_C_SOURCES}")