package at.tugraz.ist.cc;


import org.antlr.v4.runtime.tree.ParseTree;

/**
 * @author wehopeu
 *
 */
public class TypeChecker {

    public int checkTypes(String file_path, boolean debug) {
        LexicalAndSyntaxAnalyzer analyzer = new LexicalAndSyntaxAnalyzer();
        JovaParser parser = analyzer.createParser(analyzer.lexing(file_path, debug));

        TypeCheckerJovaImpl checker = new TypeCheckerJovaImpl();
        ParseTree parseTree = parser.program();
        checker.visit(parseTree);

        return 0;
    }

}
