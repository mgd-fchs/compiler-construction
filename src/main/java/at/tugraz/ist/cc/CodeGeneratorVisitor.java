package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.stream.Collectors;

public class CodeGeneratorVisitor extends JovaBaseVisitor<Integer>{
    public static final int OK = 0;
    public static final int ERROR_GENERAL = -1;

    private SymbolClass currentClass;
    private SymbolVariable currentVar;
    private final SymbolTable symbolTable;
    private boolean isReturnStatement = false;
    public SymbolVariable currentMember;

    public CodeGeneratorVisitor() {
        symbolTable = SymbolTable.getInstance();
        currentClass = null;
        currentVar = null;
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx){
        try {
            visitChildren(ctx);
        } catch (Exception e) {
        } finally {
            SymbolTable.reset();
        }
        return 0;
    }

    @Override
    public Integer visitClass_decl(JovaParser.Class_declContext ctx){
        int returnError = visitClass_head(ctx.class_head());

        if (returnError != OK) {
            return returnError;
        }

        return visitClass_body(ctx.class_body());
    }

    @Override
    public Integer visitType(JovaParser.TypeContext ctx) {
        return OK;
    }

    @Override
    public Integer visitClass_head(JovaParser.Class_headContext ctx) {
        return OK;
    }

    @Override public Integer visitClass_body(JovaParser.Class_bodyContext ctx) {

        return  OK;
    }

    @Override
    public Integer visitCtor(JovaParser.CtorContext ctx) {
        return OK;
    }

    @Override
    public Integer visitCtor_body(JovaParser.Ctor_bodyContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitMember_decl(JovaParser.Member_declContext ctx) {
        return OK;
    }

    @Override
    public Integer visitId_list(JovaParser.Id_listContext ctx) {
        return OK;
    }

    @Override public Integer visitMethod_decl(JovaParser.Method_declContext ctx) {
        return OK;
    }

    @Override
    public Integer visitMethod_head(JovaParser.Method_headContext ctx) {
        return OK;
    }

    @Override
    public Integer visitParams(JovaParser.ParamsContext ctx) {
        return OK;
    }

    @Override
    public Integer visitParam_list(JovaParser.Param_listContext ctx) {
        return OK;
    }

    @Override
    public Integer visitMethod_body(JovaParser.Method_bodyContext ctx) {

        return OK;
    }

    @Override
    public Integer visitStmt(JovaParser.StmtContext ctx) {
        return OK;
    }

    @Override
    public Integer visitCompound_stmt(JovaParser.Compound_stmtContext ctx) {
        return OK;
    }

    @Override
    public Integer visitDeclaration(JovaParser.DeclarationContext ctx) {
        return OK;
    }

    @Override
    public Integer visitRet_stmt(JovaParser.Ret_stmtContext ctx) {
        return OK;
    }

    @Override
    public Integer visitAssign_stmt(JovaParser.Assign_stmtContext ctx) {
        return OK;
    }

    @Override
    public Integer visitMember_access(JovaParser.Member_accessContext ctx) {
        return OK;

    }

    @Override
    public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        return OK;
    }

    @Override
    public Integer visitId_expr(JovaParser.Id_exprContext ctx) {
        return OK;
    }

    @Override
    public Integer visitArg_list(JovaParser.Arg_listContext ctx) {
        return OK;
    }

    @Override
    public Integer visitExpr(JovaParser.ExprContext ctx) {
        return OK;
    }

    @Override
    public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        return OK;
    }

    @Override
    public Integer visitPrimary_expr(JovaParser.Primary_exprContext ctx) {
        return OK;
    }

    @Override
    public Integer visitObject_alloc(JovaParser.Object_allocContext ctx) {
        return OK;
    }

    @Override
    public Integer visitCtor_args(JovaParser.Ctor_argsContext ctx) {
        return OK;
    }

    @Override
    public Integer visitParan_expr(JovaParser.Paran_exprContext ctx) {
        return OK;
    }

    @Override
    public Integer visitLiteral(JovaParser.LiteralContext ctx) {
        return OK;
    }

    @Override
    public Integer visitControl_stmt(JovaParser.Control_stmtContext ctx) {
        return OK;
    }

    @Override
    public Integer visitIf_stmt(JovaParser.If_stmtContext ctx) {
       return OK;
    }

    @Override
    public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        return OK;
    }

}
