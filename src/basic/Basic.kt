
package basic

import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>)
{
    for( name in args ) {
        if( Files.exists(Paths.get(name)) ) {
            val parser = parser.Parser(name)
            val prog = parser.parse()

            if (prog != null) {
                println(prog.toString())
                prog.execute()
            }
        }
    }
}
