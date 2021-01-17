# generates class, instance, enum, constructors definitions for header and C implementation.
from gen_common import *

template = {
    'name': "NoName",
    #'variants': {'Ok': '', 'Err': ''}, # variants key = name, value = text of C type var or None
    'variants': {'Ok': 'miod_BaseClassInstance *ok', 'Err': 'miod_BaseClassInstance *error'}, # variants key = name, value = text of C type var or None
}


def gen_variant_init_proc_hdr(cls, name, args_text):
    return f"miod_{cls} *miod_{cls}_{name.lower()}({args_text})"

def gen_variant_enum_const(name):
    return f"miod_{name}Tag_Uninitialized"

def gen_union(variants, indent_count):
    variants = list(filter(lambda x: len(x) > 0, variants))
    if len(variants) > 0:
        return indent_rest(f'''union {{
    {join_with_indent(variants, 1, SEMICOLON_NL)};
}};''', indent_count)
    return EMPTY


def gen_header(template):
    name = template['name']
    variants = template['variants']
    enum_consts = map(gen_variant_enum_const, [x for x in variants.keys()])
    variants_union = variants.values()
    constructors = map(lambda pair: gen_variant_init_proc_hdr(name, pair[0], pair[1]), variants.items())
    return f"""/// header code
extern miod_Class miod_cls_{name};

typedef enum {{
    {join_with_indent(enum_consts, 1, COMMA_NL)}
}} miod_ResultTag;

typedef struct {{
    miod_BaseClassInstance base;
    miod_{name}Tag enum_tag;{if_not_empty(gen_union(variants_union, 1), NL+INDENT)}
}} miod_Result;


// enum constructors

{join_with_indent(constructors, 0, SEMICOLON_NL)};
    """


def gen_impl(template):
    return f"""/// implementation code
    """

# ---
print(gen_header(template) + NL + gen_impl(template))