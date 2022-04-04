Frame work tested with OpenJDK11 + gradle 7.4

Group17

Max Mustermann    (1630001)   max.mustermann@student.tugraz.at
Miriam Musterfrau (1630002)   miriam.musterfrau@student.tugraz.at
John Doe          (1630003)   john.doe@student.tugraz.at


Description of the Program:
---------------------------
- ...


-------------------
Remarks for task 1:
-------------------
General remarks:
----------------
- ...

Changes  made:
--------------
- ...

Known limitations:
------------------
- ...

Implemented BONUS tasks:
------------------------
- Extend the type system to include float and char
- Supported coercion:
    char to string
    integer to float
- Supported operands:
    char: ternary operator
    float: arithmetic operations (addop, mulop, relop), ternary operator, unary (addop only)
- Test cases:
    - Syntax tests: "java/at/tugraz/ist/cc/LexicalAndSyntaxPublicTest.java"
        - testBonusFail01(): Check that incorrect syntax for float is rejected
        - testBonusFail02(): Check that incorrect syntax for char is rejected

    - Type check tests: "java/at/tugraz/ist/cc/TypeCheckerBonusTest.java"
        - testBonusPass01(): Check simple assign and return cases
        - testBonusPass02(): Define and retrieve class members of type float
        - testBonusPass02(): Define and retrieve class members of type char
        - testBonusPass03(): Check defined operators on float
        - testBonusPass04(): Check defined operators on char
        - testBonusCoercion01(): Check correct coercion warnings for float operations
        - testBonusCoercion02(): Check correct coercion warnings for char
-------------------
Remarks for task 2:
-------------------
General remarks:
----------------
...

Changes  made:
--------------
...

Known limitations:
------------------
...

Implemented BONUS tasks:
------------------------
...

Percentage of participation:
----------------------------
...


-------------------
Remarks for task 3:
-------------------
...
