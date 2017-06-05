
' recursion
SUB SUM(n)
  IF n = 1 THEN
    LET SUM = 1
  ELSE
    LET SUM = n + SUM(n - 1)
  END IF
END SUB

SUB Main
    PRINT SUM(100)
END SUB
