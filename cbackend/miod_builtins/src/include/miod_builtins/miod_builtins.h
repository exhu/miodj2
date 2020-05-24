#pragma once

#include <stdint.h>
#include <stddef.h>


typedef struct {
    const char *name; // name with generic params
    ptrdiff_t base_offset;
    // TODO generic param types
} miod_InterfDesc;

struct _miod_BaseIntefaceInstance;

// TODO optimize for primitive types
// TODO to class instance pointer
typedef struct _miod_BaseIntefaceInstance *(*miod_getter)(void);
typedef void (*miod_setter)(struct _miod_BaseIntefaceInstance *);

typedef struct {
    const char *name;
    miod_getter getter;
    // can be NULL
    miod_setter *setter;
} miod_Property;

typedef void (*miod_init_proc)(void *inst);
typedef void (*miod_destroy_proc)(void *inst);

typedef struct {
    const char *name;
    // NULL terminated
    miod_InterfDesc **interfaces;
    miod_Property **properties;
    // default constructor, can be null
    miod_init_proc init_proc;
    // destructor
    miod_destroy_proc destroy_proc;
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

typedef struct _miod_BaseIntefaceInstance {
    // we always know the type in the source file, so no need to store instance ptr
    // miod_BaseClassInstance *base_instance;
    void *vtbl;
} miod_BaseIntefaceInstance;

// PropertyChangeNotifier interface vtbl
typedef struct {
    // offset from the class instance pointer
    ptrdiff_t base_offset;
    void (*on_property_updated)(void *this, const char *name);
} miod_PropertyChangeNotifier;

// returns NULL or interface desc.,
// TODO to support generics one must provide types, not just the name
miod_InterfDesc* miod_class_implements(miod_BaseClassInstance *inst,
    const char *name);

miod_BaseIntefaceInstance* miod_cast_to(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf);