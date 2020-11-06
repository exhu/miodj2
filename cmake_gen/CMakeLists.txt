cmake_minimum_required(VERSION 3.17)
project(hello1)

set(GEN_CMAKE_FILE ${CMAKE_CURRENT_BINARY_DIR}/gen.cmake)
set(GEN_COMMAND ${CMAKE_CURRENT_LIST_DIR}/gen.sh)
set(GEN_SRC_COMMAND ${CMAKE_CURRENT_LIST_DIR}/gen_src.sh)

execute_process(COMMAND ${GEN_COMMAND} ${GEN_CMAKE_FILE} WORKING_DIRECTORY ${CMAKE_CURRENT_LIST_DIR})

include(${GEN_CMAKE_FILE})

add_executable(hello1 ${GEN_SOURCES})
# cmake generation
add_custom_command(OUTPUT ${GEN_CMAKE_FILE} COMMAND ${GEN_COMMAND} ARGS ${GEN_CMAKE_FILE}
    DEPENDS sources.txt WORKING_DIRECTORY ${CMAKE_CURRENT_LIST_DIR})
add_custom_target(gen_src_list DEPENDS ${GEN_CMAKE_FILE})
add_dependencies(hello1 gen_src_list)

# sources generation
add_custom_command(OUTPUT ${GEN_SOURCES} COMMAND ${GEN_SRC_COMMAND} ARGS ${GEN_SOURCES}
    DEPENDS gen_src_list ${GEN_SOURCES_INPUT} WORKING_DIRECTORY ${CMAKE_CURRENT_LIST_DIR})
