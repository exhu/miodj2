project(cbackend LANGUAGES C)

add_library(cbklib "miod_builtins.c")

add_executable(cbktests "tests/miod_builtins_tests.c")
target_link_libraries(cbktests cbklib)