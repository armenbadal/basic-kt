
SUB M$(a$, b$)
  LET M$ = a$ & ", " & b$
END SUB

SUB Main
    PRINT M$("aaa", "bb")
END SUB
