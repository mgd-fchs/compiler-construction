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

    CodeGenerator codeGenerator = new CodeGenerator();

    @Test
    public void testPublicPass01() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass_public + "pass01.jova", path_out + "passPublic01");
        assertEquals(0, result);
    }
    @Test
    public void testPublicPass02() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass_public + "pass02.jova", path_out + "passPublic02");
        assertEquals(0, result);
    }
    @Test
    public void testPublicPass03() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass_public + "pass03.jova", path_out + "passPublic03");
        assertEquals(0, result);
    }
    @Test
    public void testPublicPass04() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass_public + "pass04.jova", path_out + "passPublic04");
        assertEquals(0, result);
    }
    @Test
    public void testPublicPass05() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass_public + "pass05.jova", path_out + "passPublic05");
        assertEquals(0, result);
    }
    @Test
    public void testPublicPass06() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass_public + "pass06.jova", path_out + "passPublic06");
        assertEquals(0, result);
    }

    @Test
    public void testPass01() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass01.jova", path_out + "pass01");
        assertEquals(0, result);
    }

    @Test
    public void testPass02() {
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass02.jova", path_out + "pass02");
        assertEquals(0, result);
    }

    @Test
    public void testPass03() {
        // test if-statement instruction generation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass03.jova", path_out + "pass03");
        assertEquals(0, result);
    }

    @Test
    public void testPass04() {
        // test while-statement instruction generation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass04.jova", path_out + "pass04");
        assertEquals(0, result);
    }

    @Test
    public void testPass05() {
        // test ternary instruction generation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass05.jova", path_out + "pass05");
        assertEquals(0, result);
    }

    @Test
    public void testPass06() {
        // test member access
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass06.jova", path_out + "pass06");
        assertEquals(0, result);
    }

    @Test
    public void testPass07() {
        // test operation return values
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass07.jova", path_out + "pass07");
        assertEquals(0, result);
    }

    @Test
    public void testPass08() {
        // method invocation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass08.jova", path_out + "pass08");
        assertEquals(0, result);
    }

    @Test
    public void testPass09() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass09.jova", path_out + "pass09");
        assertEquals(0, result);
    }

    @Test
    public void testPass10() {
        // ternary statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass10.jova", path_out + "pass10");
        assertEquals(0, result);
    }

    @Test
    public void testPass11() {
        // ternary statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass11.jova", path_out + "pass11");
        assertEquals(0, result);
    }

    @Test
    public void testPass12() {
        // smaller equal statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass12.jova", path_out + "pass12");
        assertEquals(0, result);
    }

    @Test
    public void testPass13() {
        // greater equal statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass13.jova", path_out + "pass13");
        assertEquals(0, result);
    }

    @Test
    public void testPass14() {
        // greater smaller statements with classes+memberaccesses
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass14.jova", path_out + "pass14");
        assertEquals(0, result);
    }

    @Test
    public void testPass15() {
        // comparisons of classes
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass15.jova", path_out + "pass15");
        assertEquals(0, result);
    }

    @Test
    public void testPass16() {
        // nested ifs
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass16.jova", path_out + "pass16");
        assertEquals(0, result);
    }

    @Test
    public void testPass17() {
        // logical and/or
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass17.jova", path_out + "pass17");
        assertEquals(0, result);
    }

    @Test
    public void testPass18() {
        // shadowing/this
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass18.jova", path_out + "pass18");
        assertEquals(0, result);
    }

    @Test
    public void testPass19() {
        // multiplication
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass19.jova", path_out + "pass19");
        assertEquals(0, result);
    }

    @Test
    public void testPass20() {
        // multiplication
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass20.jova", path_out + "pass20");
        assertEquals(0, result);
    }

    @Test
    public void testPass21() {
        // multiplication
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass21.jova", path_out + "pass21");
        assertEquals(0, result);
    }

    @Test
    public void testPass22() {
        // multiplication
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass22.jova", path_out + "pass22");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp01() {
        // arithmetic operator order and correct result
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op01.jova", path_out + "pass_op01");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp02() {
        // simple arithmetic (binary and unary) operation on function calls
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op02.jova", path_out + "pass_op02");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp03() {
        // logical operator order and correct result
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op03.jova", path_out + "pass_op03");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp04() {
        // logical operator nested function/class member calls
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op04.jova", path_out + "pass_op04");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp05() {
        // equals and unequals operations (class type)
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op05.jova", path_out + "pass_op05");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp06() {
        // nested logical
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op06.jova", path_out + "pass_op06");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp07() {
        // ternary operator
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op07.jova", path_out + "pass_op07");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp08() {
        // simple test ternary operator
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op08.jova", path_out + "pass_op08");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp09() {
        // misc operator tests
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op09.jova", path_out + "pass_op09");
        assertEquals(0, result);
    }

    @Test
    public void testPassOp10() {
        // misc operator tests
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_op10.jova", path_out + "pass_op10");
        assertEquals(0, result);
    }

    @Test
    public void testPassIf01() {
        // test if- conditions
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_if01.jova", path_out + "pass_if01");
        assertEquals(0, result);
    }

    @Test
    public void testPassIf02() {
        // test if-conditions
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_if02.jova", path_out + "pass_if02");
        assertEquals(0, result);
    }

    @Test
    public void testPassWhile01() {
        // test while-loops
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_while01.jova", path_out + "pass_while01");
        assertEquals(0, result);
    }

    @Test
    public void testPassWhile02() {
        // test while-loops
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_while02.jova", path_out + "pass_while02");
        assertEquals(0, result);
    }

    @Test
    public void testPassWhile03() {
        // test while-loops
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_while03.jova", path_out + "pass_while03");
        assertEquals(0, result);
    }

    @Test
    public void testPassMethod01() {
        // simple method invocation
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_method01.jova", path_out + "pass_method01");
        assertEquals(0, result);
    }

    @Test
    public void testPassMethod02() {
        // test method invocation and return values
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_method02.jova", path_out + "pass_method02");
        assertEquals(0, result);
    }


    @Test
    public void testPassMethod03() {
        // test method invocation and return values
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_method03.jova", path_out + "pass_method03");
        assertEquals(0, result);
    }

    @Test
    public void testPassMethod04() {
        // test method invocation and return values
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_method04.jova", path_out + "pass_method04");
        assertEquals(0, result);
    }

    @Test
    public void testPassMethod05() {
        // test method invocation and return values
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_method05.jova", path_out + "pass_method05");
        assertEquals(0, result);
    }

    @Test
    public void testPassRet01() {
        // test return of nix value
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_ret01.jova", path_out + "pass_ret01");
        assertEquals(0, result);
    }

    @Test
    public void testPassRet02() {
        // test return value
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_ret02.jova", path_out + "pass_ret02");
        assertEquals(0, result);
    }

    @Test
    public void testPassMember01() {
        // simple "this" access check
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_member01.jova", path_out + "pass_member01");
        assertEquals(0, result);
    }

    @Test
    public void testPassMember02() {
        // simple "this" access check method
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "pass_member02.jova", path_out + "pass_member02");
        assertEquals(0, result);
    }

    @Test
    public void read() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "read.jova", path_out + "read");
        assertEquals(0, result);
    }

    @Test
    public void ctor() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "ctor.jova", path_out + "ctor");
        assertEquals(0, result);
    }

    @Test
    public void ternary() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "ternary.jova", path_out + "ternary");
        assertEquals(0, result);
    }

    @Test
    public void rel_classes() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "rel_classes.jova", path_out + "rel_classes");
        assertEquals(0, result);
    }

    @Test
    public void rel_primitive() {
        // return statements
        ErrorHandler.INSTANCE.reset();
        int result = codeGenerator.createCode(path_pass + "rel_primitive.jova", path_out + "rel_primitive");
        assertEquals(0, result);
    }
}
