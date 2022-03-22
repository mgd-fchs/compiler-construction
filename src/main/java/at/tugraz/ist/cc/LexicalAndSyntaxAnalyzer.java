package at.tugraz.ist.cc;


import at.tugraz.ist.cc.error.ErrorHandler;
import org.antlr.v4.runtime.*;

import java.io.IOException;
import java.util.List;

/**
 * @author wehopeu
 *
 */
public class LexicalAndSyntaxAnalyzer {
    // TODO add comment that this is mostly the code from the tutorium

    private class LexerErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            ErrorHandler.INSTANCE.addLexerError(line, charPositionInLine, msg);
        }
    }

    private class ParserErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            ErrorHandler.INSTANCE.addSyntaxError(line, charPositionInLine, msg);
        }
    }

    public JovaLexer lexing(String file_path, boolean debug) {
        CharStream charStream = null;
        try {
            charStream = CharStreams.fromFileName(file_path);
        } catch (IOException e) {
            if (debug) {
                System.out.println("The given file path (" + file_path + ") can not be accessed.");
                System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
            }
            System.exit(-1);
        }

        JovaLexer lexer = new JovaLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new LexerErrorListener());

        // necessary to check if there are errors
        List<? extends Token> tokens = lexer.getAllTokens();

        if (debug) {
            ErrorHandler.INSTANCE.printLexerErrors();
        }

        lexer.reset();
        return lexer;
    }

    public JovaParser createParser(JovaLexer lexer){
        JovaParser parser = new JovaParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        return parser;
    }

    public int lexer(String file_path, boolean debug) {
        JovaLexer lexer = lexing(file_path, debug);
        return ErrorHandler.INSTANCE.getNumLexErrors();
    }

    public int parser(String file_path, boolean debug) {
        JovaLexer lexer = lexing(file_path, debug);

        if (ErrorHandler.INSTANCE.getNumLexErrors() != 0) {
            return ErrorHandler.INSTANCE.getNumLexErrors();
        }

        JovaParser parser = createParser(lexer);
        parser.program();

        if (debug) {
            ErrorHandler.INSTANCE.printParserErrors();
        }

        return ErrorHandler.INSTANCE.getNumParseErrors();
    }
}
