- cmake driven build process: 1) generate initial project (CMakeLists.txt)
with java, put default path to builtins, 2) use cmake to compile, call java
generator etc.
- [ ] WIP! java generator prototype for classes in cbackend/jgen with cmake
- [x] builtins usage example with cmake in cbackend/builtins_usage2
- [ ] Implement interfaces and RTTI in C
    - [x] interfaces
    - [x] classes
    - [x] refcounter
    - [ ] generics
    - [ ] weak references
    - [ ] optional value
    - [ ] properties
- [x] Comments to ast
- [x] Docs to ast
- [ ] Simplify lang. for only reference types, int, long, float, double, enum,
    make it an optional script language too
- [ ] Constant declaration to ast
- [ ] AstBuilder -- build ast from t1.miod
- [ ] Semantic pass
- [ ] Optimization pass (replace reference types with value types)
- [ ] C backend
- [ ] C interop (Primitive types with autoboxing)
- [ ] migrate AST-related stuff from older projects
- [x] write a simple test for grammar
