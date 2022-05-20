package at.tugraz.ist.cc;

import org.junit.Test;

import java.util.Arrays;

public class CodeOptimizerPrivateTest {

    private static final String BASE_PATH_INPUT = "src/test/resources/private/code_optimization/input/";
    private static final String BASE_PATH_NORMAL_OUTPUT = "src/test/resources/private/code_optimization/output/normal/";
    private static final String BASE_PATH_NORMAL_OPTIMIZED= "src/test/resources/private/code_optimization/output/optimized/";
    private static final String[] PROGRAMS = {
            "constant_folding_01.jova",
//            "constant_folding_02.jova",
//            "constant_folding_03.jova",
//            "constant_folding_04.jova",
//            "constant_folding_05.jova",
//            "constant_propagation_01.jova",
//            "constant_propagation_02.jova",
//            "constant_propagation_03.jova",
//            "divide_by_zero_01.jova",
//            "divide_by_zero_02.jova",
//            "modulo_by_zero_01.jova",
//            "modulo_by_zero_02.jova",
//            "deadcode_elimination_01.jova",
//            "print_01.jova",
//            "no_print_or_read_empty.jova",
//            "no_print_or_read_return.jova",
//            "misc_01.jova",
//            "misc_02.jova",
//            "misc_03.jova",
    };

    private static final String[] PROGRAMS_WITH_USER_INTERACTION = {
            "divide_by_zero_user_input_03.jova",
            "divide_by_zero_user_input_04.jova",
            "modulo_by_zero_user_input_03.jova",
            "modulo_by_zero_user_input_04.jova"
    };


    private final CodeGenerator codeGenerator = new CodeGenerator();
    private final CodeOpt codeOpt = new CodeOpt();

    private int createCodeNormal(String program_name) {
        return codeGenerator.createCode(BASE_PATH_INPUT + program_name, BASE_PATH_NORMAL_OUTPUT + program_name.replace(".jova", ""));
    }

    private int createCodeOptimized(String program_name) {
        return codeOpt.optimizeCode(BASE_PATH_INPUT + program_name, false, BASE_PATH_NORMAL_OPTIMIZED + program_name.replace(".jova", ""));
    }

    private void build(String[] programms) {
        Arrays.stream(programms).forEach(program -> {
            try {
                if (createCodeNormal(program) != 0) {
                    System.out.println("\nERROR at: " + program + "\n");
                }

                if (createCodeOptimized(program) != 0) {
                    System.out.println("\nERROR at: " + program + "\n");
                }                ;
            } catch (Exception e) {
                System.out.println("\nERROR at: " + program + "\n");
                e.printStackTrace();
            }
        });
    }
    @Test
    public void buildAllAutomatedTest() {
        build(PROGRAMS);
    }

    @Test
    public void buildWithUserInput() {
        build(PROGRAMS_WITH_USER_INTERACTION);
    }

    @Test
    public void deadCodeElimination01() {
        String program_name = "deadcode_elimination_01.jova";
        createCodeNormal(program_name);
        createCodeOptimized(program_name);
    }

    @Test
    public void constantFolding04() {
        String program_name = "constant_folding_04.jova";
        createCodeNormal(program_name);
        createCodeOptimized(program_name);
    }

    @Test
    public void constantFolding01() {
        String program_name = "constant_folding_01.jova";
        createCodeNormal(program_name);
        createCodeOptimized(program_name);
    }

    @Test
    public void divide_by_zero_01() {
        String program_name = "divide_by_zero_01.jova";
        createCodeNormal(program_name);
        createCodeOptimized(program_name);
    }
    @Test
    public void divide_by_zero_02() {
        String program_name = "divide_by_zero_02.jova";
        createCodeNormal(program_name);
        createCodeOptimized(program_name);
    }

    @Test
    public void constant_propagation_03() {
        String program_name = "constant_propagation_03.jova";
        createCodeNormal(program_name);
        createCodeOptimized(program_name);
    }
}
