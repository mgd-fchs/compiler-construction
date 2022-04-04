package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TypeCheckerBonusTest {

    private final String path_bonus = "src/test/resources/public/input/typechecking/bonus/";
    
    TypeChecker typeChecker = new TypeChecker();
    boolean debug = true;

    @Test
    public void testBonusPass01() {
        // check simple pass case (assignments)
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusPass02() {
        // Define and retrieve class members of type float + return values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_pass02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusPass03() {
        // Define and retrieve class members of type char + return values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusCoercionWarning01() {
        // check coercion for char
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_warn01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusCoercionWarning02() {
        // check coercion for char
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_warn02.jova", debug);
        assertEquals(0, result);
    }
}
