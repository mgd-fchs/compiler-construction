package at.tugraz.ist.cc;


import at.tugraz.ist.cc.error.ErrorHandler;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author wehopeu
 *
 */
public class CodeGenerator {

    public int createCode(String file_path, String out_path) {

        LexicalAndSyntaxAnalyzer analyzer = new LexicalAndSyntaxAnalyzer();
        JovaParser parser = analyzer.createParser(analyzer.lexing(file_path, false));
        parser.reset();
        ParseTree parseTree = parser.program();

        TypeChecker typeChecker = new TypeChecker();
        typeChecker.checkTypes(file_path, false);

        if (ErrorHandler.INSTANCE.getNumParseErrors() != 0) {
            ErrorHandler.INSTANCE.printErrorsAndWarnings();
            return ErrorHandler.INSTANCE.getNumParseErrors();
        }

        CodeGeneratorVisitor codeVisitor = new CodeGeneratorVisitor();
        codeVisitor.visit(parseTree);
        // TODO: @Richard Write-function
        return 0;
    }

}
