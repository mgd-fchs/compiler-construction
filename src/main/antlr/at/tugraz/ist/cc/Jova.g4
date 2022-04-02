grammar Jova;

@lexer::header {
 package at.tugraz.ist.cc;
}
@parser::header {
	package at.tugraz.ist.cc;
}

// lexer rules
KEY_CLASS : 'class';
KEY_IF : 'if';
KEY_ELSE : 'else';
KEY_WHILE : 'while';
KEY_RETURN : 'return';
KEY_NEW : 'new';
KEY_NIX : 'nix';
KEY_THIS : 'this';

AMOD : 'public' | 'private';

PRIMITIVE_TYPE : 'int' | 'string' | 'bool' | 'char' | 'float';

CLASS_TYPE : UPPERCASE (LETTER | DIGIT0 | '_')*;

ASSIGN : '=';
RELOP : '<' | '>' | '<=' | '>=' | '==' | '!=';
MULOP : '*' | '/' | '%';
AND : '&&';
OR : '||';
ADDOP : '+' | '-';
NOT : '!';
DOTOP : '.';

INT_LIT : '0' | (DIGIT)(DIGIT0)* | '-'(DIGIT)(DIGIT0)*;
BOOL_LIT : 'true' | 'false';
CHAR_LIT : '\'' LETTER '\'';
FLOAT_LIT : '-'? ('0' | (DIGIT)(DIGIT0)*) '.' ('0' | '0'*(DIGIT)(DIGIT0)*);

fragment DIGIT : '1'..'9';
fragment DIGIT0 : '0'..'9';

ID : LOWERCASE (LETTER | DIGIT0 | '_')*;

fragment LETTER : LOWERCASE | UPPERCASE;

fragment UPPERCASE : 'A'..'Z';

fragment LOWERCASE : 'a'..'z';

STRING_LIT : '"' (~[\n\r\\"] | ESQ_SEQ)* '"';

fragment ESQ_SEQ : '\\' [nrtbf"'\\];

COMMENT : '//' ~[\n\r]* -> skip;

BLOCK_COMMENT : '/*' .*? '*/' -> skip;

WS : [ \n\t\r] -> skip;

// parser rules
program : class_decl+ EOF;

type : PRIMITIVE_TYPE
     | CLASS_TYPE;

class_decl : class_head class_body;

class_head : KEY_CLASS CLASS_TYPE;

class_body : '{' member_decl*  (method_decl | ctor)* '}';

ctor : CLASS_TYPE params ctor_body ;

ctor_body : '{' declaration* stmt* '}';

member_decl : AMOD type id_list ';';

id_list : ID (',' ID)* ;

method_decl : method_head method_body;

method_head : AMOD type ID params;

params : '(' param_list? ')';

param_list : type ID (',' type ID)*;

method_body : '{' declaration* stmt* ret_stmt '}';

stmt : assign_stmt ';'
     | control_stmt
     ;

compound_stmt : '{' stmt* '}';

declaration : type id_list ';';

ret_stmt : KEY_RETURN retval=expr ';';

assign_stmt : id=id_expr ASSIGN (ass=expr | alloc=object_alloc);

member_access : DOTOP (ID | method_invocation);

method_invocation : ID '(' arg_list? ')';

id_expr : (KEY_THIS | ID | method_invocation) member_access*;

arg_list : expr (',' expr)*;

expr : prim=primary_expr
     | left=expr op=MULOP right=expr
     | left=expr op=ADDOP right=expr
     | left=expr op=RELOP right=expr
     | left=expr op=AND   right=expr
     | left=expr op=OR    right=expr
     | <assoc=right> when=expr '?' then=expr ':' el=expr
     ;

unary_expr : (NOT | ADDOP) primary_expr;

primary_expr : literal
             | id_expr
             | paran_expr
             | unary_expr
             ;

object_alloc : KEY_NEW CLASS_TYPE ctor_args?;

ctor_args : '(' arg_list? ')';

paran_expr : '(' expr ')';

literal : INT_LIT
        | BOOL_LIT
        | STRING_LIT
        | KEY_NIX
        | CHAR_LIT
        | FLOAT_LIT
        ;

control_stmt : if_stmt
             | while_stmt
             ;

if_stmt : KEY_IF '(' expr ')' compound_stmt (KEY_ELSE compound_stmt)?;

while_stmt : KEY_WHILE '(' expr ')' compound_stmt;
