
package engine

class Variable constructor(nm: String) : Expression {
    val name = nm

    // Փոփոխականի հղման ինտերպրետացիան
    override fun evaluate(env: Environment) : Value
        = env[name] ?: throw RuntimeError("Չարժեքավորված փոփոխական. '$name'։")

    //
    override fun toString() : String = name
}