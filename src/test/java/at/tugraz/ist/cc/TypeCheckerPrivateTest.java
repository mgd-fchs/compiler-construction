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

    /** Access Tests **/
    @Test
    public void testFailAccess01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "access/access_multitest_01.jova", debug);
        assertEquals(6, result);
    }

    @Test
    public void testFailAccess02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "access/access_fail_member_01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailAccess03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "access/access_fail_member_02.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailAccess04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "access/access_fail_method_01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailAccess05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "access/access_fail_method_02.jova", debug);
        assertEquals(1, result);
    }




    @Test
    public void testPassAccess01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_pass_member_01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAccess02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_pass_member_02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAccess03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_pass_method_01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAccess04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_pass_method_02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAccess05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_pass_method_03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAccess06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_pass_method_04.jova", debug);
        assertEquals(0, result);
    }



    /** Method Invocation Tests **/
    @Test
    public void testFailMethodInvocation01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMethodInvocation02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_02.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailMethodInvocation03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_03.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailMethodInvocation04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_04.jova", debug);
        assertEquals(6, result);
    }

    @Test
    public void testFailMethodInvocation05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_05.jova", debug);
        assertEquals(4, result);
    }

    @Test
    public void testFailMethodInvocation06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_06.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMethodInvocation07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_07.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testFailMethodInvocation08() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "method_invocation/method_params_08.jova", debug);
        assertEquals(1, result);
    }





    @Test
    public void testPassMethodInvocation01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_04.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_05.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_06.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_07.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation08() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_08.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation09() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_09.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMethodInvocation10() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "method_invocation/method_params_10.jova", debug);
        assertEquals(0, result);
    }


    /** Method Invocation Tests **/
    @Test
    public void testFailThis01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/this_01_local_var.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailThis02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/this_02_param.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailThis03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/this_03_return_this.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailThis04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/this_04_wrong_class.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testThisFail05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/this_05_unknown.jova", debug);
        assertEquals(16, result);
    }

    @Test
    public void testThisFail06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/this_06_forLocal.jova", debug);
        assertEquals(4, result);
    }

    @Test
    public void testThisFail07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "this/this_07_forParam.jova", debug);
        assertEquals(8, result);
    }


    @Test
    public void testPassThis01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "this/this_01_member.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassThis02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "this/this_02_method.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassThis03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "this/this_03_method+class+member.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassThis04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "this/this_04_param.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassThis05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "this/this_05_return_class.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassThis06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "this/this_06_multiple.jova", debug);
        assertEquals(0, result);
    }

    /** Member Access Tests **/
    @Test
    public void testFailMember01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "member_access/member_01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMember02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "member_access/member_02.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMember03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "member_access/member_03.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMember04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "member_access/member_04.jova", debug);
        assertEquals(1, result);
    }



    @Test
    public void testPassMember01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMember02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMember03() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMember04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_04.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMember05() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_05.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMember06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_06.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMember07() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_07.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassMember08() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "member_access/member_08.jova", debug);
        assertEquals(0, result);
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

    @Test
    public void testPassOperator06() {
        // check equals and unequals operation for int and bool
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "operators/binop_pass05.jova", debug);
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

    @Test
    public void testFailOperators06() {
        // a lot of different possibilities of wrong type for all operators
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/op_fail06.jova", debug);

        final int relopError = 30;
        final int relopClassTypeError = 8;
        final int mulError = 9;
        final int andError = 3;
        final int orError = 3;
        final int addError = 6;
        final int notError = 3;

        final int sumError = relopError + relopClassTypeError + mulError +
                andError + orError +  addError + notError;

        assertEquals(sumError, result);
    }

    @Test
    public void testFailOperators07() {
        // Incorrect use of relational operators
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "operators/op_fail05.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailOperators08() {
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

    @Test
    public void testWarnOperators04() {
        // Coercion ternary op
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "operators/ternop_warn02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testWarnOperators05() {
        // Coercion ternary op
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "operators/ternop_warn03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testWarnOperators06() {
        // Coercion relational op
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "operators/relop_warn01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testWarnOperators07() {
        // Coercion unary op
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "operators/op_warn03.jova", debug);
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

    @Test
    public void testPassConditions02() {
        // nested if & while calls
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "conditions/cond_pass02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassConditions03() {
        // ternary operator condition
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "conditions/cond_pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassConditions04() {
        // ternary operator condition
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "conditions/cond_pass04.jova", debug);
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

    @Test
    public void testWarnConditions02() {
        // if & while with nested logical expressions, incl. coercion
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "conditions/cond_warn02.jova", debug);
        assertEquals(0, result);
    }

    // FAIL: Conditions
    @Test
    public void testFailConditions01() {
        // if & while with nix condition
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailConditions02() {
        // incorrect ternary conditions
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail02.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailConditions03() {
        // incorrect condition 'nix'
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail03.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailConditions04() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail04.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testFailConditions05() {
        // incorrect condition class type
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail05.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testFailConditions06() {
        // incorrect condition string type
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "conditions/cond_fail06.jova", debug);
        assertEquals(2, result);
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

    @Test
    public void testPassReturn06() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "return/pass05.jova", debug);
        assertEquals(0, result);
    }

    // WARN: Return statements
    @Test
    public void testWarnReturn01() {
        // coerce int to bool
        // expect 2 warnings
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "return/warn01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testWarnReturn02() {
        // coerce bool to int
        // expect 3 warnings
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "return/warn02.jova", debug);
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

    // PASS: Assignment
    @Test
    public void testPassAssign01() {
        // assign return value of nested function
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "assign/pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAssign02() {
        // assign literals, function return values, classes, and nix
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "assign/pass02.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAssign03() {
        // assign results of operations
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "assign/pass03.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testPassAssign04() {
        // assign class member values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "assign/pass04.jova", debug);
        assertEquals(0, result);
    }

    // WARN: Assignment
    @Test
    public void testWarnAssign01() {
        // coerce int to bool
        // expect 2 warnings
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "assign/warn01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testWarnAssign02() {
        // coerce bool to int
        // expect 2 warnings
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_warn + "assign/warn02.jova", debug);
        assertEquals(0, result);
    }

    // FAIL: Assignment
    @Test
    public void testFailAssign01() {
        // assign incorrect methods and class members
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "assign/fail01.jova", debug);
        assertEquals(4, result);
    }

    @Test
    public void testFailAssign02() {
        // assign incorrect class type and literals
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "assign/fail02.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testFailAssign03() {
        // assign nix
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "assign/fail03.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailAssign04() {
        // assign nix, assign incorrect class members
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "assign/fail04.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testFailAssign05() {
        // assign nix, assign incorrect class members
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "assign/fail05.jova", debug);
        assertEquals(18, result);
    }

    @Test
    public void testFailDoubleDecl01() {
        // double decl of local variables
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail01_localDoubleVar.jova", debug);
        assertEquals(5, result);
    }


    @Test
    public void testFailDoubleDecl02() {
        // double decl member variables
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail02_memberDoubleVar.jova", debug);
        assertEquals(11, result);
    }

    @Test
    public void testFailDoubleDecl03() {
        // double parameter of method
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail03_methodParamDouble.jova", debug);
        assertEquals(5, result);
    }

    @Test
    public void testFailDoubleDecl04() {
        // double parameter of ctor
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail04_ctorParamDouble.jova", debug);
        assertEquals(5, result);
    }

    @Test
    public void testFailDoubleDecl05() {
        // double local and method param
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail05_localMethodParamDouble.jova", debug);
        assertEquals(13, result);
    }

    @Test
    public void testFailDoubleDecl06() {
        // double class definition
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail06_classDouble.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailDoubleDecl07() {
        // double methods with different return values
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail07_methodDouble.jova", debug);
        assertEquals(8, result);
    }

    @Test
    public void testFailDoubleDecl08() {
        // double ctors
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail08_ctorDouble.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testFailDoubleDecl09() {
        // same signature as the global print methods
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail09_printMethodsDouble.jova", debug);
        assertEquals(5, result);
    }

    @Test
    public void testFailDoubleDecl10() {
        // local and ctor param double
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "double_decl/fail10_localCtorParamDouble.jova", debug);
        assertEquals(7, result);
    }

    @Test
    public void testFailMain01() {
        // Main class with member
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail01_withMember.jova", debug);
        assertEquals(12, result);
    }

    @Test
    public void testFailMain02() {
        // Main class with methods which are not the public int main()
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail02_withMethods.jova", debug);
        assertEquals(4, result);
    }

    @Test
    public void testFailMain03() {
        // Main class with methods which are not the public int main() and some member
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail03_withMethodsAndMembers.jova", debug);
        assertEquals(4, result);
    }

    @Test
    public void testFailMain04() {
        // Main class with ctor
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail04_withCtor.jova", debug);
        assertEquals(4, result);
    }

    @Test
    public void testFailMain05() {
        // Main class with ctor
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail05_mainAsMember.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMain06() {
        // Main class as method param
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail06_mainAsMethodParam.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMain07() {
        // Main class as ctor param
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail07_mainAsCtorParam.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMain08() {
        // Main class as member
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail08_mainAsMember.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMain09() {
        // Main class as local var
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail09_mainAsLocalVar.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testFailMain10() {
        // Main class with two public int main()
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "main_structure/fail10_mainWithTwoMainMethods.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknown01() {
        // unknown class as member
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "unknown/fail01_unknownClassAsMember.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknown02() {
        // unknown class as local
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "unknown/fail02_unknownClassAsLocal.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknown03() {
        // unknown class as method param
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "unknown/fail03_unknownClassAsMethodParam.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknown04() {
        // unknown class as ctor param
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "unknown/fail04_unknownClassAsCtorParam.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknown05() {
        // unknown class as return value
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "unknown/fail05_unknownClassAsReturnValue.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUnknown06() {
        // unknown class as return value
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "unknown/fail06_unknownClassAsCtor.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUndeclared01() {
        // undeclared local vars
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "undeclared_id/fail01_undeclaredLocalVars.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testUndeclared02() {
        // undeclared as param for method
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "undeclared_id/fail02_undeclaredParamForMethod.jova", debug);
        assertEquals(2, result);
    }

    @Test
    public void testUndeclared03() {
        // undeclared as param for ctor
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "undeclared_id/fail03_undeclaredParamForCtor.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testUndeclared04() {
        // undeclared as  return value
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "undeclared_id/fail04_undeclaredAsReturnValue.jova", debug);
        assertEquals(3, result);
    }

    @Test
    public void testConstructor01() {
        // ctor wrong name
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "constructor/fail01.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testConstructor03() {
        // unknown empty constructor which is not the default one
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "constructor/fail02.jova", debug);
        assertEquals(1, result);
    }

    @Test
    public void testCtorFail03() {
        // a lot of different wrong ctor calls
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_fail + "constructor/fail03.jova", debug);
        assertEquals((5 - 1) + (9*3 - 1), result);
    }

    @Test
    public void testCtorPass01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "constructor/pass01.jova", debug);
        assertEquals(0, result);
    }

    @Test
    public void testCtorPass02() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "constructor/pass02.jova", debug);
        assertEquals(0, result);
    }
}
