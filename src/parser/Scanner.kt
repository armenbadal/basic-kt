
package parser

import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Paths

class Scanner constructor(filename: String) {
    private val keywords = mapOf(
            "SUB"    to Token.SUBROUTINE,
            "LET"    to Token.LET,
            "INPUT"  to Token.INPUT,
            "PRINT"  to Token.PRINT,
            "IF"     to Token.IF,
            "THEN"   to Token.THEN,
            "ELSEIF" to Token.ELSEIF,
            "ELSE"   to Token.ELSE,
            "WHILE"  to Token.WHILE,
            "FOR"    to Token.FOR,
            "TO"     to Token.TO,
            "STEP"   to Token.STEP,
            "CALL"   to Token.CALL,
            "END"    to Token.END,
            "AND"    to Token.AND,
            "OR"     to Token.OR,
            "NOT"    to Token.NOT
    )

    private val reader = Files.newBufferedReader(Paths.get(filename))

    private var ch: Char = '\u0000'
    private var line = 1;

    init {
        read()
    }

    fun next() : Lexeme
    {
        if( !reader.ready() )
            return Lexeme(Token.EOS, "<EOS>", line)

        whitespaces()

        if( ch == '\'' )
            return comment()

        if( ch.isLetter() )
            return keywordOrIdentifier()

        if( ch.isDigit() )
            return number()

        if( ch == '"' )
            return textLiteral()

        if( ch == '\n' ) {
            ++line
            read()
            return Lexeme(Token.NEWLINE, "<NL>", line)
        }

        if( ch == '<' || ch == '>' )
            return comparison()

        return operationOrSymbol()
    }

    private fun operationOrSymbol(): Lexeme
    {
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
        read()

        return Lexeme(kind, ch.toString(), line)
    }

    private fun comparison(): Lexeme
    {
        var res = Lexeme(Token.NONE, "<NIL>", line)

        if( ch == '<' ) {
            read()
            res = if( ch == '=' ) {
                read()
                Lexeme(Token.LE, "<=", line)
            }
            else if( ch == '>' ) {
                read()
                Lexeme(Token.NE, "<>", line)
            }
            else
                Lexeme(Token.LT, "<", line)
        }
        else if( ch == '>' ) {
            read()
            res = if( ch == '=' ) {
                read()
                Lexeme(Token.GE, ">=", line)
            }
            else
                Lexeme(Token.GT, ">", line)
        }

        return res
    }

    private fun textLiteral(): Lexeme
    {
        val sb = StringBuilder()
        read()
        while( ch != '"' ) {
            sb.append(ch)
            read()
        }
        read()
        val lex = sb.toString()
        return Lexeme(Token.TEXT, lex, line)
    }

    private fun number(): Lexeme
    {
        val sb = StringBuilder()
        while( ch.isDigit() ) {
            sb.append(ch)
            read()
        }
        if( ch == '.' ) {
            sb.append('.')
            read()
            while( ch.isDigit() ) {
                sb.append(ch)
                read()
            }
        }
        val lex = sb.toString()
        return Lexeme(Token.NUMBER, lex, line)
    }

    private fun keywordOrIdentifier(): Lexeme
    {
        val sb = StringBuilder()
        while( ch.isLetterOrDigit() ) {
            sb.append(ch)
            read()
        }
        if( ch == '$' ) {
            sb.append('$')
            read()
        }

        val lex = sb.toString()
        val tok = keywords.getOrDefault(lex, Token.IDENTIFIER)
        return Lexeme(tok, lex, line)
    }

    private fun comment(): Lexeme
    {
        while( ch != '\n' )
            read()
        return next()
    }

    private fun whitespaces()
    {
        while( ch == ' ' || ch == '\t' || ch == '\r' )
            read()
    }

    private fun read()
    {
        ch = reader.read().toChar()
    }
}
