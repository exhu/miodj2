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
    miod_BaseInterfaceInstance iface1;
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

static void test_new_instance() {
    miod_Class clazz;
    clazz.name = "MyClass";
    clazz.struct_size = sizeof(MyClassInst);
    clazz.init_proc = NULL;
    clazz.destroy_proc = NULL;
    clazz.properties = NULL;

    miod_BaseVtbl iface1_vtbl = {offsetof(MyClassInst, iface1)};
    miod_InterfDesc iface1_desc = {"MyInterf", &iface1_vtbl};
    miod_InterfDesc *descs[] = {&iface1_desc, NULL};
    clazz.interfaces = descs;

    MyClassInst *inst = (MyClassInst*)miod_new_instance(&clazz);

    // check proper tbl initialization
    assert(inst->iface1.vtbl == &iface1_vtbl);

    // TODO check proper constructor initialization

    miod_inst_dec_ref(&inst->base);
}

/* TODO test miod_interface_from_class

miod_BaseClassInstance *miod_class_instance_from_interface(miod_BaseInterfaceInstance *iinst);

void miod_inst_inc_ref(miod_BaseClassInstance *inst);
void miod_inst_dec_ref(miod_BaseClassInstance *inst);
void miod_interface_inst_inc_ref(miod_BaseInterfaceInstance *iinst);
void miod_interface_inst_dec_ref(miod_BaseInterfaceInstance *iinst);
*/

int main() {
    test_no_interfaces();
    test_no_suitable_interface();
    test_interface_found();
    test_new_instance();
    return 0;
}