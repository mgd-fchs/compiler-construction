package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CodeGeneratorPrivateTest {

    private final String path_fail = "src/test/resources/public/input/codegeneration/fail/";
    private final String path_pass = "src/test/resources/public/input/codegeneration/pass/";
    private final String path_pass_public = "src/test/resources/public/input/bytecode/";
    private final String path_out = "src/test/resources/public/out/codegeneration/";

    private final String path_in_tutor = "src/test/resources/public/input/bytecode/";
    CodeGenerator codeGenerator = new CodeGenerator();

    @Test
    public void testPublicPass06() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass_public + "pass06.jova", path_out + "pass06.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass01() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass01.jova", path_out + "pass01.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass02() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass02.jova", path_out + "pass02.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass03() {
        // test if-statement instruction generation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass03.jova", path_out + "pass03.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass04() {
        // test while-statement instruction generation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass04.jova", path_out + "pass04.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass05() {
        // test ternary instruction generation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass05.jova", path_out + "pass05.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass06() {
        // test member access
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass06.jova", path_out + "pass06.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass07() {
        // test operation return values
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass07.jova", path_out + "pass07.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass08() {
        // method invocation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass08.jova", path_out + "pass08.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass09() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass09.jova", path_out + "pass09.j");
        assertEquals(0, result);
    }

    @Test
    public void testPass10Tut() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_in_tutor + "pass01.jova", path_out + "pass01_tut");
        assertEquals(0, result);
    }
}
