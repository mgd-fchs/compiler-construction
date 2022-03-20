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



    private class ParserErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            ErrorHandler.INSTANCE.addSyntaxError(line, charPositionInLine, msg);
        }
    }

    private JovaLexer lexing(String file_path, boolean debug)
    {
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

        if (debug)
        {
            ErrorHandler.INSTANCE.printLexerErrors();
        }

        return jl;
    }

    public int lexer(String file_path, boolean debug) {
        // TODO: implement

        lexing(file_path, debug);

        return ErrorHandler.INSTANCE.getNumLexErrors();
    }

    public int parser(String file_path, boolean debug) {
        // TODO: implement

        JovaLexer jl = lexing(file_path, debug);

        if (ErrorHandler.INSTANCE.getNumLexErrors() != 0)
        {
            return ErrorHandler.INSTANCE.getNumLexErrors();
        }

        JovaParser parser = new JovaParser(new CommonTokenStream(jl));
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        parser.program();

        if (debug)
        {
            ErrorHandler.INSTANCE.printParserErrors();
        }

        return ErrorHandler.INSTANCE.getNumParseErrors();
    }

}
