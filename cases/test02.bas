
SUB Main
    PRINT "# I։ Առանց պարամետրի քայլի"
    FOR i = 0 TO 20
        PRINT i
    END FOR

    PRINT "# II։ Պարամետրի դրական քայլով"
    FOR i = 0 TO 20 STEP 4
        PRINT i
    END FOR

    PRINT "# III։ Պարամետրի բացասական քայլով"
    FOR i = 20 TO 0 STEP -3
        PRINT i
    END FOR

    PRINT "# IV։ Տեքստային պարամետր"
    FOR k$ = 1 TO 5
        PRINT k$
    END FOR
END SUB
