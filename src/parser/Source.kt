
package parser

import java.nio.file.Files
import java.nio.file.Paths

class Source constructor(name: String) : Iterable<Char> {
    private val input = Files.newInputStream(Paths.get(name))
    private var charv = input.read()

    override fun iterator(): Iterator<Char>
    {
        return object : Iterator<Char> {
            override fun hasNext() : Boolean = (charv != -1)

            override fun next() : Char
            {
                val ch = charv
                charv = input.read()
                return ch.toChar()
            }
        }
    }
}
