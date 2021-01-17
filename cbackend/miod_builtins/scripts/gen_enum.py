# generates class, instance, enum, constructors definitions for header and C implementation.
import textwrap

template = {
    'name': "NoName",
    'variants': {'Ok': 'miod_BaseClassInstance *ok', 'Err': 'miod_BaseClassInstance *error'}, # variants key = name, value = text of C type var or None
}

INDENT = '    '
COMMA_NL = ',\n'
SEMICOLON_NL = ';\n'

def join_with_indent(lines, indent_count, line_sep):
    lines = list(lines)
    lines_indented = [lines[0]]
    lines_indented.extend(map(lambda x: INDENT*indent_count + x, lines[1:]))
    return line_sep.join(lines_indented)


def indent_rest(text, indent_count):
    lines = text.split('\n')
    lines_indented = [lines[0]]
    lines_indented.extend(map(lambda x: INDENT*indent_count + x, lines[1:]))
    return '\n'.join(lines_indented)


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
    return ''


def gen_header(template):
    name = template['name']
    variants = template['variants']
    enum_consts = map(gen_variant_enum_const, [x for x in variants.keys()])
    variants_union = variants.values()
    constructors = map(lambda pair: gen_variant_init_proc_hdr(name, pair[0], pair[1]), variants.items())
    return f"""
extern miod_Class miod_cls_{name};

typedef enum {{
    {join_with_indent(enum_consts, 1, COMMA_NL)}
}} miod_ResultTag;

typedef struct {{
    miod_BaseClassInstance base;
    miod_{name}Tag enum_tag;
    {gen_union(variants_union, 1)}
}} miod_Result;


// enum constructors

{join_with_indent(constructors, 0, SEMICOLON_NL)};
    """

# ---
print(gen_header(template))