
package parser

//
class Scanner constructor(filename : String) {
    private val input = Source(filename).iterator()
    private var ch : Char = input.next()
    private var line: Int = 1

    private val keywords = hashMapOf<String,Token>(
            Pair("DECLARE", Token.DECLARE),
            Pair("SUB", Token.SUBROUTINE),
            Pair("LET", Token.LET),
            Pair("INPUT", Token.INPUT),
            Pair("PRINT", Token.PRINT),
            Pair("IF", Token.IF),
            Pair("THEN", Token.THEN),
            Pair("ELSEIF", Token.ELSEIF),
            Pair("ELSE", Token.ELSE),
            Pair("WHILE", Token.WHILE),
            Pair("FOR", Token.FOR),
            Pair("TO", Token.TO),
            Pair("STEP", Token.STEP),
            Pair("CALL", Token.CALL),
            Pair("END", Token.END),
            Pair("AND", Token.AND),
            Pair("OR", Token.OR),
            Pair("NOT", Token.NOT)
    )

    //
    fun next() : Lexeme
    {
        if( !input.hasNext() ) {
            return Lexeme(Token.EOF, "EOF", line)
        }

        while( ch == ' ' || ch == '\t' || ch == '\r' ) {
            ch = input.next()
        }

        //
        if( ch == '\'' ) {
            while( ch != '\n' )
                ch = input.next()
            return next()
        }

        //
        if( ch.isLetter() ) {
            var s: String = ""
            while( ch.isLetterOrDigit() ) {
                s += ch
                ch = input.next()
            }
            if( ch == '$' ) {
                s += '$'
                ch = input.next()
            }
            val kind = keywords.getOrDefault(s, Token.IDENTIFIER)
            return Lexeme(kind, s, line)
        }

        //
        if( ch.isDigit() ) {
            var s: String = ""
            while( ch.isDigit() ) {
                s += ch
                ch = input.next()
            }
            if( ch == '.' ) {
                ch = input.next()
                while( ch.isDigit() ) {
                    s += ch
                    ch = input.next()
                }
            }
            return Lexeme(Token.DOUBLE, s, line)
        }

        //
        if( ch == '"' ) {
            ch = input.next()
            var s: String = ""
            while( ch != '"' ) {
                s += ch
                ch = input.next()
            }
            ch = input.next()
            return Lexeme(Token.STRING, s, line)
        }

        //
        if( ch == '\n' ) {
            val lex = Lexeme(Token.NEWLINE, "<-/", line)
            ch = input.next()
            ++line
            return lex
        }

        //
        if( ch == '<' ) {
            ch = input.next()
            if( ch == '=' ) {
                ch = input.next()
                return Lexeme(Token.LE, "<=", line)
            }
            else if( ch == '>' ) {
                ch = input.next()
                return Lexeme(Token.NE, "<>", line)
            }
            return Lexeme(Token.LT, "<", line)
        }

        //
        if( ch == '>' ) {
            ch = input.next()
            if( ch == '=' ) {
                ch = input.next()
                return Lexeme(Token.GE, ">=", line)
            }
            return Lexeme(Token.GT, ">", line)
        }

        //
        val kind = when( ch ) {
            '+' -> Token.ADD
            '-' -> Token.SUB
            '*' -> Token.MUL
            '/' -> Token.DIV
            '\\' -> Token.MOD
            '^' -> Token.POW
            '&' -> Token.AMP
            '=' -> Token.EQ
            '(' -> Token.LEFTPAR
            ')' -> Token.RIGHTPAR
            ',' -> Token.COMMA
            else -> Token.NONE
        }

        val lex = Lexeme(kind, ch.toString(), line)
        ch = input.next()

        return lex
    }
}