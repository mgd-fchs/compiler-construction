package at.tugraz.ist.cc;

import org.junit.Test;

import java.util.Arrays;

public class CodeOptimizerPrivateTest {

    private static final String BASE_PATH_INPUT = "src/test/resources/private/code_optimization/input/";
    private static final String BASE_PATH_NORMAL_OUTPUT = "src/test/resources/private/code_optimization/output/normal/";
    private static final String BASE_PATH_NORMAL_OPTIMIZED= "src/test/resources/private/code_optimization/output/optimized/";
    private static final String[] PROGRAMS = {
            "constantFolding04.jova", "deadCodeElimination01.jova"
    };

    private final CodeGenerator codeGenerator = new CodeGenerator();
    private final CodeOpt codeOpt = new CodeOpt();

    private void createCodeNormal(String program_name) {
        codeGenerator.createCode(BASE_PATH_INPUT + program_name, BASE_PATH_NORMAL_OUTPUT + program_name.replace(".jova", ""));
    }

    private void createCodeOptimized(String program_name) {
        codeOpt.optimizeCode(BASE_PATH_INPUT + program_name, true, BASE_PATH_NORMAL_OPTIMIZED + program_name.replace(".jova", ""));
    }

    @Test
    public void buildAll() {
        Arrays.stream(PROGRAMS).forEach(program -> {
            try {
                createCodeNormal(program);
                createCodeOptimized(program);
            } catch (Exception e) {
                System.out.println("\nERROR at: " + program);
                e.printStackTrace();
                System.out.println("");
            }
        });

    }


}
