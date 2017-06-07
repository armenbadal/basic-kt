
package engine

class Variable constructor(val name: String) : Expression {
    // Փոփոխականի հղման ինտերպրետացիան
    override fun evaluate(env: Environment) : Value
        = env[name] ?: throw RuntimeError("Չարժեքավորված փոփոխական. '$name'։")

    //
    override fun toString() : String = name
}