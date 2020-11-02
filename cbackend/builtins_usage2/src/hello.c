#include "miod_builtins/miod_builtins.h"

#include <stdio.h>

int main(int argc, char const *argv[])
{
    #ifdef NDEBUG
    printf("No debug\n%p\n", miod_class_instance_from_interface);
    #else
    printf("With debug\n%p\n", miod_class_instance_from_interface);
    #endif
    return 0; //miod_builtins(argc, argv);
}