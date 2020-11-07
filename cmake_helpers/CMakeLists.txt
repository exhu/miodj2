cmake_minimum_required(VERSION 3.17)
project(nogen)

include(miod_support.cmake)

# TODO define tags and sources in cmake, pass only used sources and tags to the
# Miod compiler.
set(MIOD_TAG_feature_a OFF CACHE BOOL "Feature A")
set(MIOD_TAG_feature_b OFF CACHE BOOL "Feature B")

set(MIOD_SOURCES mod/feature_a.miod)
set(MIOD_TAGS )

if(MIOD_TAG_feature_b)
list(APPEND MIOD_SOURCES feature_b.miod)
list(APPEND MIOD_TAGS feature_b)
endif()

if(MIOD_TAG_feature_a)
list(APPEND MIOD_TAGS feature_a)
endif()

miod_sources_to_c("${MIOD_SOURCES}" "${CMAKE_CURRENT_BINARY_DIR}" GENERATED_C_SOURCES)

list(JOIN MIOD_TAGS ",\n" JOINED_TAGS)
list(JOIN GENERATED_C_SOURCES ",\n" JOINED_SOURCES)

file(WRITE enabled_tags.txt ${JOINED_TAGS})
file(WRITE enabled_sources.txt ${JOINED_SOURCES})

# TODO extract tags and sources manipulation to a cmake module
# TODO simmulate C generation