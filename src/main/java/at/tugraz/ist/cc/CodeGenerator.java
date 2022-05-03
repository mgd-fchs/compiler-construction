package at.tugraz.ist.cc;


import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolTable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collection;

/**
 * @author wehopeu
 */
public class CodeGenerator {

    public int createCode(String file_path, String out_path) {
        SymbolTable.reset();

        LexicalAndSyntaxAnalyzer analyzer = new LexicalAndSyntaxAnalyzer();
        JovaParser parser = analyzer.createParser(analyzer.lexing(file_path, false));
        parser.reset();
        ParseTree parseTree = parser.program();

        if (ErrorHandler.INSTANCE.getNumParseErrors() != 0) {
            ErrorHandler.INSTANCE.printErrorsAndWarnings();
            return ErrorHandler.INSTANCE.getNumParseErrors();
        }

        TypeChecker typeChecker = new TypeChecker();
        typeChecker.checkTypes(file_path, false);

        if (ErrorHandler.INSTANCE.getNumTypeErrors() != 0) {
            ErrorHandler.INSTANCE.printErrorsAndWarnings();
            return ErrorHandler.INSTANCE.getNumTypeErrors();
        }

        CodeGeneratorVisitor codeVisitor = new CodeGeneratorVisitor();
        codeVisitor.visit(parseTree);

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
}
