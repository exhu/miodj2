cmake_minimum_required(VERSION 3.17)
add_executable(hello hello.c)
target_link_libraries(hello miod_builtins::lib)
target_compile_features(hello PUBLIC c_std_99)

install(TARGETS hello EXPORT hello_targets)
