package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.instructions.BaseInstruction;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolTable;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author wehopeu
 */
public class CodeOpt {

    public int optimizeCode(String file_path, boolean debug, String out_path) {
        SymbolTable.reset();

        LexicalAndSyntaxAnalyzer analyzer = new LexicalAndSyntaxAnalyzer();
        JovaParser parser = analyzer.createParser(analyzer.lexing(file_path, debug));
        parser.reset();
        ParseTree parseTree = parser.program();

        if (ErrorHandler.INSTANCE.getNumParseErrors() != 0) {
            ErrorHandler.INSTANCE.printErrorsAndWarnings();
            return ErrorHandler.INSTANCE.getNumParseErrors();
        }

        TypeChecker typeChecker = new TypeChecker();
        typeChecker.checkTypes(file_path, debug, false);

        if (ErrorHandler.INSTANCE.getNumTypeErrors() != 0) {
            ErrorHandler.INSTANCE.printErrorsAndWarnings();
            return ErrorHandler.INSTANCE.getNumTypeErrors();
        }

        // generate Jasmin files
        CodeGeneratorVisitor codeVisitor = new CodeGeneratorVisitor();
        codeVisitor.visit(parseTree);

        // optimize code
        optimizeInstructions();

        Collection<SymbolClass> symbolClasses = SymbolTable.getInstance().getClasses();
        symbolClasses.forEach(symbolClass -> {
            try (ClassWriter codeWriter = new ClassWriter(out_path, symbolClass)) {
                codeWriter.write();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        SymbolTable.reset();

        return 0;
    }

    public void optimizeInstructions() {
        SymbolTable symbolTable = SymbolTable.getInstance();
        symbolTable.getClasses().forEach(clazz -> clazz.getMethods().forEach(method -> {
                // optimize constants and copy propagation
                LinkedList<BaseInstruction> optimizedInstructions =
                        OptimizerUtils.constantFoldingConstantPropagationCopyPropagation(method.getInstructions());

                // optimize dead code elimination
                optimizedInstructions = OptimizerUtils.deadCodeElimination(optimizedInstructions, method);
                method.setInstructions(optimizedInstructions);


                OptimizerUtils.reorderLocalArrayVars(method);
            })
        );
    }

}
