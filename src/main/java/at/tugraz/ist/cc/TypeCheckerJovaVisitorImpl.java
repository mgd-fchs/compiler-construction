package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.stream.Collectors;

public class TypeCheckerJovaVisitorImpl extends JovaBaseVisitor<Integer>{
    private static final int OK = 0;

    private static final int TYPE_CLASS = 31;
    private static final int TYPE_PRIMITIVE = 32;
    private static final int TYPE_ERROR = -30;


    private SymbolClass currentClass;
    private final SymbolTable symbolTable;

    public TypeCheckerJovaVisitorImpl() {
        symbolTable = SymbolTable.getInstance();
        currentClass = null;
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx){
        // TODO: GENERAL remove all outputs like this, before submission
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
            currentClass.setCurrentClassName(ctx.CLASS_TYPE().toString());
            currentClass.setCurrentSymbolType(SymbolType.CLASS);

            return TYPE_CLASS;
        } else if(ctx.PRIMITIVE_TYPE() != null) {
            currentClass.setCurrentSymbolPrimitiveType(SymbolPrimitiveType.valueOf(ctx.PRIMITIVE_TYPE().toString().toUpperCase()));
            currentClass.setCurrentSymbolType(SymbolType.PRIMITIVE);

            return TYPE_PRIMITIVE;
        } else {
            return TYPE_ERROR;
        }
    }

    @Override
    public Integer visitClass_head(JovaParser.Class_headContext ctx) {
        SymbolClass newClass = new SymbolClass(ctx.CLASS_TYPE().toString());
        if(symbolTable.addClass(newClass) != 0){
            // TODO remove exit
            ErrorHandler.INSTANCE.addClassDoubleDefError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), String.valueOf(ctx.stop.getText()));
            System.out.println("duplicate class name");
            System.exit(-1);
            return -1;
        }

        currentClass = newClass;
        return visitChildren(ctx);
    }

    @Override public Integer visitClass_body(JovaParser.Class_bodyContext ctx) {
        return  visitChildren(ctx);
    }

    @Override
    public Integer visitCtor(JovaParser.CtorContext ctx) {
        return visitChildren(ctx); }

    @Override
    public Integer visitCtor_body(JovaParser.Ctor_bodyContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitMember_decl(JovaParser.Member_declContext ctx) {
        // TODO check if there are no member with the same name: consider primitives and classes
        Integer returnValue = visit(ctx.id_list());
        Integer result = visit(ctx.type());

        currentClass.buildCurrentMembers(SymbolModifier.valueOf(ctx.AMOD().toString().toUpperCase()));
        return returnValue;
    }

    @Override
    public Integer visitId_list(JovaParser.Id_listContext ctx) {
        List<TerminalNode> ids = ctx.ID();
        List<String> idsString = ids.stream().map(Object::toString).collect(Collectors.toList());
        currentClass.setCurrentIds(idsString);

        return OK;
    }

    @Override public Integer visitMethod_decl(JovaParser.Method_declContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitMethod_head(JovaParser.Method_headContext ctx) {
        visit(ctx.type());
        visit(ctx.params());

        currentClass.addMethod(SymbolModifier.valueOf(ctx.AMOD().toString().toUpperCase()), ctx.ID().toString());

        return visitChildren(ctx);
    }

    @Override
    public Integer visitParams(JovaParser.ParamsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitParam_list(JovaParser.Param_listContext ctx) {
        List<TerminalNode> ids = ctx.ID();
        List<JovaParser.TypeContext> types = ctx.type();
        List<SymbolVariable> params = new ArrayList<>();

        for (int id = 0; id < ids.size(); ++id){
            String variableName = ids.get(id).toString();
            SymbolVariable variable = null;
            if(types.get(id).PRIMITIVE_TYPE() != null) {
                SymbolPrimitiveType symbolPrimitiveType = SymbolPrimitiveType.valueOf(types.get(id).PRIMITIVE_TYPE().toString().toUpperCase());
                variable = new SymbolVariable(SymbolType.PRIMITIVE, symbolPrimitiveType, variableName);
            } else if(types.get(id).CLASS_TYPE() != null){
                // TODO check if class exits
                String className = types.get(id).CLASS_TYPE().toString();
                variable = new SymbolVariable(SymbolType.CLASS, symbolTable.getClassByName(className), variableName);
                // TODO: we do not save the name of the param => only which class it is
            } else {
                System.exit(-8);
            }
            params.add(variable);

        }

        currentClass.setCurrentParams(params);
        return OK;
    }

    @Override
    public Integer visitMethod_body(JovaParser.Method_bodyContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitStmt(JovaParser.StmtContext ctx) {
        return visitChildren(ctx); }

    @Override
    public Integer visitCompound_stmt(JovaParser.Compound_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitDeclaration(JovaParser.DeclarationContext ctx) {
        // TODO: implement shadowing -> check if this is bonus task

        Integer returnValue = visit(ctx.id_list());
        Integer result = visit(ctx.type());

        currentClass.saveLocalVariables();

        return returnValue;
    }

    @Override
    public Integer visitRet_stmt(JovaParser.Ret_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitAssign_stmt(JovaParser.Assign_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitMember_access(JovaParser.Member_accessContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        System.out.println("visitMethod_invocation");

        Collection<SymbolMethod> methods = currentClass.getMatchingMethods(ctx.ID().toString());
        currentClass.setArgList();
        if (ctx.arg_list() != null) {
            visit(ctx.arg_list());
        }

        for (SymbolMethod method : methods) {
            if (currentClass.checkValidArgList(method)) {
                currentClass.resetArgList();
                return 0;
            }
        }

        String params = "";
        if (ctx.arg_list() != null) {
            params = currentClass.getArgListTypes();
        }


        // TODO add params to error msg
        ErrorHandler.INSTANCE.addUndefMethodError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString(), params);
        currentClass.resetArgList();
        return 0;
    }

    @Override public Integer visitId_expr(JovaParser.Id_exprContext ctx) {
        if (ctx.ID() != null) {
            if (!currentClass.checkIfVariableExists(ctx.ID().toString())) {
                // TODO: add which error?
                ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString());
                return -1;
            }

            return 0;
        }

        return visitChildren(ctx);
    }

    @Override public Integer visitArg_list(JovaParser.Arg_listContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitExpr(JovaParser.ExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        return visitChildren(ctx);
    }

    @Override public Integer visitPrimary_expr(JovaParser.Primary_exprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitObject_alloc(JovaParser.Object_allocContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitCtor_args(JovaParser.Ctor_argsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitParan_expr(JovaParser.Paran_exprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitLiteral(JovaParser.LiteralContext ctx) {
        if (currentClass.currentlyGatheringArguments()) {
            SymbolPrimitiveType type = null;
            if (ctx.BOOL_LIT() != null) {
                type = SymbolPrimitiveType.BOOL;
            } else if (ctx.INT_LIT() != null) {
                type = SymbolPrimitiveType.INT;
            } else if (ctx.STRING_LIT() != null) {
                type = SymbolPrimitiveType.STRING;
            }

            currentClass.addPrimitiveArgument(type);
        }

        return visitChildren(ctx);
    }

    @Override
    public Integer visitControl_stmt(JovaParser.Control_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitIf_stmt(JovaParser.If_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        return visitChildren(ctx);
    }
}
