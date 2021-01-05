#include "miod_builtins/miod_builtins.h"

#include <assert.h>
#include <stdlib.h>
#include <string.h>

void miod_init_instance(miod_BaseClassInstance *inst, miod_Class *clazz) {
    const size_t sz = clazz->struct_size;
    assert(sz > 0);
    memset(inst, 0, sz);
    inst->any_impl.ref_counter = 1;
    inst->any_impl.clazz = clazz;
    clazz->instance_count++;

    // initialize instance vtbls to interfaces
    miod_InterfDesc **idesc = clazz->interfaces;
    if (idesc != NULL) {
        while(*idesc != NULL) {
            miod_BaseVtbl *vtable = (*idesc)->vtbl;
            ptrdiff_t inst_ptr = (ptrdiff_t)inst;
            ptrdiff_t ivtbl_ptr = inst_ptr + vtable->base_offset;
            miod_BaseInterfaceInstance *i_inst = (miod_BaseInterfaceInstance*)ivtbl_ptr;
            i_inst->vtbl = vtable;
            ++idesc;
        }
    }

    if (clazz->init_proc != NULL) {
        clazz->init_proc(inst);
    }
}

miod_BaseClassInstance *miod_new_instance(miod_Class *clazz) {
    const size_t sz = clazz->struct_size;
    assert(sz > 0);
    miod_BaseClassInstance *inst = malloc(sz);
    miod_init_instance(inst, clazz);
    return inst;
}

miod_BaseClassInstance *miod_class_instance_from_interface(miod_BaseInterfaceInstance *iinst) {
    ptrdiff_t pinst = ((ptrdiff_t)iinst) - iinst->vtbl->base_offset;
    return (miod_BaseClassInstance*) pinst;
}

void miod_inst_inc_ref(miod_BaseClassInstance *inst) {
    assert(inst->any_impl.ref_counter > 0);
    inst->any_impl.ref_counter++;
}

void miod_inst_dec_ref(miod_BaseClassInstance **pinst) {
    miod_BaseClassInstance *inst = *pinst;
    miod_Class *clazz = inst->any_impl.clazz;
    int32_t counter = inst->any_impl.ref_counter;
    counter--;
    inst->any_impl.ref_counter = counter;
    assert(counter >= 0);
    if (counter == 0) {
        miod_destroy_proc destroy_proc = inst->any_impl.clazz->destroy_proc;
        if (destroy_proc != NULL) {
            destroy_proc(inst);
        }

        if (inst->any_impl.weak_counter == 0) {
            free(inst);
            *pinst = NULL;
            clazz->instance_count--;
        }

    }
}

void miod_interface_inst_inc_ref(miod_BaseInterfaceInstance *iinst) {
    miod_BaseClassInstance *inst = miod_class_instance_from_interface(iinst);
    miod_inst_inc_ref(inst);
}

void miod_interface_inst_dec_ref(miod_BaseInterfaceInstance **piinst) {
    miod_BaseClassInstance *inst = miod_class_instance_from_interface(*piinst);
    miod_inst_dec_ref(&inst);
    if (inst == NULL)
    {
        *piinst = NULL;
    }
}

void miod_inst_inc_weak_ref(miod_BaseClassInstance *inst) {
    inst->any_impl.weak_counter++;
}

void miod_inst_dec_weak_ref(miod_BaseClassInstance **pinst) {
    miod_BaseClassInstance *inst = *pinst;
    miod_Class *clazz = inst->any_impl.clazz;
    inst->any_impl.weak_counter--;
    assert(inst->any_impl.weak_counter >= 0);
    if (inst->any_impl.ref_counter == 0 && inst->any_impl.weak_counter == 0) {
        free(inst);
        *pinst = NULL;
        clazz->instance_count--;
    }
}

void miod_interface_inst_inc_weak_ref(miod_BaseInterfaceInstance *iinst) {
    miod_BaseClassInstance *inst = miod_class_instance_from_interface(iinst);
    miod_inst_inc_weak_ref(inst);
}

void miod_interface_inst_dec_weak_ref(miod_BaseInterfaceInstance **piinst) {
    miod_BaseClassInstance *inst = miod_class_instance_from_interface(*piinst);
    miod_inst_dec_weak_ref(&inst);
    if (inst == NULL)
    {
        *piinst = NULL;
    }
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


bool miod_is_same_class(miod_Class *clazz_a, miod_Class *clazz_b) {
    if (clazz_a == clazz_b) return true;
    if (clazz_a->struct_size == clazz_b->struct_size &&
        strcmp(clazz_a->name, clazz_b->name) == 0) return true;

    return false;
}