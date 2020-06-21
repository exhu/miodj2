#pragma once

#include <stdint.h>
#include <stddef.h>

typedef struct {
    // offset from the class instance pointer
    ptrdiff_t base_offset;
} miod_BaseVtbl;

// interface table for an interface per each class that implements it
typedef struct {
    const char *name; // name with generic params
    // TODO generic param types
    miod_BaseVtbl *vtbl;
} miod_InterfDesc;

struct _miod_BaseClassInstance;

// TODO optimize for primitive types
typedef struct _miod_BaseClassInstance *(*miod_getter)(void);
typedef void (*miod_setter)(struct _miod_BaseClassInstance *);

typedef struct {
    const char *name;
    miod_getter getter;
    // can be NULL
    miod_setter *setter;
} miod_Property;

typedef void (*miod_init_proc)(struct _miod_BaseClassInstance *inst);
typedef void (*miod_destroy_proc)(struct _miod_BaseClassInstance *inst);

typedef struct {
    const char *name;
    // NULL terminated
    miod_InterfDesc **interfaces;
    miod_Property **properties;
    // default constructor, can be null
    miod_init_proc init_proc;
    // destructor
    miod_destroy_proc destroy_proc;
    size_t struct_size;
} miod_Class;

typedef struct {
    int32_t ref_counter;
    int32_t weak_counter;
    miod_Class *clazz;
} miod_AnyTypeImpl;

typedef struct _miod_BaseClassInstance {
    miod_AnyTypeImpl any_impl;
    // class fields, interface function pointers
    // miod_BaseInterfaceInstance iface1;
    // miod_BaseInterfaceInstance iface2;
} miod_BaseClassInstance;

typedef struct _miod_BaseInterfaceInstance {
    // we always know the type in the source file, so no need to store instance ptr
    // miod_BaseClassInstance *base_instance;
    miod_BaseVtbl *vtbl;
} miod_BaseInterfaceInstance;

// PropertyChangeNotifier interface vtbl
// If a class implements this interfaces, then each call to set property will trigger
// this method.
typedef struct {
    miod_BaseVtbl baseVtbl;
    void (*on_property_updated)(void *this, const char *name);
} miod_PropertyChangeNotifier;

miod_BaseClassInstance *miod_new_instance(miod_Class *clazz);

miod_BaseClassInstance *miod_class_instance_from_interface(miod_BaseInterfaceInstance *iinst);

void miod_inst_inc_ref(miod_BaseClassInstance *inst);
void miod_inst_dec_ref(miod_BaseClassInstance **pinst);
void miod_interface_inst_inc_ref(miod_BaseInterfaceInstance *iinst);
void miod_interface_inst_dec_ref(miod_BaseInterfaceInstance **piinst);

// returns NULL or interface desc.,
// TODO to support generics one must provide types, not just the name
miod_InterfDesc* miod_interface_desc_from_class(miod_BaseClassInstance *inst,
    const char *name);

// NULL if any argument is null
miod_BaseInterfaceInstance* miod_interface_from_class(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf);