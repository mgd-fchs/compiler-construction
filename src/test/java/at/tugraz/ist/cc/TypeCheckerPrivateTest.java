package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TypeCheckerPrivateTest {

    private final String path_fail = "src/test/resources/private/typechecking/fail/";
    private final String path_pass = "src/test/resources/private/typechecking/fail/";
    
    TypeChecker typeChecker = new TypeChecker();
    boolean debug = true;

    @Test
    public void testFailAccess01() {
        ErrorHandler.INSTANCE.reset();
        int result = typeChecker.checkTypes(path_pass + "access/access_01.jova", debug);
        assertEquals(6, result);
    }

}
