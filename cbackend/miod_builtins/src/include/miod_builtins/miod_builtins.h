#pragma once

#include <stdbool.h>
#include <stdint.h>
#include <stddef.h>

// actual method table per interface per class
typedef struct {
    // offset from the class instance pointer
    ptrdiff_t base_offset;
} miod_BaseVtbl;

// interface table for an interface per each class that implements it
typedef struct {
    // name with generic params, pattern: name_paramcls1_paramclsN
    const char *name;
    miod_BaseVtbl *vtbl;
} miod_InterfDesc;

typedef struct miod_BaseClassInstance miod_BaseClassInstance;

// TODO optimize for primitive types
typedef miod_BaseClassInstance *(*miod_getter)(void);
typedef void (*miod_setter)(miod_BaseClassInstance *);

typedef struct {
    const char *name;
    miod_getter getter;
    // can be NULL
    miod_setter *setter;
} miod_Property;

typedef void (*miod_init_proc)(miod_BaseClassInstance *inst);
typedef void (*miod_destroy_proc)(miod_BaseClassInstance *inst);

typedef struct {
    // name with generic params, pattern: name_paramcls1_paramclsN
    const char *name;
    // NULL terminated
    miod_InterfDesc **interfaces;
    miod_Property **properties;
    // default constructor, can be null
    miod_init_proc init_proc;
    // destructor
    miod_destroy_proc destroy_proc;
    size_t struct_size;
    int32_t instance_count;
} miod_Class;

typedef struct {
    int32_t ref_counter;
    int32_t weak_counter;
    miod_Class *clazz;
} miod_AnyTypeImpl;

struct miod_BaseClassInstance {
    miod_AnyTypeImpl any_impl;
    // class fields, interface function pointers
    // miod_BaseInterfaceInstance iface1;
    // miod_BaseInterfaceInstance iface2;
};

typedef struct miod_BaseInterfaceInstance miod_BaseInterfaceInstance;
struct miod_BaseInterfaceInstance {
    // we always know the type in the source file, so no need to store instance ptr
    // miod_BaseClassInstance *base_instance;
    miod_BaseVtbl *vtbl;
};

// PropertyChangeNotifier interface vtbl
// If a class implements this interfaces, then each call to set property will trigger
// this method.
typedef struct {
    miod_BaseVtbl base;
    void (*on_property_updated)(void *this, const char *name);
} miod_PropertyChangeNotifier;

miod_BaseClassInstance *miod_new_instance(miod_Class *clazz);

miod_BaseClassInstance *miod_class_instance_from_interface(miod_BaseInterfaceInstance *iinst);

void miod_inst_inc_ref(miod_BaseClassInstance *inst);
void miod_inst_dec_ref(miod_BaseClassInstance **pinst);
void miod_interface_inst_inc_ref(miod_BaseInterfaceInstance *iinst);
void miod_interface_inst_dec_ref(miod_BaseInterfaceInstance **piinst);

void miod_inst_inc_weak_ref(miod_BaseClassInstance *inst);
void miod_inst_dec_weak_ref(miod_BaseClassInstance **pinst);
void miod_interface_inst_inc_weak_ref(miod_BaseInterfaceInstance *iinst);
void miod_interface_inst_dec_weak_ref(miod_BaseInterfaceInstance **piinst);

// returns NULL or interface desc.,
// To support generics one must provide interface name with classes names for args.
miod_InterfDesc* miod_interface_desc_from_class(miod_BaseClassInstance *inst,
    const char *name);

// NULL if any argument is null
miod_BaseInterfaceInstance* miod_interface_from_class(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf);

// Upcasting support. Need to check for compatible generic classes defined in
// different precompiled packages.
// NULL class pointers are not supported!
bool miod_is_same_class(miod_Class *clazz_a, miod_Class *clazz_b);


// some standard types which cannot be implemented in Miod itself (wrappers for value types etc.)
typedef struct {
    miod_BaseClassInstance base;
    int32_t value;
} miod_Integer;

extern miod_Class miod_cls_Integer;

typedef struct {
    miod_BaseClassInstance base;
    const char *value;
    int32_t len;
} miod_String;

extern miod_Class miod_cls_String;

// initialize with a copy of src string data
miod_String* miod_String_from_cstr(const char *src);