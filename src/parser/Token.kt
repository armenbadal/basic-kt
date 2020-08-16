
package parser

enum class Token {
    NONE,

    NUMBER,
    TEXT,
    IDENTIFIER,

    SUBROUTINE,
    LET,
    INPUT,
    PRINT,
    IF,
    THEN,
    ELSEIF,
    ELSE,
    WHILE,
    FOR,
    TO,
    STEP,
    CALL,
    END,

    ADD,
    SUB,
    AMP,
    MUL,
    DIV,
    MOD,
    POW,

    EQ,
    NE,
    GT,
    GE,
    LT,
    LE,

    AND,
    OR,
    NOT,

    NEWLINE,
    LEFTPAR,
    RIGHTPAR,
    COMMA,

    EOS
}