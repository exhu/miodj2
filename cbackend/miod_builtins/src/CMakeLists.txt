include(GNUInstallDirs)

set(SOURCES
    miod_builtins.c
)

set(PUBLIC_HEADERS
    include/miod_builtins/miod_builtins.h
)

add_library(miod_builtins_lib ${SOURCES} ${PUBLIC_HEADERS})
set_target_properties(miod_builtins_lib PROPERTIES OUTPUT_NAME miod_builtins)
target_compile_features(miod_builtins_lib PUBLIC c_std_99)
target_include_directories(miod_builtins_lib PUBLIC $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}>)
target_include_directories(miod_builtins_lib PUBLIC $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
$<INSTALL_INTERFACE:${CMAKE_INSTALL_INCLUDEDIR}>)

set_target_properties(miod_builtins_lib PROPERTIES
    PUBLIC_HEADER ${PUBLIC_HEADERS}
) 

add_library(miod_builtins::lib ALIAS miod_builtins_lib)

install(TARGETS miod_builtins_lib EXPORT miod_builtins_targets
    PUBLIC_HEADER DESTINATION "${CMAKE_INSTALL_INCLUDEDIR}/miod_builtins")

install(EXPORT miod_builtins_targets
    FILE miod_builtins-config.cmake
    NAMESPACE miod_builtins::
    DESTINATION "${CMAKE_INSTALL_LIBDIR}/cmake/miod_builtins")

# add version file generation here,
# in case of dependencies we need to create manually Config file, configure it and
# install via install(FILES)
# see https://cliutils.gitlab.io/modern-cmake/chapters/install/installing.html
