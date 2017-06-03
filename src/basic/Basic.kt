
package basic

fun main(args: Array<String>)
{
    /// println(args)
    val parser = parser.Parser("C:\\Projects\\a0\\test03.bas")
    val prog = parser.parse()

    if( prog != null ) {
        println(prog.toString())
    }

    prog!!.execute()
}
