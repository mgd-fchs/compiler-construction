package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TypeCheckerPrivateTest {

    private final String path_fail = "src/test/resources/private/typechecking/fail/";
    private final String path_pass = "src/test/resources/private/typechecking/pass/";
    private final String path_warn = "src/test/resources/private/typechecking/warn/";
    
    TypeChecker typeChecker = new TypeChecker();
    boolean debug = true;

    @Test
    public void testFailAccess01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_01.jova", debug);
        assertEquals(6, result);
    }

    // PASS: Equality operator
    @Test
    public void testPassEquals01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/eq_pass01.jova", debug);
        assertEquals(0, result);
    }

    // FAIL: Equality operator
    @Test
    public void testFailEquals01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/eq_fail01.jova", debug);
        assertEquals(3, result);
    }

    // PASS: Operators (unary, binary, ternary)
    @Test
    public void testPassOperators01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOperators02() {
        // arithmetic operations with function returns, literals, variables, and class members
        // unary and binary operations
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOperators03() {
        // logical operations with function returns, literals, variables, and class members
        // unary and binary operations
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOperators04() {
        // nested ternary operations with function returns, literals, variables, and class members
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/ternop_pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOperator05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass04.jova", debug);
        assertEquals(0, result);
    }

    // FAIL: Operators (unary, binary, ternary)
    @Test
    public void testFailOperators01() {
        // Incorrect logical operations (string, Ctype, nix), incl. binary and unary operations
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/op_fail01.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailOperators02() {
        // Incorrect arithmetic operations (string, Ctype, nix), incl. binary and unary operations
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/op_fail02.jova", debug);
        assertEquals(4, result);
    }

    @Test
    public void testFailOperators03() {
        // Incorrect unary operations only
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/op_fail03.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailOperators04() {
        // Incorrect ternary operations (only rhs/lhs compatibility -> condition is tested separately)
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/op_fail04.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testFailOperators05() {
        // Incorrect use of relational operators
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/op_fail05.jova", debug);
        assertEquals(3, result);
    }

    // WARN: Operators (unary, binary, ternary)
    @Test
    public void testWarnOperators01() {
        // Coercion arithmetic ops incl unary
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "operators/op_warn01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testWarnOperators02() {
        // Coercion logical ops incl unary
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "operators/op_warn02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testWarnOperators03() {
        // Coercion ternary op
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "operators/ternop_warn01.jova", debug);
        assertEquals(0, result);
    }

    // PASS: Conditions
    @Test
    public void testPassConditions01() {
        // if & while with nested logical expressions, no coercion
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "conditions/cond_pass01.jova", debug);
        assertEquals(0, result);
    }

    // WARN: Conditions
    @Test
    public void testWarnConditions01() {
        // if & while with nested logical expressions, incl. coercion
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "conditions/cond_warn01.jova", debug);
        assertEquals(0, result);
    }

    // FAIL: Conditions
    @Test
    public void testFailConditions01() {
        // if & while with incorrect conditions
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail01.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailConditions02() {
        // incorrect ternary conditions
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail02.jova", debug);
        assertEquals(3, result);
    }

    // PASS: Return statements
    @Test
    public void testPassReturn01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "return/pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "return/pass02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "return/pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "return/pass04.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn05() {
        // return nix
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "return/pass05.jova", debug);
        assertEquals(0, result);
    }

    // WARN: Return statements
    @Test
    public void testWarnReturn01() {
        // if & while with nested logical expressions, incl. coercion
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "return/warn01.jova", debug);
        assertEquals(0, result);
    }

    // FAIL: Return statements
    @Test
    public void testFailReturn01() {
        // return method that is class member
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "return/fail01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailReturn02() {
        // return class member
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "return/fail02.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailReturn03() {
        // return function call
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "return/fail03.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailReturn04() {
        // return function call
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "return/fail04.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailReturn05() {
        // return function call
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "return/fail05.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailReturn06() {
        // return function call
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "return/fail05.jova", debug);
        assertEquals(1, result);
    }

}
