
package engine

class Sequence : Statement {
    var items = mutableListOf<Statement>()

    // Հրամանների հաջորդականության ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // հերթով կատարել հաջորդականության ամեն մի տարրը
        items.forEach { it.execute(env) }
    }
}
