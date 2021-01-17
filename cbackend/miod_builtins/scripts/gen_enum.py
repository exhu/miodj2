# generates class, instance, enum, constructors definitions for header and C implementation.
from gen_common import *

CLASS = 'class'
INTERFACE = 'interface'
VALUE = 'value'

template = {
    'name': "NoName",
    #'variants': {'Ok': '', 'Err': ''}, # variants key = name, value = text of C type var or None
    # TODO support value, interface types
    'variants': {'Ok': ('miod_BaseClassInstance *', CLASS), 'Err': ('miod_BaseClassInstance *', CLASS)}, # variants key = name, value = text of C type var or None
}


def gen_variant_init_proc_hdr(cls, name, args_text):
    return f"miod_{cls} *miod_{cls}_{name.lower()}({args_text}{name.lower()})"

def gen_variant_enum_const(name):
    return f"miod_{name}Tag_Uninitialized"

def gen_union(variants_tuples, indent_count):
    variants = list(filter(lambda x: len(x[1]) > 0, variants_tuples))
    if len(variants) > 0:
        variants = map(lambda x: f"{x[1][0]}{x[0].lower()}", variants)
        return indent_rest(f'''union {{
    {join_with_indent(variants, 1, SEMICOLON_NL)};
}};''', indent_count)
    return EMPTY


def gen_header(template):
    name = template['name']
    variants = template['variants']
    enum_consts = map(gen_variant_enum_const, [x for x in variants.keys()])
    variants_union = variants.items()
    constructors = map(lambda pair: gen_variant_init_proc_hdr(name, pair[0], pair[1][0]), variants.items())
    return f"""/// header code
extern miod_Class miod_cls_{name};

typedef enum {{
    {join_with_indent(enum_consts, 1, COMMA_NL)}
}} miod_{name}Tag;

typedef struct {{
    miod_BaseClassInstance base;
    miod_{name}Tag enum_tag;{if_not_empty(gen_union(variants_union, 1), NL+INDENT)}
}} miod_{name};


// enum constructors

{join_with_indent(constructors, 0, SEMICOLON_NL)};
    """

def inc_ref(var_text, kind):
    if kind == CLASS:
        return f"miod_inst_inc_ref((miod_BaseClassInstance *){var_text};"
    elif kind == INTERFACE:
        return f"miod_interface_inst_inc_ref((miod_BaseInterfaceInstance *){var_text});"

    return EMPTY

def dec_ref(var_text, kind):
    if kind == CLASS:
        return f"miod_inst_dec_ref((miod_BaseClassInstance **)&{var_text};"
    elif kind == INTERFACE:
        return f"miod_interface_inst_dec_ref((miod_BaseInterfaceInstance **)&{var_text});"

    return EMPTY

# TODO generate inc/dec ref on destruct/construct


def gen_impl(template):
    name = template['name']
    return f"""/// implementation code
static void miod_{name}_destroy_proc(miod_BaseClassInstance *inst);
miod_Class miod_cls_{name} = {{
    name: "{name}",
    interfaces: NULL,
    properties: NULL,
    init_proc: NULL,
    destroy_proc: miod_{name}_destroy_proc,
    struct_size: sizeof(miod_{name}),
    instance_count: 0,
}};

static void miod_Result_destroy_proc(miod_BaseClassInstance *binst) {{
    miod_{name} *inst = (miod_{name}*)binst;
    miod_inst_dec_ref((miod_BaseClassInstance **)&inst->value);
}}
    """

# ---
print(gen_header(template) + NL + gen_impl(template))