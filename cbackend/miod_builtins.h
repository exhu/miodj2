#pragma once

#include <stdint.h>
#include <stddef.h>


typedef struct {
    const char *name;
    size_t base_offset;
} miod_InterfDesc;

typedef struct {
    const char *name;
    miod_InterfDesc **interfaces;
} miod_Class;

typedef struct {
    int32_t ref_counter;
    miod_Class *clazz;
    void(*destroy_func)();
} miod_AnyTypeImpl;

typedef struct {
    miod_AnyTypeImpl any_impl;
    void *other[0];
} miod_BaseClassInstance;

typedef struct {
    miod_BaseClassInstance *base_instance;
} miod_BaseIntefaceInstance;


// returns NULL or interface desc.
miod_InterfDesc* miod_class_implements(miod_BaseClassInstance *inst,
    const char *name);

miod_BaseIntefaceInstance* miod_cast_to(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf);