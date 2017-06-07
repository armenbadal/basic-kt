
package engine

enum class Type {
    TEXT,
    REAL
}

data class Signature constructor(val result: Type, val inputs: List<Type>)

fun createSignature(nm: String, ps: MutableList<String>): Signature
{
    val isText = { x: String -> if( x.endsWith('$') ) Type.TEXT else Type.REAL };
    return Signature(isText(nm), ps.map(isText));
}
