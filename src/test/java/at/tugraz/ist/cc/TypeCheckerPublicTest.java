package at.tugraz.ist.cc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;


public class TypeCheckerPublicTest {

    private final String path_coercion = "src/test/resources/public/input/typechecking/coercion_warning/";
    private final String path_fail = "src/test/resources/public/input/typechecking/fail//";
    private final String path_pass = "src/test/resources/public/input/typechecking/pass/";
    
    TypeChecker typeChecker = new TypeChecker();
    boolean debug = true;

    @Test
    public void testPass01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass02.jova", debug);
        assertEquals(0, result);
    }
        @Test
    public void testPass03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass04.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_coercion + "warning01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_coercion + "warning02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_coercion + "warning03.jova", debug);
        assertEquals(0, result);
    }
    
    @Test
    public void testPass08() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_coercion + "warning04.jova", debug);
        assertEquals(0, result);
    }

/*    @Test
    public void testPass09() {
        // TODO: This should not pass IMO, mismatched return statement
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass05.jova", debug);
        assertEquals(0, result);
    }
*/
    @Test
    public void testPass10() {
        // check assignment to 'nix'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass06.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass11() {
        // check assignments of id_lists
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass07.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass12() {
        // check compatibility ADDOP
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass18.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass13() {
        // check compatibility ADDOP
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass19.jova", debug);
        ErrorHandler.INSTANCE.printTypeWarnings();
        assertEquals(0, result);
    }

    @Test
    public void testPass20() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass20.jova", debug);
        ErrorHandler.INSTANCE.printTypeWarnings();
        assertEquals(0, result);
    }

    @Test
    public void testPass21() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass21.jova", debug);
        ErrorHandler.INSTANCE.printTypeWarnings();
        assertEquals(0, result);
    }


    @Test
    public void testAccFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "access/fail01.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testAccFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "access/fail02.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testAccFail03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "access/fail03.jova", debug);
        assertTrue(result == 6);
    }

    @Test
    public void testAccFail04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "access/fail04.jova", debug);
        assertTrue(result == 1);
    }

    @Test
    public void testAccFail05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "access/fail05.jova", debug);
        assertTrue(result == 1);
    }

    @Test
    public void testDoubleDeclFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail01.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testDoubleDeclFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail02.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testDoubleDeclFail03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail03.jova", debug);
        assertTrue(result > 0);
    }

    
    @Test
    public void testDoubleDeclFail04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail04.jova", debug);
        assertTrue(result > 0);
    }
    
    
    @Test
    public void testDoubleDeclFail05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail05.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testDoubleDeclFail06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail06.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testDoubleDeclFail07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail07.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testDoubleDeclFail08() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail08.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testDoubleDeclFail09() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail09.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testDoubleDeclFail10() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail10.jova", debug);
        assertEquals(result, 5);
    }

    @Test
    public void testDoubleDeclFail11() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail11.jova", debug);
        assertEquals(result, 1);
    }

    @Test
    public void testDoubleDeclFail12() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail12.jova", debug);
        assertEquals(result, 2);
    }

    @Test
    public void testDoubleDeclFail13() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail13.jova", debug);
        assertEquals(result, 1);
    }

    @Test
    public void testDoubleDeclFail14() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail14.jova", debug);
        assertEquals(result, 1);
    }

    @Test
    public void testDoubleDeclFail15() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail15.jova", debug);
        assertEquals(7, result);
    }

    @Test
    public void testDoubleDeclFail16() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail16.jova", debug);
        final int expectedErrorInClassTest0 = 7;
        final int expectedErrorInClassTest1 = 7;
        final int expectedErrorInClassTest2 = 0;
        assertEquals(expectedErrorInClassTest0 + expectedErrorInClassTest1 + expectedErrorInClassTest2, result);
    }

    @Test
    public void testDoubleDeclFail17() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail17.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testDoubleDeclFail18() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail18.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testDoubleDeclFail19() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "double_decl/fail19.jova", debug);
        assertEquals(14, result);
    }


    @Test
    public void testCondFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_condition/fail01.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testCondFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_condition/fail02.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testCondFail03() {
        // incompatible while condition 1
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_condition/fail03.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testCondFail04() {
        // incompatible while condition 2
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_condition/fail04.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testCondFail05() {
        // incompatible while condition 2
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_condition/fail10.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testCondFail06() {
        // incompatible while condition 2
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_condition/fail11.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testRetFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_return/fail01.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testRetFail02() {
        // check coerced assignment of nix
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail02.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testRetFail03() {
        // check coerced assignment of nix
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail03.jova", debug);
        ErrorHandler.INSTANCE.printTypeWarnings();
        assertTrue(result > 0);
    }

    @Test
    public void testRetFail04() {
        // check coerced assignment of nix
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail04.jova", debug);
        ErrorHandler.INSTANCE.printTypeWarnings();
        assertTrue(result == 1);
    }

    @Test
    public void testRetFail05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_return/fail05.jova", debug);
        assertTrue(result == 1);
    }


    @Test
    public void testOpFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail01.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testOpFail02() {
        // TODO: @Magda This should throw an operator error (currently throws assign error)
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail02.jova", debug);
        assertTrue(result == 1);
    }

    @Test
    public void testOpFail03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail03.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testOpFail04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail04.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testOpFail05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail05.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testOpFail06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail06.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testOpFail07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail07.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testOpFail08() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail08.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testMainFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail01.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testMainFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail02.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testMainFail03() {
        // additional method in main
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail03.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testMainFail04() {
        // additional method in main
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail04.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testMainFail05() {
        // additional method in main
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail05.jova", debug);
        assertEquals(result, 4);
    }

    @Test
    public void testMainFail06() {
        // additional method in main
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail06.jova", debug);
        assertEquals(result, 1);
    }

    @Test
    public void testMainFail07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail07.jova", debug);
        assertEquals(result, 1);
    }

    @Test
    public void testMainFail08() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail08.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testMainFail09() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "main_structure/fail09.jova", debug);
        assertEquals(9, result);
    }

    @Test
    public void testUndeclFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "undeclared_id/fail01.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testUndeclFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "undeclared_id/fail02.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testUndeclFail03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "undeclared_id/fail03.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testUndeclFail04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "undeclared_id/fail04.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testUndeclFail05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "undeclared_id/fail05.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testUnknwonFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "unknown_type/fail01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknwonFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "unknown_type/fail02.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknwonFail03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "unknown_type/fail03.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknwonFail04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "unknown_type/fail04.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testUnknwonFail05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "unknown_type/fail05.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testUnknwonFail06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "unknown_type/fail06.jova", debug);
        assertEquals(2, result);
    }


    @Test
    public void testPass_Simon01() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass09.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass10.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon03() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass11.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon04() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass12.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon05() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass13.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon06() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass14.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon07() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass15.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon08() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass16.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Magda01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass_Magda01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Magda02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass_Magda02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Magda03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass_Magda03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPass_Simon09() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass17.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testCtorPass01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "passCtor01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testCtorFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "constructor/fail01.jova", debug);
        assertEquals(1, result);
    }


    @Test
    public void testCtorFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "constructor/fail02.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testCtorFail03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "constructor/fail03.jova", debug);
        assertEquals((5 - 1) + (9*3 - 1), result);
    }

    @Test
    public void testAssignFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_assign/fail01.jova", debug);
        ErrorHandler.INSTANCE.printTypeWarnings();
        assertEquals(4, result);
    }

    @Test
    public void testAssignFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_assign/fail02.jova", debug);
        ErrorHandler.INSTANCE.printTypeWarnings();
        assertEquals(2, result);
    }

    @Test
    public void testPassThis01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "passThis01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "passReturn02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "passReturn03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn04() {
        ErrorHandler.INSTANCE.reset();
        // TODO this testcase leads to an exception
        int result = typeChecker.checkTypes(path_pass + "passReturn04.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "passReturn05.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassReturn06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "passReturn06.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOperator01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "passOperator01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOperator02() {
        ErrorHandler.INSTANCE.reset();
        // TODO: this leads to an exception
        int result = typeChecker.checkTypes(path_pass + "passOperator02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassOperator03() {
        ErrorHandler.INSTANCE.reset();
        // TODO: this leads to an exception
        int result = typeChecker.checkTypes(path_pass + "passOperator03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testThisFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/fail01.jova", debug);
        assertEquals(24, result);
    }

    @Test
    public void testRetFail06() {
        // Return value should be a class but is int. With function call.
        // TODO: leads to a exception
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail06.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testRetFail07() {
        // Return value should be a class but is int. With function call.
        // TODO: leads to a exception
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail07.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testRetFail08() {
        // Return value should be a class but is int. With function call.
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail08.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testRetFail09() {
        // Return value should be a string but is int. With function call and ternary operator
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail09.jova", debug);
        assertEquals(1, result);

        // TODO: Simon but the actual return is a int but this error appears: #1: Incompatible type 'bool' for return (line 37)
    }

    @Test
    public void testRetFail10() {
        // Return value should be a string but is int. With function call and ternary operator
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail10.jova", debug);
        assertEquals(1, result);

        // TODO: Simon but the actual return is a int but this error appears: #1: Incompatible type 'bool' for return (line 37)
    }


}
