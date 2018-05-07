package engine

//
data class Signature constructor(val result: Type, val inputs: List<Type>) {
    constructor(rs: Type, vararg ins: Type) : this(rs, ins.asList())

    override fun toString(): String =
            inputs.joinToString(separator = " × ") + " → " + result
}
