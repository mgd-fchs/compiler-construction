package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CodeGeneratorPrivateTest {

    private final String path_fail = "src/test/resources/public/input/codegeneration/fail/";
    private final String path_pass = "src/test/resources/public/input/codegeneration/pass/";
    private final String path_out = "src/test/resources/public/out/codegeneration/";
    CodeGenerator codeGenerator = new CodeGenerator();

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
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass03.jova", path_out + "pass03.j");
        assertEquals(0, result);
    }
}
