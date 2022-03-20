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

    private class LexerErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            ErrorHandler.INSTANCE.addLexerError(line, charPositionInLine, msg);
        }
    }

    public int lexer(String file_path, boolean debug) {
        // TODO: implement

        CharStream tmp = null;
        try {
            tmp = CharStreams.fromFileName(file_path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JovaLexer jl = new JovaLexer(tmp);
        jl.removeErrorListeners();
        jl.addErrorListener(new LexerErrorListener());

        List<Token> tk = (List<Token>) jl.getAllTokens();

//        ErrorHandler.INSTANCE.addLexerError();
        ErrorHandler.INSTANCE.printLexerErrors();

        return ErrorHandler.INSTANCE.getNumLexErrors();
    }

    public int parser(String file_path, boolean debug) {
        // TODO: implement
        return 0;
    }

}
