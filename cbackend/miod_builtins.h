#pragma once

#include <stdint.h>
#include <stddef.h>


typedef struct {
    const char *name; // name with generic params
    ptrdiff_t base_offset;
    // TODO generic param types
} miod_InterfDesc;


typedef void (*miod_defaults_proc)(void *inst);

typedef struct {
    const char *name;
    miod_InterfDesc **interfaces;

    // defaults constructor, can be null
    miod_defaults_proc defaults_proc;
    void(*destroy_func)();
} miod_Class;

typedef struct {
    int32_t ref_counter;
    miod_Class *clazz;
} miod_AnyTypeImpl;

typedef struct {
    miod_AnyTypeImpl any_impl;
    void *other[0]; // class fields, interface function pointers
    // miod_BaseInterfaceInstance iface1;
    // miod_BaseInterfaceInstance iface2;
} miod_BaseClassInstance;

typedef struct {
    miod_BaseClassInstance *base_instance;
    void *vtbl;
} miod_BaseIntefaceInstance;


// returns NULL or interface desc.,
// TODO to support generics one must provide types, not just the name
miod_InterfDesc* miod_class_implements(miod_BaseClassInstance *inst,
    const char *name);

miod_BaseIntefaceInstance* miod_cast_to(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf);