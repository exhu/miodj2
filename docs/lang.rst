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
- closures (probably via anonymous classes like in java)
- automatically reference counted objects (like in ObjC) with composition and
    interfaces (like in Golang but interfaces are implemented explicitly)
- no inheritance, automatic delegation
- everything is a reference type (some magic optimizations for basic types),
    an instance implementing basic interfaces.
- operator overloading via interfaces (like Lua's metatables), or via special
    methods like in Python
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
(computer games).  Also some ideas from Nim, Go, Pascal, Lua, Rust, Scala,
Python.

Like Java Miod has single entity description and data reference type -- an
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

    class A
        var b: Optional<MyObject>
    end_class

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
        match a.b.item.class
        case myobj: MyObject
            myobj.doSmth()
        case EmptyObject
            print("failed to retain all")
        end_match
    end

    interface Hash() # aggregated interfaces in the braces
        method hash(): i32
        # read-write property:
        property stuff:bool, set
    end_interface

    # implemented interfaces are listed in the braces
    class Myobject(Finalizable, Hash, Equals, ListenerHolder)
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

    class A(Hashable)
        var b: Optional<Object>
    end_class

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

Custom operator support is not confirmed yet.
Mathematical operators are available for floats/ints, string concatenation is
available via '..' operator.

::

    interface Arithmetic
        proc plus(other: Arithmetic): Arithmetic
        proc minus(other: Arithmetic): Arithmetic
        proc mul(other: Arithmetic): Arithmetic
        proc div(other: Arithmetic): Arithmetic
        proc mod(other: Arithmetic): Arithmetic
        # unary minus
        proc neg(): Arithmetic
    end_interface

    interface Hash
        # read-only
        prop hash: int
    end_interface

    interface Numeric(Hash, Arithmetic)
        proc sqrt(): Numeric
        # zero constant
        prop zero: Numeric
    end_interface

    # generic class
    public
    class Point<T: Numeric>(Hash)
        # adds hidden fields automatically
        prop x: T, set

        var _y: T

        # uses field _y to store the value
        prop y: T, get(_y), set(_y)

        prop len: T, get(calc_len)
        prop hash: int, get(calc_hash), impl
    end_class

    proc calc_len<T:Numeric>(p: Point<T>): T
        return sqrt(p.x*p.x + p._y*p._y)
    end

    proc calc_hash<T:Numeric>(p: Point<T>): int
        return p.x.hash + p.y.hash
    end

    interface Entity
        prop id: int
    end_interface

    class DefEntity(Entity)
        var _id: int
        prop id: int, get(_id), impl
    end_class

    class Person(Entity)
        var _def: DefEntity, delegate(Entity)
    end_class

    # created by [a, b, c] syntax
    interface Array<T>(IndexedCollection)
        prop len: int, get
        proc at(index: int): T
    end_interface

    # created by [a, b, c] syntax, but requires explicit type of var as MutableArray
    interface MutableArray<T>(Array)
        proc put_item_at(item: T, index: int): T
    end_interface

    # enum declares a class, implements Hash, ToString, comparison operators,
    # declares global consts as instances of that class
    enum Day
        Working, # instance accessible as Day.Working
        Holiday
    end_enum

    # overriding/implementing operators on types
    # see Arithmetic interfaces above for declaring +,-,*,/,% operators

    # concatenation operator '..' like in Lua
    interface ConCat<T>
        proc concat(other: T): ConCat
    end_interface

    # override ==
    interface Equals<T>
        # must return false for NaN
        proc equals(other: T): bool
    end_interface

    # override !=
    interface NotEquals<T>()
        # must return false for NaN
        proc not_equals(other: T): bool
    end_interface

    interface LessThan<T>
        proc less_than(other: T): bool
    end_interface

    # TODO the same pattern for binary SHL, SHR, binary OR, AND, XOR, boolean AND, OR

    # each closure creates a hidden class with actual proc pointer and captured data
    interface Closure
    end_interface

    # each proc pointer is a hidden struct with actual pointer
    interface ProcPtr
    end_interface

    # wrapped C pointer, treated magically
    interface CPtr
    end_interface

    # C string, treated magically
    interface CString
    end_interface

    alias int = i32
    alias long = i64
    alias float = f32
    alias double = f64

    interface WeakRef<T>(Finalize)
        proc lock(): Optional<T>
    end_interface

    # base for all interfaces
    interface Interface<T>()
        proc inc_ref()
        proc dec_ref()
        proc get_weak(): WeakRef<T>
        proc unlink_weak(w: WeakRef<T>)
        proc free_ref()

        prop class: Class<T>, get
    end_interface

    enum PropertyMode
        ReadOnly,
        ReadAndWrite
    end_enum

    class Property
        prop name: String
        prop mode: PropertyMode
        prop type_id: String
    end_class

    interface Class<T>()
        proc implements(interface_id: String)
        prop interfaces: Array<String>
        prop properties: Array<Property>
        prop name: string
    end_interface


Packages usage
--------------


System types are declared in the 'builtin' package, which is imported
implicitly.

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


Modules (DISCARDED! See Compilation Units)
------------------------------------------

Modules are namespaces.

::

    package mypkg

    const a = 3

    public
    module read_consts

        public
        const b = 4

        public
        proc abc()
        end

    end_module


    -------

    import mypkg

    mypkg::read_consts::abc()

    import mypkg::read_consts

    read_consts::abc()

    import_all mypkg::read_consts

    abc()



Properties and fields
---------------------

Class fields are not accessible beyond the package they are declared in.
Properties are public, accessible everywhere. Read-only properties are set
during object creation:

::

    class MyClass()
        # read-only, declares field 'name' to store value
        prop name: String
        # declares field 'f_email' to store value, because of 'synth' modifier
        prop email: String, set(set_email), synth
    end_class

    proc set_email(c: MyClass, email: String)
        if valid_email(email) then
            f_email = email
        else
            panic("invalid email!")
        end_if
    end

    proc test1()
        var a = MyClass(name: "ttt")
        # the following will not compile:
        a.name = "bbb"
    end

    proc test2()
        # this won't trigger a panic, the field will contain invalid value
        var a = MyClass(name: "invald email")
        # but the following will stop the program:
        a.email = "invalid again"
    end


Type system
-----------

Everything is an object, has a pointer to a class.

::

    # @_root means not inherited from any interface,
    # meant for build-in bare types only!

    @_builtin
    @_root
    interface Class
        prop name: String
        proc implements(class_or_interface_name: String): bool
        # TODO reflection API
    end_interface

    @_builtin
    @_root
    interface WeakMon
        proc inc_ref()
        proc dec_ref()
        prop obj_ptr: CPtr, set
    end_interface


    # root for all interfaces/classes/enums
    @_builtin
    @_root
    interface AnyType
        prop __clazz__: Class
        proc __inc_ref__()
        proc __dec_ref__()
        # object, which stores a raw pointer to self to service weak references
        proc __get_weak_mon__(): WeakMon
    end_interface

    @_builtin
    # _native -- means implemented in C
    @_native
    class Int
    end_class


'int' and 'float' types are special, they behave like instances, but can be
optimized to be passed by value.


Literals
--------

Integer values (without casting produces 32/64-bit type to fit the size):
    decimal e.g. -123_456, 128
    hex e.g. 0xFFFF_EEEE

Floating point:
    3.34e-7

String:
    "a unicode string"

There's no "character" type.


Variables and constants
-----------------------

Only constants can be global.


C backend
---------

Interface/class instance:
::

    typedef struct {
        AnyType common;
        void(*some_method)();
    } Interface1;

    typedef struct {
        AnyType common;
        # implemented interfaces
        Interface1 i1;
        Interface2 i2;
        # only for class instance:
        AnyTypeImpl anyTypeImpl;
    } SomeInstance;

See details in "miod_builtins.h"



Interfaceless variant (DISCARDED!)
----------------------------------

No interfaces, true classes with inheritance, abstract classes, closures
are replaced with anonymous classes.

::

    abstract class BaseHandler
        abstract method on_new_data(data: Object)
        end
    end_class


    class MyHandler(BaseHandler)
        override method on_new_data(data: Object)
            data.received = true
        end
    end_class

    proc myproc()
        var i = 3
        let a = class(BaseHandler)
            var captured: Int
            override method on_new_data(data: Object)
                data.received = true
            end
        end_class { captured: i }
    end


Methods are virtual, all methods and properties are public, class fields are
accessible only from the package they are defined in.


Closure
-------

Closures are class instances in order to manage weak references for captured
vars explicitly.

::

    public
    interface MyClosure
      proc do_smth(a, b: Int): Int
    end_interface

    public
    proc call()
      var a = 1
      var b = "aaa"
      let c = class(MyClosure)
        var cap_a: Int = 0
        var cap_b: String = ""
        method do_smth(a, b: Int): Int
          return a*b + a + b.len() + this.cap_a
        end
      end_class(cap_b = b, cap_a = a)
    end


All classes must have default values set for the fields, if there are none,
the class cannot be instantiated without passing initial field values.

@_defaults attribute can be set on class declaration to make compiler check
if all fields are initialized and trigger error otherwise. This is necessary
for serializable classes. This attribute can be set on an interfaces to make
implemeting classes obey the rule.



Optional instead of null
~~~~~~~~~~~~~~~~~~~~~~~~

::

    # generic parameter is used only for clarity
    public
    interface Optional$<O>
        prop has_value: Bool, get
    end_interface

    public
    class OptionalWithValue$<O>(Optional$<O>)
        prop value: O, get
        @_nofield
        impl prop has_value: Bool, get = get_has_value
        proc get_has_value(): Bool
            return true
        end
    end_class

    public
    class OptionalEmpty$<O>(Optional$<O>, Equals)
        @_nofield
        impl prop has_value: Bool, get = get_has_value
        proc get_has_value(): Bool
            return false
        end
    end_class

    proc test(obj: Optional$<Int>)
        switch_cast(obj)

        case OptionalWithValue$<Int>
            println("value = {0}", [obj.value])
        end_case

        case OptionalEmpty$<Int>
            println("No value")
        end_case

        else
            println("Invalid type")
        end_case

        end_switch
    end


Read-only properties are marked with 'get' only. They can be initialized but
cannot be assigned later. A getter proc can ignore stored value completely.
@_nofield anotation disables generation of field value and disables property
initialization completely.

Properties are always public.


Generics
~~~~~~~~

The code is not generated, but new types are registered with generic arguments
stored in the meta data, so that you cannot cast OptionalEmpty!<Int> to
OptionalEmpty!<Long>.

All type info is generated only when the final program or DLL is built.

Compilation units
-----------------

The compiler can accept unit names aliases to allow using platform specific
implementation or using multiple unit version for a common unit, e.g.
"os_linux", "os_win" for an import unit name "os". But for normal usage a
unit annotation must be used, e.g.:

::

    @_build_tags("win32", "threads")
    @_build_tags("win64", "threads")
    import threads.win

    @_build_tags("linux")
    import process.linux

    @_build_tags("linux")
    alias process = process.linux

    @_build_tags("win")
    alias process = process.windows
    
    # make procedure accessible in unit tests
    @_build_tags("test")
    public alias my_private_proc = my_private_proc


The compiler receives a list of paths to search for units. Each unit is a single
source file. Dots in unit names define a path separator, e.g. The unit name
"mypackage.os.win32" means the source file "mypackage/os/win32.miod".
