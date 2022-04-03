package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TypeCheckerBonusTest {

    private final String path_bonus = "src/test/resources/public/input/typechecking/bonus/";
    
    TypeChecker typeChecker = new TypeChecker();
    boolean debug = true;
    // TODO @Magda Document in README
    @Test
    public void testBonusFail01() {
        // check syntax for float
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_fail01.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testBonusFail02() {
        // check syntax for char
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_fail02.jova", debug);
        assertTrue(result > 0);
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

    @Test
    public void testBonusPass01() {
        // check coercion for char
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_bonus + "bonus_pass01.jova", debug);
        assertEquals(0, result);
    }
}
