Language reference
==================

Goal
----

Close (preferably zero-cost) integration with C. High level safe language
features in tandem with C for low-level features.

Features
--------

- modules/packages
- global consts
- global procs
- procs pointers
- closures
- lambdas
- automatically reference counted objects (like in ObjC) with composition and
    interfaces (like in Golang but interfaces are implemented explicitly)
- no inheritance
- everything is a reference type (some magic optimizations for basic types),
    an instance implementing basic interfaces.
- operator overloading via interfaces (like Lua's metatables)
- generic pointer type to interop with C (nothing can be done with pointer
    except to pass between C and Miod)
- no null pointers
- multiple return values
- multiple source files per package, single namespace (like in Golang)
- conditional compilation like in Golang (tags-based)
- coroutines (like in Lua)
- annotations like in Java
- RTTI, introspection, reflection
- generics like in Java, int/float types are also objects (like in Scala)
- dispatch on first argument type
    e.g. proc abs(a: int) -> "abs_int", proc abs(a: float) -> "abs_float"...


Build layout
------------

A package is a set of files.
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

Language notes
=====================

This is work in progress notes, and ideas, which will become a readable
document later.

Miod is planned to be used in tight symbiosis with C. It is a high level
counterpart.  All performance critical code is implemented in C, but overall
architecture of a program is implemented in Miod that's why the aim of the
project is to implement a simple high level programming language with least
possible memory and cpu footprint yet delivering near to zero overhead in
interoperation with C. Future easier interop with Java is also a side goal.
Miod must be a viable scripting language too.

OOP
---

The language is inspired by Java, C++ practices in the projects I worked on
(computer games).  Also some ideas from Nim, Go, Pascal, Lua, Rust, Scala.

Like Java Miod has single entity description and data reference type -- a
object/interface instance (which are almost the same). Unlike Java there's no
garbage collection yet reference counting and manual weak references are used,
so it's not that safe at all yet has predictable reproducible performance.

Most primitive types are used to interop with C and for performance reasons:
integers, floats, pointers, C structures accessed via C functions. They look
like reference types in the language, but are boxed/unboxed automatically for
performance reasons.

Lambdas/closures are supported. Coroutines support.

Objects do not support inheritance, but useful composition mechanics are
provided with automatic delegation.

Object types and weak refs can be nullable, but they can be accessed only in a
guarded block with match-like constructs like in Rust/Scala:

::

    type A = class
        var b: Optional<MyObject>
    end_object

    proc dosmth(a: A)
        match a.b.item.class
        case MyObject
            let myobj = cast<MyObject>(a.b.item)
            myobj.doSmth()
        case EmptyObject
            print("failed to retain all")
        end_match
    end

    # syntax sugar for switch on .class:
    proc dosmth(a: A)
        match_class a.b.item
        case MyObject as myobj
            myobj.doSmth()
        case EmptyObject
            print("failed to retain all")
        end_match
    end

    type Hash = interface() # aggregated interfaces in the braces
        proc hash(): i32
        property stuff get, set
    end_interface

    # implemented interfaces are listed in the braces
    type Myobject = class(Finalizable, Hash, Equals, ListenerHolder)
        // const + @_no_heap makes it preallocated as part of object
        // if ref counter of such field in finalization
        @_no_heap
        const holder: ListenerHolderImpl implements(ListenerHolder)
        # - dispatches ListenerHolder interface methods to this instance

        var f_stuff: bool

        # impl means interface property implementation
        property stuff get(f_stuff),set(set_stuff), impl
    end_object

    # 'impl' means interface implementation
    proc hash(o: Myobject): int, impl
        return 3
    end

    # called before field objects reference decremented
    proc finalize(), impl
    end


    proc set_stuff(o: Myobject, b: bool)
        o.f_stuff = b
    end


    proc do()
        # some object can be stack-allocated to optimize memory usage, if ref
        # counter is not 1 at exit from the function
        # then program aborts with the message
        # only consts to objects can be @_no_heap
        @_no_heap
        const myobj: Object
    end


Simplified overload/override/method syntax
------------------------------------------

::

    type A = object(Hashable)
            var b: Optional<Object>
    end_object

    # method implementation, compulsory for interface implementations.
    # allows access into fields for objects of type specified as the first argument.
    proc hash(myobj: MyObject): i32
        return b.isEmpty()
    end

    # procs can be called on objects both as dosmth(o) and o.dosmth()
    proc dosmth(o: MyObject): bool
       return o.b.isEmpty()
    end

    proc hash(i: i32): i32
        return i
    end

    proc hash(i: i64): i32
        return cast<i32>(i & 0xFFFFFFFF)
    end


Type declaration
----------------

All values are of reference types:
    - arithmetic(int, int64, float, double)
    - class instance (either concrete or interface)
        - enum constant instance
        - string

::

    type Arithmetic = interface()
        proc plus(other: Arithmetic): Arithmetic
        proc minus(other: Arithmetic): Arithmetic
        proc mul(other: Arithmetic): Arithmetic
        proc div(other: Arithmetic): Arithmetic
        proc mod(other: Arithmetic): Arithmetic
        # unary minus
        proc neg(): Arithmetic
    end_interface

    type Hash = interface()
        prop hash: int, get
    end

    type Numeric = interface(Hash, Arithmetic)
        proc sqrt(): Numeric
        # zero constant
        prop zero: Numeric, get
    end_interface

    # generic class
    type Point<T: Numeric> = class(Hash)
        # adds hidden fields automatically
        prop x: T, get, set

        var _y: T

        # uses field _y to store the value
        prop y: T, get(_y), set(_y)

        prop len: T, get(calc_len)
        prop hash: int, get(calc_hash), impl
    end_class

    private
    proc calc_len<T:Numeric>(p: Point<T>): T
        return sqrt(p.x*p.x + p._y*p._y)
    end

    proc calc_hash<T:Numeric>(p: Point<T>): int
        return p.x.hash + p.y.hash
    end

    type Entity = interface()
        prop id: int, get
    end_interface

    type DefEntity = class(Entity)
        var _id: int
        prop id: int, get(_id), impl
    end_class

    type Person = class(Entity)
        var _def: DefEntity, delegate(Entity)
    end_class

    type IndexedCollection<T> = interface()
        prop len: int, get
        proc at(index: int): T
    end_interface

    type IndexedMutableCollection<T> = interface(IndexedCollection)
        proc put_item_at(item: T, index: int): T
    end_interface

    type Array<T> = class(IndexedMutableCollection)
    end_class

    # enum declares a class, implements Hash, ToString, comparison operators,
    # declares global consts as instances of that class
    type Day = enum
        Working, # instance accessible as Day.Working
        Holiday
    end_enum

    # overriding/implementing operators on types
    # see Arithmetic interfaces above for declaring +,-,*,/,% operators

    # concatenation operator '..' like in Lua
    type ConCat<T> = interface()
        proc concat(other: T): ConCat
    end_interface

    type Equals<T> = interface()
        # must return false for NaN
        proc equals(other: T): bool
    end_interface

    type NotEquals<T> = interface()
        # must return false for NaN
        proc not_equals(other: T): bool
    end_interface

    type LessThan<T> = interface()
        proc less_than(other: T): bool
    end_interface

    # each closure creates a hidden class with actual proc pointer and captured data
    type Closure = interface()
    end_interface

    # each proc pointer is a hidden struct with actual pointer
    type ProcPtr = interface()
    end_interface

    # wrapped C pointer
    type Ptr = interface()
    end_interface

    type CString = Ptr

    alias int = i32
    alias long = i64
    alias float = f32
    alias double = f64

    type WeakRef<T> = interface(Finalize)
        proc lock(): Optional<T>
    end_class

    # base for all interfaces
    type Interface<T> = interface()
        proc inc_ref()
        proc dec_ref()
        proc get_weak(): WeakRef<T>
        proc unlink_weak(w: WeakRef<T>)
        proc free_ref()

        prop class: Class<T>, get
    end_interface

    type Class<T> = interface()
        proc implements(i: Interface<T>)
        prop name: string
    end_interface


Packages usage
--------------

::

    import math

    proc myproc(a: float): float
        return math::sqrt(a)
    end


    import math::sqrt

    proc myproc(a: float): float
        return a.sqrt(a)
    end


    import_all math

    proc myproc(a: float): float
        return a.sqrt(a.abs())
    end

