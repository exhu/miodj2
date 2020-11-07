cmake_minimum_required(VERSION 3.17)

# see miod_declare_tags(), miod_add_compiler_command() for usage below.

list(LENGTH CMAKE_CONFIGURATION_TYPES CONF_COUNT)
if(CONF_COUNT GREATER 1)
    message(FATAL_ERROR "Multiple build types = ${CMAKE_CONFIGURATION_TYPES} are not supported")
    # TODO support via CMAKE_CFG_INTDIR or $<CONFIG>
else()
    set(ACTUAL_BUILD_TYPE ${CMAKE_BUILD_TYPE})
    if(NOT ACTUAL_BUILD_TYPE)
        set(ACTUAL_BUILD_TYPE ${CMAKE_CONFIGURATION_TYPES})
    endif()
    message("Config = ${ACTUAL_BUILD_TYPE}")
endif()

# Produces C sources list from the passed Miod sources list.
# MIOD_SOURCES must be relative package paths like "package/private/mod.miod"
# OUT_BINARY_DIR = ${CMAKE_CURRENT_BINARY_DIR} for generated C sources
# RESULT_VAR_OUT_C_SOURCES = name of the result variable
function(miod_sources_to_c MIOD_SOURCES OUT_BINARY_DIR RESULT_VAR_OUT_C_SOURCES)
    set(OUT_C_SOURCES)
    foreach(item IN LISTS MIOD_SOURCES)
        string(REPLACE "/" "_" OUT_ITEM "${item}")
        string(REPLACE "\.miod" ".c" OUT_ITEM "${OUT_ITEM}")
        list(APPEND OUT_C_SOURCES "${OUT_BINARY_DIR}/${OUT_ITEM}")
    endforeach()
    set(${RESULT_VAR_OUT_C_SOURCES} "${OUT_C_SOURCES}" PARENT_SCOPE)
endfunction()

# Sets CACHE variables, TAG_NAMES = list of tags, OPTIONAL_DESCRIPTIONS -- list
# of descriptions, can be empty
function(miod_declare_tags TAG_NAMES OPTIONAL_DESCRIPTIONS DEFAULT_VALUE)
    foreach(tag desc IN ZIP_LISTS TAG_NAMES OPTIONAL_DESCRIPTIONS)
        set(MIOD_TAG_${tag} ${DEFAULT_VALUE} CACHE BOOL "${desc}")
    endforeach()
endfunction()


# Produces list of enabled tags to pass to the Miod compiler.
function(miod_collect_tags TAG_NAMES RESULT_VAR)
    set(OUT_TAGS)
    foreach(tag IN LISTS TAG_NAMES)
        if(MIOD_TAG_${tag})
            list(APPEND OUT_TAGS "${tag}")
        endif()
    endforeach()

    set(${RESULT_VAR} "${OUT_TAGS}" PARENT_SCOPE)
endfunction()


# The same as miod_collect_tags, plus appends debug,release,os etc tags based
# on the cmake compilation info.
function(miod_collect_all_tags TAG_NAMES RESULT_VAR)
    set(OUT_TAGS)
    miod_collect_tags("${TAG_NAMES}" OUT_TAGS)
    # Debug, Release, RelWithDebInfo
    if(ACTUAL_BUILD_TYPE STREQUAL Debug)
        list(APPEND OUT_TAGS debug)
    else()
        list(APPEND OUT_TAGS release)
    endif()

    set(${RESULT_VAR} "${OUT_TAGS}" PARENT_SCOPE)
endfunction()


function(miod_write_build_params TAG_NAMES SOURCES OUT_BINARY_DIR SOURCES_DIR)
    list(JOIN TAG_NAMES " " TAG_NAMES)
    list(JOIN SOURCES " " SOURCES)
    set(BUILD_PARAMS_CONTENTS "set(MIOD_SOURCES_DIR ${SOURCES_DIR})\nset(MIOD_TAGS ${TAG_NAMES})\nset(MIOD_SOURCES ${SOURCES})\n")
    file(WRITE "${OUT_BINARY_DIR}/miod_build_params.txt" ${BUILD_PARAMS_CONTENTS})
endfunction()


# The only function you need to build miod files.
# MIOD_TAGS = list of all declared tags
# SOURCES_ROOT = package root path (CMAKE_CURRENT_LIST_DIR)
# MIOD_SOURCES = list of relative paths to miod files 
# C_OUT_DIR = binary dir for generated C sources (CMAKE_CURRENT_BINARY_DIR)
# RESULT_VAR_GENERATED_SOURCES = variable name for generated list of C sources paths
function(miod_add_compiler_command MIOD_TAGS SOURCES_ROOT MIOD_SOURCES C_OUT_DIR RESULT_VAR_GENERATED_SOURCES)
    miod_sources_to_c("${MIOD_SOURCES}" "${C_OUT_DIR}" GENERATED_C_SOURCES)
    miod_collect_all_tags("${MIOD_TAGS}" ENABLED_TAGS)
    miod_write_build_params("${ENABLED_TAGS}" "${MIOD_SOURCES}" "${C_OUT_DIR}" "${SOURCES_ROOT}")

    add_custom_command(OUTPUT ${GENERATED_C_SOURCES} COMMAND ${CMAKE_COMMAND}
        ARGS -P "${CMAKE_CURRENT_LIST_DIR}/fake_compiler.cmake"
        DEPENDS ${MIOD_SOURCES} WORKING_DIRECTORY ${C_OUT_DIR})

    set(${RESULT_VAR_GENERATED_SOURCES} "${GENERATED_C_SOURCES}" PARENT_SCOPE)
endfunction()