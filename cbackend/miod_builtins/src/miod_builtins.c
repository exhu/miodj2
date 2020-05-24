#include "miod_builtins/miod_builtins.h"

#include <assert.h>
#include <malloc.h>
#include <string.h>

miod_BaseClassInstance *miod_new_instance(miod_Class *clazz) {
    const size_t sz = clazz->struct_size;
    assert(sz > 0);
    miod_BaseClassInstance *inst = malloc(sz);
    memset(inst, 0, sz);
    inst->any_impl.ref_counter = 1;
    inst->any_impl.clazz = clazz;
    // TODO initialize vtbls
    if (clazz->init_proc != NULL) {
        clazz->init_proc(inst);
    }
    return inst;
}

miod_BaseClassInstance *miod_class_instance_from_interface(miod_BaseInterfaceInstance *iinst) {
    ptrdiff_t pinst = ((ptrdiff_t)iinst) - iinst->vtbl->base_offset;
    return (miod_BaseClassInstance*) pinst;
}

void miod_inst_inc_ref(miod_BaseClassInstance *inst) {
    inst->any_impl.ref_counter++;
}

void miod_inst_dec_ref(miod_BaseClassInstance *inst) {
    int32_t counter = inst->any_impl.ref_counter;
    counter--;
    inst->any_impl.ref_counter = counter;
    assert(counter >= 0);
    if (counter == 0) {
        miod_destroy_proc destroy_proc = inst->any_impl.clazz->destroy_proc;
        if (destroy_proc != NULL) {
            destroy_proc(inst);
        }
        free(inst);
    }
}

void miod_interface_inst_inc_ref(miod_BaseInterfaceInstance *iinst) {
    miod_BaseClassInstance *inst = miod_class_instance_from_interface(iinst);
    miod_inst_inc_ref(inst);
}

void miod_interface_inst_dec_ref(miod_BaseInterfaceInstance *iinst) {
    miod_BaseClassInstance *inst = miod_class_instance_from_interface(iinst);
    miod_inst_dec_ref(inst);
}

miod_InterfDesc* miod_interface_desc_from_class(miod_BaseClassInstance *inst,
    const char *name) {

    miod_InterfDesc **cur_interf = inst->any_impl.clazz->interfaces;
    while(cur_interf != NULL && *cur_interf != NULL) {
        if (strcmp(name, (*cur_interf)->name) == 0) {
            break;
        }
        cur_interf++;
    }

    if (cur_interf) {
        return *cur_interf;
    }

    return NULL;
}


miod_BaseInterfaceInstance* miod_interface_from_class(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf) {

    if (inst == NULL || interf == NULL) {
        return NULL;
    }

    ptrdiff_t pinst = (ptrdiff_t)inst;
    miod_BaseInterfaceInstance *iinst = (miod_BaseInterfaceInstance*)(pinst + interf->vtbl->base_offset);
    return iinst;
}