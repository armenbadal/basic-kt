
package engine

interface Statement {
    fun execute(env: Environment)
}