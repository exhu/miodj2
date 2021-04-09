INDENT = '    '
COMMA_NL = ',\n'
SEMICOLON_NL = ';\n'
NL = '\n'
EMPTY = ''

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


def if_not_empty(text, prefix):
    if len(text) > 0:
        return prefix + text
    return EMPTY
