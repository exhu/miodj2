#include "miod_builtins/miod_builtins.h"

#include <assert.h>

static void test_no_interfaces() {
    miod_Class clazz;
    clazz.name = "MyClass";
    clazz.interfaces = NULL;

    miod_BaseClassInstance inst;
    inst.any_impl.clazz = &clazz;

    miod_InterfDesc *interf = miod_interface_desc_from_class(&inst, "MyInterf");
    assert(interf == NULL);
}

static void test_no_suitable_interface() {
    miod_Class clazz;
    clazz.name = "MyClass";
    miod_InterfDesc *descs[] = {NULL};
    clazz.interfaces = descs;

    miod_BaseClassInstance inst;
    inst.any_impl.clazz = &clazz;

    miod_InterfDesc* interf = miod_interface_desc_from_class(&inst, "MyInterf");
    assert(interf == NULL);
}

typedef struct {
    miod_BaseClassInstance base;
    miod_BaseIntefaceInstance iface1;
} MyClassInst;

static void test_interface_found() {
    miod_Class clazz;
    clazz.name = "MyClass";

    miod_BaseVtbl vtbl = {offsetof(MyClassInst, iface1)};

    miod_InterfDesc desc = {"MyInterf", &vtbl};
    miod_InterfDesc *descs[] = {&desc, NULL};
    clazz.interfaces = descs;

    MyClassInst inst;
    inst.base.any_impl.clazz = &clazz;

    miod_InterfDesc* interf = miod_interface_desc_from_class(&inst.base, "MyInterf");
    assert(interf == &desc);
}

// TODO test miod_interface_from_class

int main() {
    test_no_interfaces();
    test_no_suitable_interface();
    test_interface_found();
    return 0;
}