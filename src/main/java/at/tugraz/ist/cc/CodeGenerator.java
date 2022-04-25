package at.tugraz.ist.cc;


import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolTable;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.*;
import java.util.Collection;

/**
 * @author wehopeu
 *
 */
public class CodeGenerator {

    public int createCode(String file_path, String out_path) {
        TypeChecker typeChecker = new TypeChecker();
        typeChecker.checkTypes(file_path, false);

        LexicalAndSyntaxAnalyzer analyzer = new LexicalAndSyntaxAnalyzer();
        JovaParser parser = analyzer.createParser(analyzer.lexing(file_path, false));
        parser.reset();
        ParseTree parseTree = parser.program();

        if (ErrorHandler.INSTANCE.getNumParseErrors() != 0) {
            ErrorHandler.INSTANCE.printErrorsAndWarnings();
            return ErrorHandler.INSTANCE.getNumParseErrors();
        }

        //CodeGeneratorVisitor codeVisitor = new CodeGeneratorVisitor();
        //codeVisitor.visit(parseTree);
        createCode(out_path);
        return 0;
    }

    private void createCode(String out_path) {
        Collection<SymbolClass> symbolClasses = SymbolTable.getInstance().getClasses();
        symbolClasses.forEach(symbolClass -> createCode(out_path, symbolClass));
    }

    private void createCode(String out_path, SymbolClass symbolClass) {
        String fileName = out_path + "/" + symbolClass.getClassName() + ".j";
        File file = new File(fileName);
        file.getParentFile().mkdirs();

        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter);){

            printWriter.println(CodeAssembly.getClassHead(symbolClass.getClassName()));

            symbolClass.getMembers().forEach(member ->
                    printWriter.println(CodeAssembly.getField(member.getKey(), member.getValue())));

            printWriter.println("");

            if(symbolClass.hasNoEmptyConstructor()) {
                printWriter.println(CodeAssembly.getDefaultConstructor());
            }

            symbolClass.getConstructors().forEach(symbolConstructor ->
                    printWriter.println(CodeAssembly.getConstructor(symbolConstructor)));
/*
            symbolClass.getMethods().forEach(symbolMethod ->
                    printWriter.println(CodeAssembly.getMethod(symbolMethod)));
*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
