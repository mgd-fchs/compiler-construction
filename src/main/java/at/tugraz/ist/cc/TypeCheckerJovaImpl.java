package at.tugraz.ist.cc;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.stream.Collectors;

public class TypeCheckerJovaImpl extends JovaBaseVisitor<Integer>{
    private static final int TypeMethod = 2;
    private static final int TypeMember = 3;
    private static final int TypeVariable = 4;
    private static final int TypeError = -1;
    private static final int TYPE_INT = 11;
    private static final int TYPE_STRING = 12;
    private static final int TYPE_BOOL = 13;

    private static final int TYPE_MODIFIER_PUBLIC = 21;
    private static final int TYPE_MODIFIER_PRIVATE = 22;

    private static final int TYPE_CLASS = 31;
    private static final int TYPE_PRIMITIVE = 32;


    private SymbolClass currentClass;
    private SymbolTable symbolTable;

    public TypeCheckerJovaImpl() {
        symbolTable = new SymbolTable();
        currentClass = null;
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx){
        System.out.println("visitProgram");
        visitChildren(ctx);
        return 0;
    }

    @Override
    public Integer visitClass_decl(JovaParser.Class_declContext ctx){
        System.out.println("visitClass_decl");
        return visitChildren(ctx);
    }

    @Override
    public Integer visitType(JovaParser.TypeContext ctx) {
        System.out.println("visitType");
        if (ctx.CLASS_TYPE() != null){
            currentClass.setCurrentType(TYPE_CLASS);
            return TYPE_CLASS;

        } else if(ctx.PRIMITIVE_TYPE() != null) {
            currentClass.setPrimitiveType(ctx.PRIMITIVE_TYPE().toString());
            currentClass.setCurrentType(TYPE_PRIMITIVE);
            return TYPE_PRIMITIVE;
        } else {
            // TODO create different errors
            return TypeError;
        }
        // return visitChildren(ctx); // recheck if there are really not children
    }

    @Override public Integer visitClass_head(JovaParser.Class_headContext ctx) {
        SymbolClass class_ = new SymbolClass(ctx.CLASS_TYPE().toString());
        if(symbolTable.addClass(class_) != 0){
            // TODO add error message
            // TODO remove exit
            System.out.println("duplicate class name");
            System.exit(-1);
            return -1;
        }

        currentClass = class_;
        return visitChildren(ctx);
    }

    @Override public Integer visitClass_body(JovaParser.Class_bodyContext ctx) {
        Integer childErrors = visitChildren(ctx);
        return  1;
    }

    @Override public Integer visitCtor(JovaParser.CtorContext ctx) { return visitChildren(ctx); }

    @Override public Integer visitCtor_body(JovaParser.Ctor_bodyContext ctx) { return visitChildren(ctx); }

    @Override public Integer visitMember_decl(JovaParser.Member_declContext ctx) {
        // TODO check if there are no member with the same name: consider primitives and classes
        Integer returnValue = visit(ctx.id_list());
        Integer result = visit(ctx.type());

        currentClass.buildCurrentMembers(ctx.AMOD().toString());
        return returnValue;
    }

    @Override
    public Integer visitId_list(JovaParser.Id_listContext ctx) {
        int returnValue = 1;
        List<TerminalNode> ids = ctx.ID();
        List<String> idsString = ids.stream().map(element -> element.toString()).collect(Collectors.toList());
        currentClass.setCurrentIds(idsString);

        return returnValue;
    }

    @Override public Integer visitMethod_decl(JovaParser.Method_declContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitMethod_head(JovaParser.Method_headContext ctx) {
        // TODO handle errors
        visit(ctx.type());
        visit(ctx.params());
        currentClass.addMethod(ctx.AMOD().toString(), ctx.ID().toString());

        return visitChildren(ctx);
    }

    @Override public Integer visitParams(JovaParser.ParamsContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitParam_list(JovaParser.Param_listContext ctx) {
        List<TerminalNode> ids = ctx.ID();
        List<JovaParser.TypeContext> types = ctx.type();

        List<Object> params = new ArrayList<>();
        for (int id = 0; id < ids.size(); ++id){
            Type type = null;
            if(types.get(id).PRIMITIVE_TYPE() != null){
                params.add(new SymbolVariable(PrimitveType.valueOf(types.get(id).PRIMITIVE_TYPE().toString().toUpperCase()), 0, ids.get(id).toString()));
            }else if(types.get(id).CLASS_TYPE() != null){
                // TODO check if class exits
                 params.add(symbolTable.getClassByName(types.get(id).CLASS_TYPE().toString()));
            } else {
                System.exit(-8);
            }

        }

        currentClass.setCurrentParams(params);
        return 1;
    }

    @Override public Integer visitMethod_body(JovaParser.Method_bodyContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitStmt(JovaParser.StmtContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitCompound_stmt(JovaParser.Compound_stmtContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitDeclaration(JovaParser.DeclarationContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitRet_stmt(JovaParser.Ret_stmtContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitAssign_stmt(JovaParser.Assign_stmtContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitMember_access(JovaParser.Member_accessContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitId_expr(JovaParser.Id_exprContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitArg_list(JovaParser.Arg_listContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitExpr(JovaParser.ExprContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitPrimary_expr(JovaParser.Primary_exprContext ctx) {
        return visitChildren(ctx); }

    @Override public Integer visitObject_alloc(JovaParser.Object_allocContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitCtor_args(JovaParser.Ctor_argsContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitParan_expr(JovaParser.Paran_exprContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitLiteral(JovaParser.LiteralContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitControl_stmt(JovaParser.Control_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitIf_stmt(JovaParser.If_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        return visitChildren(ctx);
    }


}
