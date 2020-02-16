#pragma once

#include <stdint.h>
#include <stddef.h>


typedef struct {
    const char *name;
    size_t base_offset;
} miod_InterfPair;

typedef struct {
    const char *name;
    miod_InterfPair **interfaces;
} miod_Class;

typedef struct {
    miod_BaseClassInstance *base_instance;
} miod_BaseIntefaceInstance;

typedef struct {
    miod_AnyTypeImpl any_impl;
} miod_BaseClassInstance;

typedef struct {
    int32_t ref_counter;
    miod_Class *clazz;
    void(*destroy_func)();
} miod_AnyTypeImpl;