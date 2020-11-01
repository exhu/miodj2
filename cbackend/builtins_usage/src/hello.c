#include "miod_builtins/miod_builtins.h"

#include <stdio.h>

int main(int argc, char const *argv[])
{
    #ifdef NDEBUG
    printf("No debug\n");
    #else
    printf("With debug\n");
    #endif
    return 0; //miod_builtins(argc, argv);
}