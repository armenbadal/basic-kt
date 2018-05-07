
SUB Main
    CALL f 3.14
    CALL h$ 2, "*"
END SUB

SUB h$(e, s$)
    PRINT e
    PRINT s$
END SUB

SUB f(x)
    PRINT x
END SUB
