
package parser

import engine.*

class Parser constructor(filename: String) {
    val scanner = Scanner(filename)
    var lookahead = scanner.next()

    val FirstOfExpr = setOf(Token.DOUBLE, Token.STRING,
            Token.IDENTIFIER, Token.SUB, Token.NOT, Token.LEFTPAR)

    //
    fun parse()
    {
        parseProgram()
    }

    //
    private fun parseProgram() {
        while( lookahead.kind == Token.NEWLINE ) {
            match(Token.NEWLINE)
        }

        while( true ) {
            if( lookahead.kind == Token.DECLARE ) {
                parseDeclare()
            }
            else if( lookahead.kind == Token.SUBROUTINE ) {
                parseSubroutine()
            }
            else {
                break
            }
            parseNewLines()
        }
    }

    //
    private fun parseDeclare() {
        match(Token.DECLARE)
        parseSubrHeader()
    }

    //
    private fun parseSubroutine() {
        parseSubrHeader()
        parseSequence()
        match(Token.END)
        match(Token.SUBROUTINE)
    }

    //
    private fun parseNewLines() {
        match(Token.NEWLINE)
        while( lookahead.kind == Token.NEWLINE ) {
            match(Token.NEWLINE)
        }
    }

    //
    private fun parseSubrHeader() {
        match(Token.SUBROUTINE)
        match(Token.IDENTIFIER)
        if( lookahead.kind == Token.LEFTPAR ) {
            match(Token.LEFTPAR)
            if( lookahead.kind == Token.IDENTIFIER ) {
                match(Token.IDENTIFIER)
                while( lookahead.kind == Token.COMMA ) {
                    match(Token.COMMA)
                    match(Token.IDENTIFIER)
                }
            }
            match(Token.RIGHTPAR)
        }
    }

    //
    private fun parseSequence() {
        parseNewLines()
        loop@
        while( true ) {
            when( lookahead.kind ) {
                Token.LET -> parseLet()
                Token.INPUT -> parseInput()
                Token.PRINT -> parsePrint()
                Token.IF -> parseIf()
                Token.WHILE -> parseWhile()
                Token.FOR -> parseFor()
                Token.CALL -> parseCall()
                else -> break@loop
            }
            parseNewLines()
        }
    }

    //
    private fun parseLet() {
        match(Token.LET)
        match(Token.IDENTIFIER)
        match(Token.EQ)
        parseExpression()
    }

    //
    private fun parseInput()
    {
        match(Token.INPUT)
        match(Token.IDENTIFIER)
    }

    //
    private fun parsePrint()
    {
        match(Token.PRINT)
        parseExpression()
    }

    //
    private fun parseIf()
    {
        match(Token.IF)
        parseExpression()
        match(Token.THEN)
        parseSequence()
        while( lookahead.kind == Token.ELSEIF ) {
            match(Token.ELSEIF)
            parseExpression()
            match(Token.THEN)
            parseSequence()
        }
        if( lookahead.kind == Token.ELSE ) {
            match(Token.ELSE)
            parseSequence()
        }
        match(Token.END)
        match(Token.IF)
    }

    //
    private fun parseWhile()
    {
        match(Token.WHILE)
        parseExpression()
        parseSequence()
        match(Token.END)
        match(Token.WHILE)
    }

    //
    private fun parseFor()
    {
        match(Token.FOR)
        match(Token.IDENTIFIER)
        match(Token.EQ)
        parseExpression()
        match(Token.TO)
        parseExpression()
        if( lookahead.kind == Token.STEP ) {
            match(Token.STEP)
            if( lookahead.kind == Token.SUB ) {
                match(Token.SUB)
            }
            match(Token.DOUBLE)
        }
        parseSequence()
        match(Token.END)
        match(Token.FOR)
    }

    //
    private fun parseCall()
    {
        match(Token.CALL)
        match(Token.IDENTIFIER)
        if( FirstOfExpr.contains(lookahead.kind) ) {
            parseExpression()
            while( lookahead.kind == Token.COMMA ) {
                match(Token.COMMA)
                parseExpression()
            }
        }
    }

    //
    private fun parseExpression() : Expression
    {
        var e0 = parseConjunction()
        while( lookahead.kind == Token.OR ) {
            match(Token.OR)
            val e1 = parseConjunction()
            e0 = Binary(Operation.OR, e0, e1)
        }
        return e0
    }

    //
    private fun parseConjunction() : Expression
    {
        var e0 = parseEquality()
        while( lookahead.kind == Token.AND ) {
            match(Token.AND)
            val e1 = parseEquality()
            e0 = Binary(Operation.AND, e0, e1)
        }
        return e0
    }

    //
    private fun parseEquality() : Expression
    {
        var e0 = parseComparison()
        if( lookahead.kind == Token.EQ || lookahead.kind == Token.NE ) {
            val opc = when( lookahead.kind ) {
                Token.EQ -> Operation.EQ
                Token.NE -> Operation.NE
                else -> Operation.NONE
            }
            match(lookahead.kind)
            val e1 = parseComparison()
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parseComparison() : Expression
    {
        var e0 = parseAddition()
        if( lookahead.kind in Token.GT .. Token.LE ) {
            val opc = when( lookahead.kind ) {
                Token.GT -> Operation.GT
                Token.GE -> Operation.GE
                Token.LT -> Operation.LT
                Token.LE -> Operation.LE
                else -> Operation.NONE
            }
            match(lookahead.kind)
            val e1 = parseAddition()
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parseAddition() : Expression
    {
        var e0 = parseMultiplication()
        loop@
        while( true ) {
            val opc = when( lookahead.kind ) {
                Token.ADD -> Operation.ADD
                Token.SUB -> Operation.SUB
                Token.AMP -> Operation.CONC
                else -> break@loop
            }
            match(lookahead.kind)
            val e1 = parseMultiplication()
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parseMultiplication() : Expression
    {
        var e0 = parsePower()
        while( lookahead.kind in Token.MUL .. Token.MOD ) {
            val opc = when( lookahead.kind ) {
                Token.MUL -> Operation.MUL
                Token.DIV -> Operation.DIV
                Token.MOD -> Operation.MOD
                else -> Operation.NONE
            }
            val e1 = parsePower()
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parsePower() : Expression
    {
        var e0 = parseFactor()
        if( lookahead.kind == Token.POW ) {
            match(Token.POW)
            val e1 = parsePower()
            e0 = Binary(Operation.POW, e0, e1)
        }
        return e0
    }

    //
    private fun parseFactor() : Expression
    {
        if( lookahead.kind == Token.DOUBLE ) {
            val num = lookahead.value.toDouble()
            match(Token.DOUBLE)
            return DoubleValue(num)
        }

        if( lookahead.kind == Token.STRING ) {
            val str = lookahead.value
            match(Token.STRING)
            return StringValue(str)
        }

        if( lookahead.kind == Token.IDENTIFIER ) {
            val name = lookahead.value
            match(Token.IDENTIFIER)
            if( lookahead.kind == Token.LEFTPAR ) {
                val args = mutableListOf<Expression>()
                match(Token.LEFTPAR)
                if( FirstOfExpr.contains(lookahead.kind) ) {
                    var e0 = parseExpression()
                    args.add(e0)
                    while( lookahead.kind == Token.COMMA ) {
                        match(Token.COMMA)
                        e0 = parseExpression()
                        args.add(e0)
                    }
                }
                match(Token.RIGHTPAR)
                return Apply(name, args)
            }
            else {
                return Variable(name)
            }
        }

        if( lookahead.kind == Token.SUB ) {
            match(Token.SUB)
            val expr = parseFactor()
            return Unary(Operation.SUB, expr)
        }

        if( lookahead.kind == Token.NOT ) {
            match(Token.NOT)
            val expr = parseFactor()
            return Unary(Operation.NOT, expr)
        }

        if( lookahead.kind == Token.LEFTPAR ) {
            match(Token.LEFTPAR)
            val expr = parseExpression()
            match(Token.RIGHTPAR)
            return expr
        }

        throw SyntaxError("Unexpected token '$lookahead'.")
    }

    //
    private fun match(exp: Token)
    {
        if( lookahead.kind == exp ) {
            lookahead = scanner.next()
        }
        else {
            throw SyntaxError("Syntax error: at $lookahead")
        }
    }
}