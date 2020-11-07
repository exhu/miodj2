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

miod_sources_to_c("${MIOD_SOURCES}" "${CMAKE_CURRENT_BINARY_DIR}" GENERATED_C_SOURCES)

miod_collect_all_tags("${MIOD_TAGS}" ENABLED_TAGS)

miod_write_build_params("${ENABLED_TAGS}" "${MIOD_SOURCES}" "${CMAKE_CURRENT_BINARY_DIR}" "${CMAKE_CURRENT_LIST_DIR}")
# TODO different tags lists and sources for Debug, Release etc configurations

# TODO simmulate C generation

add_library(miodlib "${GENERATED_C_SOURCES}")

add_custom_command(OUTPUT ${GENERATED_C_SOURCES} COMMAND ${CMAKE_COMMAND}
    ARGS -P "${CMAKE_CURRENT_LIST_DIR}/fake_compiler.cmake"
    DEPENDS ${MIOD_SOURCES} WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR})