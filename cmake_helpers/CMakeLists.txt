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

list(JOIN ENABLED_TAGS ",\n" JOINED_TAGS)
list(JOIN GENERATED_C_SOURCES ",\n" JOINED_SOURCES)

file(WRITE enabled_tags.txt ${JOINED_TAGS})
file(WRITE enabled_sources.txt ${JOINED_SOURCES})

# TODO different tags lists and sources for Debug, Release etc configurations

# TODO simmulate C generation