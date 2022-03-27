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

    @Test
    public void testPass09() {
        // check nested assignments, previously 'pass_own_simple.jova'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "pass05.jova", debug);
        assertEquals(0, result);
    }

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
    
  /*  @Test
    public void testRetFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incompatible_return/fail01.jova", debug);
        assertTrue(result > 0);
    }*/

    @Test
    public void testRetFail02() {
        // check coerced assignment of nix
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "incompatible_return/fail02.jova", debug);
        assertTrue(result > 0);
    }

    @Test
    public void testOpFail01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail01.jova", debug);
        assertTrue(result > 0);
    }
    
    @Test
    public void testOpFail02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail+ "incorrect_operand/fail02.jova", debug);
        assertTrue(result > 0);
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
}
