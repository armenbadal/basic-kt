
SUB Main
    LET a = 62
    LET b = 31

    IF a > b THEN
        PRINT "a > b"
    END IF

    IF a <= b THEN
        PRINT "a <= b"
    ELSE
        PRINT "a > b"
    END IF

    IF a <> 0 THEN
        PRINT "a <> 0"
    ELSEIF b <> 0 THEN
        PRINT "b <> 0"
    ELSEIF a * b <> 0 THEN
        PRINT "a * b <> 0"
    END IF
END SUB
