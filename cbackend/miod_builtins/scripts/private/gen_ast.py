"""Ast classes."""

# abstract node
class BaseNode:
    pass

# typedef struct _... {} name;
class StructDecl(BaseNode):
    pass


# structure variable initialization
class StructVarInit(BaseNode):
    pass