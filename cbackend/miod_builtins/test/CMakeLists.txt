set(TESTS
    miod_builtins_tests
)

foreach(T ${TESTS})
    add_executable(${T} ${T}.c)
    target_link_libraries(${T} miod_builtins_lib)
    target_compile_features(${T} PUBLIC c_std_99)
    add_test(NAME ${T} COMMAND "${T}")
endforeach()
