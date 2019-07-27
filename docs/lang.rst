Language reference
==================

Goal
----

Close (preferably zero-cost) integration with C. High level safe language
features in tandem with C for low-level features.

Features
--------

- modules/packages
- global vars
- global procs
- procs pointers
- closures
- lambdas
- automatically reference counted objects (like in ObjC) with composition and
    interfaces (like in Golang but interfaces are implemented explicitly)
- no inheritance
- structures (value type) with desctructors, copy constructors
- generic pointer type to interop with C (nothing can be done with pointer
    except to pass between C and Miod)
- pass by reference args
- multiple return values
- multiple source files per package, single namespace (like in Golang)
- conditional compilation like in Golang (tags-based)
- coroutines (like in Lua)
- annotations like in Java
- RTTI, introspection, reflection
- generics like in Java
- dispatch on first argument type
    e.g. proc abs(a: int) -> "abs_int", proc abs(a: float) -> "abs_float"...


Build layout
------------

A package is a set of files (in single and subdirs if necessary).
Each of the source files can refer to symbols defined in the other files
of the package.

Compilation produces header files for public symbols and C sources for
the implementation. CMake or other project description is generated as well.

Full package name for package management and code generation consists of
a *vendor* name, and a package *name*, declared with the package directive.


For future:
After a package is built, an interface file is generated for public symbols
(procs with empty bodies etc), a binary file (or generated C sources),
a list of build tags which affect the build process (all build tags listed
in source files of the package) and the set list of set tags during the
building process. If current tags do not match, the package is rebuilt.

