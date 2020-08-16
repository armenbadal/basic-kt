
package parser

import engine.*

class Parser constructor(filename: String) {
    private val scanner = Scanner(filename)
    private var lookahead = scanner.next()

    private val firstOfExpr = setOf(Token.NUMBER, Token.TEXT,
            Token.IDENTIFIER, Token.SUB, Token.NOT, Token.LEFTPAR)

    //
    class CallOrApply constructor(val call: Call?, val apply: Apply?) {
        fun isCall() = call != null && apply == null
        fun isApply() = !isCall()
    }
    private var subrLinks = hashMapOf<String,MutableList<CallOrApply>>()

    //
    private val program = Program(filename)

    //
    fun parse(): Program?
    {
        try {
            parseProgram()
        }
        catch(se: Exception) {
            println(se)
            return null
        }
        return program
    }

    // Program = { Subroutine NewLines }.
    private fun parseProgram()
    {
        // բաց թողնել հոսքի սկզբի դատարկ տողերը
        while( lookahead.kind == Token.NEWLINE )
            match(Token.NEWLINE)

        // վերլուծել ենթածրագրերը
        while( lookahead.kind == Token.SUBROUTINE ) {
            val subr = parseSubroutine()
            program.subroutines.put(subr.name, subr)
            parseNewLines()
        }
    }

    //
    private fun parseSubroutine(): Subroutine
    {
        // վերնագիր
        match(Token.SUBROUTINE)
        // անուն
        val name = match(Token.IDENTIFIER)
        val params = mutableListOf<String>()
        // պարամետրերի ցուցակ, որը կարող է բացակայել
        if( lookahead.kind == Token.LEFTPAR ) {
            match(Token.LEFTPAR)
            if( lookahead.kind == Token.IDENTIFIER ) {
                val p0 = match(Token.IDENTIFIER)
                params.add(p0)
                while( lookahead.kind == Token.COMMA ) {
                    match(Token.COMMA)
                    val p1 = match(Token.IDENTIFIER)
                    params.add(p1)
                }
            }
            match(Token.RIGHTPAR)
        }
        // մարմին
        val body = parseSequence()
        // ավարտ
        match(Token.END)
        match(Token.SUBROUTINE)
        // ենթածրագրի կառուցում
        val subro = Subroutine.UserDefined(name, params, body)

        // վատ հղումների կարգավորում -- վերանայել
        if( subrLinks.containsKey(name) ) {
            for(ca in subrLinks[name]!!) {
                if( ca.isCall() ) {
                    ca.call!!.callee = subro
                }
                else if( ca.isApply() ) {
                    ca.apply!!.callee = subro
                }
            }
            subrLinks.remove(name)
        }

        return subro
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
    private fun parseSequence(): Statement
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
    private fun parseInput(): Statement
    {
        match(Token.INPUT)
        val nm = match(Token.IDENTIFIER)
        return Input(nm)
    }

    //
    private fun parsePrint(): Statement
    {
        match(Token.PRINT)
        val exo = parseExpression()
        return Print(exo)
    }

    //
    private fun parseLet(): Statement
    {
        val cli = lookahead.line
        match(Token.LET)
        val nm = match(Token.IDENTIFIER)
        match(Token.EQ)
        val ve = parseExpression()

        // ստուգել վերագրման աջ ու ձախ կողմերի տիպերի համապատասխանությունը
        if( Type.TEXT == typeOf(nm) && Type.NUMBER == ve.type() ) {
            throw RuntimeError("Սխալ [$cli]։ Տեքստային փոփոխականին վերագրված է թվային արժեք։")
        }
        if( Type.NUMBER == typeOf(nm) && Type.TEXT == ve.type() ) {
            throw RuntimeError("Սխալ [$cli]։ Թվային փոփոխականին վերագրված է տեքստային արժեք։")
        }

        return Let(nm, ve)
    }

    //
    private fun parseIf(): Statement
    {
        match(Token.IF)
        val cono = parseExpression()
        match(Token.THEN)
        val deco = parseSequence()
        val resu = If(cono, deco, null)
        var bi = resu
        while( lookahead.kind == Token.ELSEIF ) {
            match(Token.ELSEIF)
            val coni = parseExpression()
            match(Token.THEN)
            val deci = parseSequence()
            val bre = If(coni, deci, null)
            bi.alternative = bre
            bi = bre
        }
        if( lookahead.kind == Token.ELSE ) {
            match(Token.ELSE)
            val alte = parseSequence()
            bi.alternative = alte
        }
        match(Token.END)
        match(Token.IF)
        return resu
    }

    //
    private fun parseWhile(): Statement
    {
        match(Token.WHILE)
        val cond = parseExpression()
        val bdy = parseSequence()
        match(Token.END)
        match(Token.WHILE)
        return While(cond, bdy)
    }

    //
    private fun parseFor(): Statement
    {
        match(Token.FOR)
        val pr = match(Token.IDENTIFIER)
        if( Type.NUMBER != typeOf(pr) ) {
            throw ParseError("FOR հրամանի պարամետրը պետք է թվային լինի։")
        }
        match(Token.EQ)
        val be = parseExpression()
        match(Token.TO)
        val en = parseExpression()
        var sp = Value.Number(1.0)
        if( lookahead.kind == Token.STEP ) {
            match(Token.STEP)
            var posi = true
            if( lookahead.kind == Token.SUB ) {
                posi = false
                match(Token.SUB)
            }
            var spvl = lookahead.value.toDouble()
            match(Token.NUMBER)
            spvl *= if( posi ) 1 else -1
            sp = Value.Number(spvl)
        }
        val bo = parseSequence()
        match(Token.END)
        match(Token.FOR)
        return For(pr, be, en, sp, bo)
    }

    // ենթածրագրի կիրառում որպես հրաման (պրոցեդուրա)
    private fun parseCall(): Statement
    {
        match(Token.CALL)
        val name = match(Token.IDENTIFIER)
        val args = mutableListOf<Expression>()
        if( firstOfExpr.contains(lookahead.kind) ) {
            var e0 = parseExpression()
            args.add(e0)
            while( lookahead.kind == Token.COMMA ) {
                match(Token.COMMA)
                e0 = parseExpression()
                args.add(e0)
            }
        }

        val sigact = Signature(typeOf(name), args.map{ it.type() })

        val subrex = program.subroutines[name]
        // սահմանված ենթածրագրի կիրառում
        if( subrex != null ) {
            val sigdcl = subrex.signature()
            if( sigact != sigdcl ) {
                throw ParseError("'$name'-ն հայտարարված է '$sigact', կիրառված է՝ '$sigdcl'։")
            }
            return Call(subrex, args)
        }

        // TODO: վերանայել
        val call = Call(Subroutine.UserDefined(
                if( Type.NUMBER == typeOf(name) ) "--dummy--" else "--dummy--$",
                args.map{ if( Type.NUMBER == it.type() ) "~" else "~$" },
                Sequence()), args)
        if( !subrLinks.containsKey(name) ) {
            subrLinks[name] = mutableListOf<CallOrApply>()
        }
        subrLinks[name]!!.add(CallOrApply(call, null))

        return call
    }

    //
    private fun parseExpression(): Expression
    {
        var e0 = parseConjunction()
        while( lookahead.kind == Token.OR ) {
            match(Token.OR)
            val e1 = parseConjunction()
            if( Type.NUMBER != e0.type() || Type.NUMBER != e1.type() ) {
                throw TypeError("'OR' գործողության արգումենտների տիպերը պետք է թվային լինեն։")
            }
            e0 = Binary(Operation.OR, e0, e1)
        }
        return e0
    }

    //
    private fun parseConjunction(): Expression
    {
        var e0 = parseEquality()
        while( lookahead.kind == Token.AND ) {
            match(Token.AND)
            val e1 = parseEquality()
            if( Type.NUMBER != e0.type() || Type.NUMBER != e1.type() ) {
                throw TypeError("'AND' գործողության արգումենտների տիպերը պետք է թվային լինեն։")
            }
            e0 = Binary(Operation.AND, e0, e1)
        }
        return e0
    }

    //
    private fun parseEquality(): Expression
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
            if( e0.type() != e1.type() ) {
                throw TypeError("'$opc' գործողության արգումենտների տիպերը տարբեր են։")
            }
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parseComparison(): Expression
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
            if( e0.type() != e1.type() ) {
                throw TypeError("'$opc' գործողության արգումենտների տիպերը տարբեր են։")
            }
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parseAddition(): Expression
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
            if( opc == Operation.CONC ) {
                if( Type.TEXT != e0.type() || Type.TEXT != e1.type() ) {
                    throw TypeError("'$opc' գործողության արգումենտները պետք է տեքստային լինեն։")
                }
            }
            else {
                if( Type.NUMBER != e0.type() || Type.NUMBER != e1.type() ) {
                    throw TypeError("'$opc' գործողության արգումենտները պետք է թվային լինեն։")
                }
            }
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parseMultiplication(): Expression
    {
        var e0 = parsePower()
        while( lookahead.kind in Token.MUL .. Token.MOD ) {
            val opc = when( lookahead.kind ) {
                Token.MUL -> Operation.MUL
                Token.DIV -> Operation.DIV
                Token.MOD -> Operation.MOD
                else -> Operation.NONE
            }
            match(lookahead.kind)
            val e1 = parsePower()
            if( Type.NUMBER != e0.type() || Type.NUMBER != e1.type() ) {
                throw TypeError("'$opc' գործողության արգումենտները պետք է թվային լինեն։")
            }
            e0 = Binary(opc, e0, e1)
        }
        return e0
    }

    //
    private fun parsePower(): Expression
    {
        var e0 = parseFactor()
        if( lookahead.kind == Token.POW ) {
            match(Token.POW)
            val e1 = parsePower()
            if( Type.NUMBER != e0.type() || Type.NUMBER != e1.type() ) {
                throw TypeError("'^' գործողության արգումենտները պետք է թվային լինեն։")
            }
            e0 = Binary(Operation.POW, e0, e1)
        }
        return e0
    }

    //
    private fun parseFactor(): Expression
    {
        if( lookahead.kind == Token.NUMBER ) {
            val num = lookahead.value.toDouble()
            match(Token.NUMBER)
            return Value.Number(num)
        }

        if( lookahead.kind == Token.TEXT ) {
            val str = match(Token.TEXT)
            return Value.Text(str)
        }

        if( lookahead.kind == Token.IDENTIFIER ) {
            val name = match(Token.IDENTIFIER)
            if( lookahead.kind == Token.LEFTPAR ) {
                val args = mutableListOf<Expression>()
                match(Token.LEFTPAR)
                if( firstOfExpr.contains(lookahead.kind) ) {
                    var e0 = parseExpression()
                    args.add(e0)
                    while( lookahead.kind == Token.COMMA ) {
                        match(Token.COMMA)
                        e0 = parseExpression()
                        args.add(e0)
                    }
                }
                match(Token.RIGHTPAR)
                if( !program.subroutines.containsKey(name) ) {
                    throw ParseError("Subroutine '$name' dot declared/defined.")
                }
                val cal = program.subroutines[name]
                return Apply(cal!!, args)
            }
            else {
                return Variable(name)
            }
        }

        if( lookahead.kind == Token.SUB || lookahead.kind == Token.NOT ) {
            val cli = lookahead.line
            val opc = when( lookahead.kind ) {
                Token.SUB -> Operation.SUB
                Token.NOT -> Operation.NOT
                else -> Operation.NONE
            }
            match(lookahead.kind)
            val expr = parseFactor()
            if( Type.NUMBER != expr.type() ) {
                throw TypeError("Սխալ [$cli]։ Ժխտման ու բացասման արգումենտը պետք է թվային լինեն։")
            }
            return Unary(opc, expr)
        }

        if( lookahead.kind == Token.LEFTPAR ) {
            match(Token.LEFTPAR)
            val expr = parseExpression()
            match(Token.RIGHTPAR)
            return expr
        }

        throw ParseError("Unexpected token '$lookahead'.")
    }

    //
    private fun match(exp: Token): String
    {
        if( lookahead.kind == exp ) {
            val lex = lookahead.value;
            lookahead = scanner.next()
            return lex
        }

        throw ParseError("Սխալ: ${lookahead.line} տողում սպասվում է $exp, բայց գրված է ${lookahead.kind}։")
    }
}