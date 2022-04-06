package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TypeCheckerPrivateTest {

    private final String path_fail = "src/test/resources/private/typechecking/fail/";
    private final String path_pass = "src/test/resources/private/typechecking/pass/";
    
    TypeChecker typeChecker = new TypeChecker();
    boolean debug = true;

    @Test
    public void testFailAccess01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_01.jova", debug);
        assertEquals(6, result);
    }



    // Relative operators
    @Test
    public void testPassEquals01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/eq_pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testFailEquals01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/eq_fail01.jova", debug);
        assertEquals(3, result);
    }

    // PASS: Operators (unary, binary, ternary)
    @Test
    public void testPassOp01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOp02() {
        // arithmetic operations with function returns, literals, variables, and class members
        // unary and binary operations
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOp03() {
        // logical operations with function returns, literals, variables, and class members
        // unary and binary operations
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOp04() {
        // nested ternary operations with function returns, literals, variables, and class members
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/ternop_pass01.jova", debug);
        assertEquals(0, result);
    }
}
