
package parser

import engine.*

class Parser constructor(filename: String) {
    val scanner = Scanner(filename)
    var lookahead = scanner.next()

    val FirstOfExpr = setOf(Token.DOUBLE, Token.STRING,
            Token.IDENTIFIER, Token.SUB, Token.NOT, Token.LEFTPAR)

    //
    private val program = Program(filename)

    //
    fun parse() : Program?
    {
        try {
            parseProgram()
        }
        catch(se: SyntaxError) {
            println(se)
            return null
        }
        return program
    }

    //
    private fun parseProgram() {
        while( lookahead.kind == Token.NEWLINE ) {
            match(Token.NEWLINE)
        }

        while( true ) {
            if( lookahead.kind == Token.DECLARE ) {
                val subr = parseDeclare()
                program.subroutines.put(subr.name, subr)
            }
            else if( lookahead.kind == Token.SUBROUTINE ) {
                val subr = parseSubroutine()
                program.subroutines.put(subr.name, subr)
            }
            else {
                break
            }
            parseNewLines()
        }
    }

    //
    private fun parseDeclare() : Subroutine
    {
        match(Token.DECLARE)
        return parseSubrHeader()
    }

    //
    private fun parseSubroutine() : Subroutine
    {
        val subr = parseSubrHeader()
        subr.body = parseSequence()
        match(Token.END)
        match(Token.SUBROUTINE)
        return subr
    }

    //
    private fun parseNewLines()
    {
        match(Token.NEWLINE)
        while( lookahead.kind == Token.NEWLINE ) {
            match(Token.NEWLINE)
        }
    }

    //
    private fun parseSubrHeader() : Subroutine
    {
        match(Token.SUBROUTINE)
        val name = lookahead.value
        match(Token.IDENTIFIER)
        val params = mutableListOf<String>()
        if( lookahead.kind == Token.LEFTPAR ) {
            match(Token.LEFTPAR)
            if( lookahead.kind == Token.IDENTIFIER ) {
                val p0 = lookahead.value
                match(Token.IDENTIFIER)
                params.add(p0)
                while( lookahead.kind == Token.COMMA ) {
                    match(Token.COMMA)
                    val p1 = lookahead.value
                    match(Token.IDENTIFIER)
                    params.add(p1)
                }
            }
            match(Token.RIGHTPAR)
        }
        return Subroutine(name, params, null)
    }

    //
    private fun parseSequence() : Statement
    {
        parseNewLines()
        val stats = Sequence()
        loop@
        while( true ) {
            val esa = when( lookahead.kind ) {
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
            stats.items.add(esa)
        }
        return stats
    }

    //
    private fun parseLet() : Statement
    {
        match(Token.LET)
        val nm = lookahead.value
        match(Token.IDENTIFIER)
        match(Token.EQ)
        val ve = parseExpression()
        return Let(nm, ve)
    }

    //
    private fun parseInput() : Statement
    {
        match(Token.INPUT)
        val nm = lookahead.value
        match(Token.IDENTIFIER)
        return Input(nm)
    }

    //
    private fun parsePrint() : Statement
    {
        match(Token.PRINT)
        val exo = parseExpression()
        return Print(exo)
    }

    //
    private fun parseIf() : Statement
    {
        match(Token.IF)
        val cono = parseExpression()
        match(Token.THEN)
        val deco = parseSequence()
        while( lookahead.kind == Token.ELSEIF ) {
            match(Token.ELSEIF)
            val coni = parseExpression()
            match(Token.THEN)
            val deci = parseSequence()
        }
        if( lookahead.kind == Token.ELSE ) {
            match(Token.ELSE)
            val alte = parseSequence()
        }
        match(Token.END)
        match(Token.IF)
        return If(cono, deco, null)
    }

    //
    private fun parseWhile() : Statement
    {
        match(Token.WHILE)
        val cond = parseExpression()
        val bdy = parseSequence()
        match(Token.END)
        match(Token.WHILE)
        return While(cond, bdy)
    }

    //
    private fun parseFor() : Statement
    {
        match(Token.FOR)
        val pr = lookahead.value
        match(Token.IDENTIFIER)
        match(Token.EQ)
        val be = parseExpression()
        match(Token.TO)
        val en = parseExpression()
        var sp = DoubleValue(1.0)
        if( lookahead.kind == Token.STEP ) {
            match(Token.STEP)
            var posi = true
            if( lookahead.kind == Token.SUB ) {
                posi = false
                match(Token.SUB)
            }
            var spvl = lookahead.value.toDouble()
            match(Token.DOUBLE)
            spvl *= if( posi ) 1 else -1
            sp = DoubleValue(spvl)
        }
        val bo = parseSequence()
        match(Token.END)
        match(Token.FOR)
        return For(pr, be, en, sp, bo)
    }

    //
    private fun parseCall() : Statement
    {
        match(Token.CALL)
        val name = lookahead.value
        match(Token.IDENTIFIER)
        val args = mutableListOf<Expression>()
        if( FirstOfExpr.contains(lookahead.kind) ) {
            var e0 = parseExpression()
            args.add(e0)
            while( lookahead.kind == Token.COMMA ) {
                match(Token.COMMA)
                e0 = parseExpression()
                args.add(e0)
            }
        }
        // TODO check existence of procedure
        return Call(name, args)
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
                // TODO check existence of subroutine
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