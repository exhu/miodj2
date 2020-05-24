#include "miod_builtins/miod_builtins.h"

#include <string.h>


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


miod_BaseIntefaceInstance* miod_interface_from_class(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf) {

    if (inst == NULL || interf == NULL) {
        return NULL;
    }

    ptrdiff_t pinst = (ptrdiff_t)inst;
    miod_BaseIntefaceInstance *iinst = (miod_BaseIntefaceInstance*)(pinst + interf->vtbl->base_offset);
    return iinst;
}