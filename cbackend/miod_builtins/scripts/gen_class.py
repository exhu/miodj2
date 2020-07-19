"""Generate class and interface structs"""
from typing import Sequence

from private import gen_ast, gen_text

class TypeIdDesc:
    name: str = None
    parameters: Sequence['TypeIdDesc'] = None


class PropertyDesc:
    pass

class ProcDesc:
    name: str = None
    return_type: TypeIdDesc = None


class MiodIntefaceDesc:
    name: str = None
    methods = None
    properties: Sequence[PropertyDesc] = None

class MiodClassDesc:
    name: str = None

    interfaces: Sequence[MiodIntefaceDesc] = None
    properties: Sequence[PropertyDesc] = None
    user_init_proc: str = None
    user_destroy_proc: str = None

    def _gen_interfaces(self):
        pass

    def generate_ast(self) -> gen_ast.BaseNode:
        # generate struct miod_InterfDesc and miod_BaseVtbl derived struct for methods
        pass


class MiodEnumDesc:
    pass

