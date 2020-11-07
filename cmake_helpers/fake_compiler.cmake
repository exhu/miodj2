include(miod_build_params.txt)
include(${CMAKE_CURRENT_LIST_DIR}/miod_support.cmake)

miod_sources_to_c("${MIOD_SOURCES}" "${CMAKE_CURRENT_BINARY_DIR}" OUT_SOURCES)

foreach(miod_file c_file IN ZIP_LISTS MIOD_SOURCES OUT_SOURCES)
    configure_file("${MIOD_SOURCES_DIR}/${miod_file}" "${c_file}" COPYONLY)
endforeach()