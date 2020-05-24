#include "miod_builtins/miod_builtins.h"

#include <string.h>


miod_InterfDesc* miod_class_implements(miod_BaseClassInstance *inst,
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


miod_BaseIntefaceInstance* miod_cast_to(miod_BaseClassInstance *inst,
    miod_InterfDesc *interf) {

    // TODO
}