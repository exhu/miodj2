#include "miod_builtins/miod_builtins.h"

#include <assert.h>
#include <memory.h>

static void test_no_interfaces() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    clazz.interfaces = NULL;

    miod_BaseClassInstance inst;
    inst.any_impl.clazz = &clazz;

    miod_InterfDesc *interf = miod_interface_desc_from_class(&inst, "MyInterf");
    assert(interf == NULL);
}

static void test_no_suitable_interface() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    miod_InterfDesc *descs[] = {NULL};
    clazz.interfaces = descs;

    miod_BaseClassInstance inst;
    inst.any_impl.clazz = &clazz;

    miod_InterfDesc* interf = miod_interface_desc_from_class(&inst, "MyInterf");
    assert(interf == NULL);
}

static void test_interface_found() {
    typedef struct {
        miod_BaseClassInstance base;
        miod_BaseInterfaceInstance iface1;
    } MyClassInst;

    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
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

typedef struct {
    miod_BaseClassInstance base;
    miod_BaseInterfaceInstance iface1;
    int test_field;
    int *test_ptr;
} MyClassWithFieldInst;

static void test_new_instance() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    clazz.struct_size = sizeof(MyClassWithFieldInst);

    miod_BaseVtbl iface1_vtbl = {offsetof(MyClassWithFieldInst, iface1)};
    miod_InterfDesc iface1_desc = {"MyInterf", &iface1_vtbl};
    miod_InterfDesc *descs[] = {&iface1_desc, NULL};
    clazz.interfaces = descs;

    MyClassWithFieldInst *inst = (MyClassWithFieldInst*)miod_new_instance(&clazz);
    assert(inst != NULL);

    // check proper tbl initialization
    assert(inst->iface1.vtbl == &iface1_vtbl);
    assert(inst->base.any_impl.ref_counter == 1);

    miod_inst_dec_ref((miod_BaseClassInstance**)&inst);
    assert(inst == NULL);
}

static void myclass_init_proc(struct _miod_BaseClassInstance *inst) {
    MyClassWithFieldInst *myinst = (MyClassWithFieldInst*)inst;
    myinst->test_field = 12345678;
}

static void myclass_destroy_proc(struct _miod_BaseClassInstance *inst) {
    MyClassWithFieldInst *myinst = (MyClassWithFieldInst*)inst;
    assert(myinst->test_ptr != NULL);
    myinst->test_ptr[0] = 98765432;
}

static void test_new_instance_constructor_and_destructor() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    clazz.struct_size = sizeof(MyClassWithFieldInst);
    clazz.init_proc = myclass_init_proc;
    clazz.destroy_proc = myclass_destroy_proc;

    miod_BaseVtbl iface1_vtbl = {offsetof(MyClassWithFieldInst, iface1)};
    miod_InterfDesc iface1_desc = {"MyInterf", &iface1_vtbl};
    miod_InterfDesc *descs[] = {&iface1_desc, NULL};
    clazz.interfaces = descs;

    MyClassWithFieldInst *inst = (MyClassWithFieldInst*)miod_new_instance(&clazz);
    assert(inst != NULL);

    // test constructor
    assert(inst->test_field == 12345678);

    // test destructor
    int test_value = 1111;
    inst->test_ptr = &test_value;
    miod_inst_dec_ref((miod_BaseClassInstance**)&inst);
    assert(inst == NULL);
    assert(test_value == 98765432);
}

static void test_miod_interface_from_class() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    clazz.struct_size = sizeof(MyClassWithFieldInst);

    miod_BaseVtbl iface1_vtbl = {offsetof(MyClassWithFieldInst, iface1)};
    miod_InterfDesc iface1_desc = {"MyInterf", &iface1_vtbl};
    miod_InterfDesc *descs[] = {&iface1_desc, NULL};
    clazz.interfaces = descs;

    MyClassWithFieldInst *inst = (MyClassWithFieldInst*)miod_new_instance(&clazz);
    assert(inst != NULL);
    miod_BaseInterfaceInstance *int_inst = miod_interface_from_class(
        (miod_BaseClassInstance*)inst, &iface1_desc);
    assert(int_inst != NULL);
    assert(int_inst == &(inst->iface1));
    miod_inst_dec_ref((miod_BaseClassInstance**)&inst);
}

static void test_class_inst_from_interface() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    clazz.struct_size = sizeof(MyClassWithFieldInst);

    miod_BaseVtbl iface1_vtbl = {offsetof(MyClassWithFieldInst, iface1)};
    miod_InterfDesc iface1_desc = {"MyInterf", &iface1_vtbl};
    miod_InterfDesc *descs[] = {&iface1_desc, NULL};
    clazz.interfaces = descs;

    MyClassWithFieldInst *inst = (MyClassWithFieldInst*)miod_new_instance(&clazz);
    assert(inst != NULL);
    miod_BaseInterfaceInstance *iface_inst = &(inst->iface1);
    miod_BaseClassInstance *cls_inst = miod_class_instance_from_interface(iface_inst);
    assert((ptrdiff_t)cls_inst == (ptrdiff_t)inst);
    miod_inst_dec_ref((miod_BaseClassInstance**)&inst);
}

static void test_inst_inc_dec_ref() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    clazz.struct_size = sizeof(MyClassWithFieldInst);
    MyClassWithFieldInst *inst = (MyClassWithFieldInst*)miod_new_instance(&clazz);
    assert(inst != NULL);

    miod_inst_inc_ref((miod_BaseClassInstance*)inst);

    assert(inst->base.any_impl.ref_counter == 2);

    miod_inst_dec_ref((miod_BaseClassInstance**)&inst);
    assert(inst != NULL);
    assert(inst->base.any_impl.ref_counter == 1);
    miod_inst_dec_ref((miod_BaseClassInstance**)&inst);
    assert(inst == NULL);
}

static void test_interface_inc_dec_ref() {
    miod_Class clazz;
    memset(&clazz, 0, sizeof(clazz));
    clazz.name = "MyClass";
    clazz.struct_size = sizeof(MyClassWithFieldInst);

    miod_BaseVtbl iface1_vtbl = {offsetof(MyClassWithFieldInst, iface1)};
    miod_InterfDesc iface1_desc = {"MyInterf", &iface1_vtbl};
    miod_InterfDesc *descs[] = {&iface1_desc, NULL};
    clazz.interfaces = descs;

    MyClassWithFieldInst *inst = (MyClassWithFieldInst*)miod_new_instance(&clazz);
    assert(inst != NULL);
    miod_BaseInterfaceInstance *iface_inst = &(inst->iface1);
    miod_interface_inst_inc_ref(iface_inst);
    assert(inst->base.any_impl.ref_counter == 2);
    miod_interface_inst_dec_ref(&iface_inst);
    assert(iface_inst != NULL);
    assert(inst->base.any_impl.ref_counter == 1);
    miod_interface_inst_dec_ref(&iface_inst);
    assert(iface_inst == NULL);
}


int main() {
    test_no_interfaces();
    test_no_suitable_interface();
    test_interface_found();
    test_new_instance();
    test_miod_interface_from_class();
    test_class_inst_from_interface();
    test_inst_inc_dec_ref();
    test_interface_inc_dec_ref();
    return 0;
}