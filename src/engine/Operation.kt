
package engine

enum class Operation(val text: String) {
    NONE("NONE"),
    ADD("+"),
    SUB("-"),
    CONC("&"),
    MUL("*"),
    DIV("/"),
    MOD("\\"),
    POW("^"),
    EQ("="),
    NE("<>"),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    AND("AND"),
    OR("OR"),
    NOT("NOT");

    override fun toString() = text
}