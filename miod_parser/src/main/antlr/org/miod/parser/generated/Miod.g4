grammar Miod;

compUnit: comments? docs? unitHeader unitBody EOF;

unitHeader: PACKAGE bareName NEWLINE;

namespaceSep: DOT;
memberAccess: DOT;
bareName: ID;
qualifName: pkg=ID namespaceSep sym=ID;

comments: COMMENT+;
docs: DOC_COMMENT+;

unitBody: globalStatements?;

// IMPORT imports units, so that public symbols can be addressed as myunit.procName
// IMPORT_ALL imports unit public symbols into global namespace
importDecl: IMPORT path=STRING;

globalStmt:
    constDecl NEWLINE
    | comments
    | NEWLINE
    ;

globalStatements: globalStmt+;

boolExpr: TRUE | FALSE;

constDecl: docs? CONST name=ID (COLON type=typeSpec)? ASSIGN NEWLINE? expr;

expr: literal
    | ID;

literal: STRING | INTEGER | FLOAT;

methodName: (pkg=ID namespaceSep)? clazz=ID namespaceSep name=ID;

methodImpl: METHOD methodName argDecl NEWLINE procBody NEWLINE END_PROC;

argDecl: OPEN_PAREN CLOSE_PAREN;

typeSpec: ID;

procBody: ;


//// lexer --------------

fragment NL: ('\r'? '\n');

NEWLINE: NL;
// comments

DOC_COMMENT: '##' .*? NL;
COMMENT: '#' .*? NL;

//
WS: (' ' | '\t')+ -> skip;

//JOIN_LINE: '\\' NEWLINE -> skip;


// keywords
CONST: 'const';
VAR: 'var';
PROC: 'proc';
METHOD: 'method';
CPROC: 'cproc';
RETURN: 'return';
END_PROC: 'end';
IMPORT: 'import'; // items are accessible via fully qualified name only
TYPE: 'type';
OPAQUE: 'opaque';
STATIC_IF: 'static_if';
IF: 'if';
THEN: 'then';
ELSE: 'else';
ELIF: 'elif';
END_IF: 'end_if';
PACKAGE: 'package';
PUBLIC: 'public';
PLUS: '+';
MINUS: '-';
DIV: '/';
MUL: '*';
MOD: '%';
BNOT: '~';
BOR: '|';
BAND: '&';
NOT: 'not';
OR: 'or';
AND: 'and';
XOR: '^'; // only binary
EQUALS: '==';
NOT_EQ: '!=';
LESS: '<';
GREATER: '>';
LESS_EQ: '<=';
GREATER_EQ: '>=';
ASSIGN: '=';
SHL: 'shl';
SHR: 'shr';
NULL: 'null';
TRUE: 'true';
FALSE: 'false';
ALIAS: 'alias';
FINALLY: 'finally'; // code block run at leaving scope
END_FINALLY: 'end_finally';
STRUCT: 'struct';
END_STRUCT: 'end_struct';
ANNOTATE: '@';
OPEN_CURLY: '{';
CLOSE_CURLY: '}';
COLON: ':';
SEMICOLON: ';';
COMMA: ',';
WITH: 'with';
END_WITH: 'end_with';
OPEN_PAREN: '(';
CLOSE_PAREN: ')';
ARRAY: 'array';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';
DOT: '.';
FOR: 'for';
IN: 'in';
END_FOR: 'end_for';
WHILE: 'while';
END_WHILE: 'end_while';
BREAK: 'break';
CONTINUE: 'continue';
CAST: 'cast'; // checked only in debug
CAST_INSTANCE: 'cast_inst'; // checked cast, null if not that class
NEW: 'new';
ENUM: 'enum';
END_ENUM: 'end_enum';
WEAK: 'weak';
CLASS: 'class';
INTERFACE: 'interface';
END_INTERFACE: 'end_interface';
PROPERTY: 'property';
SETTER: 'setter';
GETTER: 'getter';
IMPLEMENTS: 'implements';
// Map$<String, Integer> -- integer map generic type
TYPE_ARGS_OPEN: '$<';
LITERAL: 'literal';
RETAIN: 'retain';
SHARED: 'shared'; // for pointers shared between threads
CLOSURE: 'closure';

// literals
fragment ESC: '\\"' | '\\\\';
fragment ESC_CHAR: '\\\'' | '\\\\';
STRING: '"' (ESC|~('\r'|'\n'))*? '"';
RAW_STRING: '"""' .*? '"""';
// only 32-127 ASCII char can be specified
CHAR_STR: '\'' (ESC_CHAR|[ -\u007F])*? '\'';

fragment HEX: [a-fA-F0-9_];
fragment DIGIT: [0-9_];
fragment OCTAL: [0-7_];
fragment BIN: [01_];
ID: [a-zA-Z_]+[0-9a-zA-Z]*;

INT_OCTAL: '-'? '0o' OCTAL+ ('_' OCTAL+)* 'U'?;
INT_HEX: '-'? '0x' HEX+ ('_' HEX+)* 'U'?;
INT_BIN: '-'? '0b' BIN+ ('_' HEX+)* 'U'?;
FLOAT: '-'? ((DIGIT+ ('_' DIGIT+)* '.' DIGIT*) | ('.' DIGIT+)) ([eE][+\-]DIGIT+)? 'f'?;
INTEGER: '-'? DIGIT+ ('_' DIGIT+)* 'U'?;


//////

