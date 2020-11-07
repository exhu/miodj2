cmake_minimum_required(VERSION 3.17)

if(CMAKE_CONFIGURATION_TYPES)
message(FATAL_ERROR "Multiple build types = ${CMAKE_CONFIGURATION_TYPES} are not supported")
# TODO support via CMAKE_CFG_INTDIR or $<CONFIG>
else()
message("Config = ${CMAKE_CFG_INTDIR}")
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
        list(APPEND OUT_C_SOURCES "${OUT_BINARY_DIR}/${CMAKE_CFG_INTDIR}/${OUT_ITEM}")
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
    if(CMAKE_BUILD_TYPE STREQUAL Debug)
        list(APPEND OUT_TAGS debug)
    else()
        list(APPEND OUT_TAGS release)
    endif()

    set(${RESULT_VAR} "${OUT_TAGS}" PARENT_SCOPE)
endfunction()