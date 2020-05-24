#include "miod_builtins/miod_builtins.h"

#include <assert.h>

static void test_no_interfaces() {
    miod_Class clazz;
    clazz.name = "MyClass";
    clazz.interfaces = NULL;

    miod_BaseClassInstance inst;
    inst.any_impl.clazz = &clazz;

    miod_InterfDesc* interf = miod_class_implements(&inst, "MyInterf");
    assert(interf == NULL);
}

static void test_no_suitable_interface() {
    miod_Class clazz;
    clazz.name = "MyClass";
    miod_InterfDesc *descs[] = {NULL};
    clazz.interfaces = descs;

    miod_BaseClassInstance inst;
    inst.any_impl.clazz = &clazz;

    miod_InterfDesc* interf = miod_class_implements(&inst, "MyInterf");
    assert(interf == NULL);
}

static void test_interface_found() {
    miod_Class clazz;
    clazz.name = "MyClass";
    miod_InterfDesc desc = {"MyInterf", offsetof(miod_BaseClassInstance, other)};
    miod_InterfDesc *descs[] = {&desc, NULL};
    clazz.interfaces = descs;

    miod_BaseClassInstance inst;
    inst.any_impl.clazz = &clazz;

    miod_InterfDesc* interf = miod_class_implements(&inst, "MyInterf");
    assert(interf == &desc);
}


int main() {
    test_no_interfaces();
    test_no_suitable_interface();
    test_interface_found();
    return 0;
}