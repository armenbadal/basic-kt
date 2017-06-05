
' first test case
SUB Main
  PRINT "Hello!"
  INPUT a$

  PRINT 3.14
  INPUT b0

  LET c = b0

  IF x > y THEN
    PRINT "x > y"
  ELSEIF x < y THEN
    PRINT "x < y"
  ELSE
    PRINT "x = y"
  END IF

  WHILE n * m <> 0
    IF n > m THEN
      LET n = n \ m
    ELSE
      LET m = m \ n
    END IF
  END WHILE

  FOR i = 0 TO 10 + 3 STEP -3
    PRINT i * i
  END FOR

  PRINT a*x^2 + b*x + c

  'PRINT max(3, 8)

  'CALL Start
  'CALL Finish a$, e$
END SUB
