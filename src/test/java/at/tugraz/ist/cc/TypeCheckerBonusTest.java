package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TypeCheckerBonusTest {

    private final String path_fail_bonus = "src/test/resources/private/typechecking/fail/bonus/";
    private final String path_pass_bonus = "src/test/resources/private/typechecking/pass/bonus/";
    private final String path_warn_bonus = "src/test/resources/private/typechecking/warn/bonus/";

    TypeChecker typeChecker = new TypeChecker();
    boolean debug = true;

    @Test
    public void testBonusPass01() {
        // check simple pass case (assignments)
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass_bonus + "bonus_pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusPass02() {
        // Define and retrieve class members of type float + return values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass_bonus + "bonus_pass02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusPass03() {
        // Define and retrieve class members of type char + return values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass_bonus + "bonus_pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusPass04() {
        // Check correct operators on float
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass_bonus + "bonus_pass04.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusPass05() {
        // Check correct operators on char
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass_bonus + "bonus_pass05.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusCoercionWarning01() {
        // check coercion for char
        // expect 2 warnings
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn_bonus + "bonus_warn01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusCoercionWarning02() {
        // check coercion for float
        // expect 5 warnings
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn_bonus + "bonus_warn02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testBonusFail01() {
        // Define and retrieve class members of type char + return values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail_bonus + "bonus_fail01.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testBonusFail02() {
        // Define and retrieve class members of type char + return values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail_bonus + "bonus_fail02.jova", debug);
        assertEquals(6, result);
    }
}
