package at.tugraz.ist.cc;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

public class TypeCheckerJovaImpl extends JovaBaseVisitor<Integer>{
    private static final int TypeClass = 1;
    private static final int TypeMethod = 2;
    private static final int TypeMember = 3;
    private static final int TypeVariable = 4;
    private static final int TypeError = -1;

    private List<String> classes;

    public TypeCheckerJovaImpl() {
        classes = new ArrayList<>();
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx){
        System.out.println("visitProgram");
        visitChildren(ctx);

        List<JovaParser.Class_declContext> classes = ctx.class_decl();
        for(JovaParser.Class_declContext class_ : classes) {
            JovaParser.Class_headContext head_ = class_.class_head();
            TerminalNode className = head_.CLASS_TYPE();
            Optional<String> found = this.classes.stream().filter(element -> element.contains(className.getText())).findFirst();
            if(found.isPresent()){
                return -1;
            }

            this.classes.add(className.toString());
        }

        return 0;
    }

    @Override
    public Integer visitType(JovaParser.TypeContext ctx) {
        System.out.println("visitType");

        return visitChildren(ctx);
    }

    @Override
    public Integer visitClass_decl(JovaParser.Class_declContext ctx){
        System.out.println("visitClass_decl");
        return 2;
    }

    @Override
    public Integer visitLiteral(JovaParser.LiteralContext ctx){
        if (ctx.BOOL_LIT() != null){
            System.out.println("BOOL");
        }

        return 0;
    }

    @Override
    public Integer visitDeclaration(JovaParser.DeclarationContext ctx){
        JovaParser.TypeContext typeMe = ctx.type();
        List<ParseTree> childs = ctx.children;
        childs.forEach(element -> {
        });
        return 0;
    }


}
