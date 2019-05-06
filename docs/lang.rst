Language reference
==================

Goal
----

Close (preferably zero-cost) integration with C. High level safe language
features in tandem with C for low-level features.

Features
--------

- global vars
- global procs
- procs pointers
- closures
- lambdas
- automatically reference counted objects (like in ObjC) with composition and
    interfaces (like in Golang but interfaces are implemented explicitly)
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
