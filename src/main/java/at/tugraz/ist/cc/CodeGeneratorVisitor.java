package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.instructions.*;
import at.tugraz.ist.cc.symbol_table.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CodeGeneratorVisitor extends JovaBaseVisitor<Integer>{
    public static final int OK = 0;
    public static final int ERROR_GENERAL = -1;

    private SymbolClass currentClass;
    private final SymbolTable symbolTable;
    private Integer currentConstructorIndex;
    private Integer currentLabelIndex;

    public CodeGeneratorVisitor() {
        symbolTable = SymbolTable.getInstance();
        currentClass = null;
        currentConstructorIndex = 0;
        currentLabelIndex = 0;
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx){
        try {
            visitChildren(ctx);
        } catch (Exception e) {
            // TODO remove:
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Integer visitClass_decl(JovaParser.Class_declContext ctx){
        visitClass_head(ctx.class_head());
        visitClass_body(ctx.class_body());
        return OK;
    }

    @Override
    public Integer visitType(JovaParser.TypeContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitClass_head(JovaParser.Class_headContext ctx) {
        currentClass = symbolTable.getClassByName(ctx.CLASS_TYPE().toString(), ctx).get();
        return OK;
    }

    @Override public Integer visitClass_body(JovaParser.Class_bodyContext ctx) {
        visitChildren(ctx);
        return  OK;
    }

    @Override
    public Integer visitCtor(JovaParser.CtorContext ctx) {
        currentClass.setCurrentCallable(currentClass.getConstructors().get(currentConstructorIndex));
        visitCtor_body(ctx.ctor_body());
        currentConstructorIndex += 1;
        return OK;
    }

    @Override
    public Integer visitCtor_body(JovaParser.Ctor_bodyContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitMember_decl(JovaParser.Member_declContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitId_list(JovaParser.Id_listContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override public Integer visitMethod_decl(JovaParser.Method_declContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitMethod_head(JovaParser.Method_headContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitParams(JovaParser.ParamsContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitParam_list(JovaParser.Param_listContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitMethod_body(JovaParser.Method_bodyContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitStmt(JovaParser.StmtContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitCompound_stmt(JovaParser.Compound_stmtContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitDeclaration(JovaParser.DeclarationContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitRet_stmt(JovaParser.Ret_stmtContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitAssign_stmt(JovaParser.Assign_stmtContext ctx) {
        SymbolVariable previousSymbolVariable = currentClass.getCurrentScopeVariable(ctx.id_expr().ID().toString());

        if (ctx.ass != null){
            visitExpr(ctx.expr());
            AssignInstruction newInstruction = new AssignInstruction(previousSymbolVariable, currentClass.currentSymbolVariable);
            addInstruction(newInstruction);
        } else if (ctx.alloc != null){
            visitObject_alloc(ctx.object_alloc());
            AllocInstruction newInstruction = new AllocInstruction(currentClass.currentSymbolVariable);
            addInstruction(newInstruction);
        }

      //  visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitMember_access(JovaParser.Member_accessContext ctx) {
        return OK;
    }

    @Override
    public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        SymbolClass classOfMethodInvocation = (SymbolClass) currentClass.currentSymbolVariable.getActualType();

        currentClass.setArgList(new ArrayList<>());

        if (ctx.arg_list() != null) {
            visitArg_list(ctx.arg_list());
        }

        String methodName = ctx.ID().toString();
        SymbolMethod foundMethod =
                classOfMethodInvocation.getMatchingMethod(methodName, currentClass.getCurrentArgList()).get();

        MethodInvocationInstruction newInstruction = new MethodInvocationInstruction(classOfMethodInvocation, foundMethod);
        addInstruction(newInstruction);

        currentClass.currentSymbolVariable = foundMethod.getReturnValue();

        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitId_expr(JovaParser.Id_exprContext ctx) {
        currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.CLASS, currentClass, "");

        if (ctx.KEY_THIS() != null) {
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.CLASS, currentClass, "");
        } else if (ctx.ID() != null) {
            currentClass.currentSymbolVariable = currentClass.getCurrentScopeVariable(ctx.ID().toString());
            if (currentClass.currentSymbolVariable == null) {
                ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString());
            }
        } else if (ctx.method_invocation() != null) {
            visitMethod_invocation(ctx.method_invocation());
        }

        if (ctx.member_access() != null) {
            for (JovaParser.Member_accessContext expr : ctx.member_access()) {
                visitMember_access(expr);
            }
        }
        return OK;
    }

    @Override
    public Integer visitArg_list(JovaParser.Arg_listContext ctx) {
        for (JovaParser.ExprContext expr : ctx.expr()) {
            visitExpr(expr);
            currentClass.addArgument(currentClass.currentSymbolVariable);
        }
        return OK;
    }

    @Override
    public Integer visitExpr(JovaParser.ExprContext ctx) {
        if (ctx.op != null) {
            // case: operation, check operand types
            visit(ctx.left);
            SymbolVariable leftVariable = currentClass.currentSymbolVariable;

            visit(ctx.right);
            SymbolVariable rightVariable = currentClass.currentSymbolVariable;

            BinaryInstruction newInstruction = new BinaryInstruction(leftVariable, rightVariable, OperatorTypes.valueOf(ctx.op.toString()));
            addInstruction(newInstruction);
        } else {
            visitPrimary_expr(ctx.primary_expr());
        }

        return OK;
    }

    @Override
    public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        visitChildren(ctx);
        UnaryInstruction newInstruction = new UnaryInstruction(currentClass.currentSymbolVariable, OperatorTypes.valueOf(ctx.op.toString()));
        addInstruction(newInstruction);
        return OK;
    }

    @Override
    public Integer visitPrimary_expr(JovaParser.Primary_exprContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitObject_alloc(JovaParser.Object_allocContext ctx) {
        String className = ctx.CLASS_TYPE().toString();

        Optional<SymbolClass> correspondingClass = symbolTable.getClassByName(className, ctx);
        SymbolClass classObjectAlloc;

        classObjectAlloc = correspondingClass.get();
        currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.CLASS, classObjectAlloc, "");

        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitCtor_args(JovaParser.Ctor_argsContext ctx) {
        return OK;
    }

    @Override
    public Integer visitParan_expr(JovaParser.Paran_exprContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitLiteral(JovaParser.LiteralContext ctx) {
        if (ctx.BOOL_LIT() != null){
            Integer boolValue;
            if (ctx.BOOL_LIT().toString() == "true"){
                boolValue = 1;
            } else {
                boolValue = 0;
            }
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "", boolValue);
            return OK;
        } else if (ctx.STRING_LIT() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.STRING, "",
                    ctx.STRING_LIT().toString());
            return OK;
        } else if (ctx.INT_LIT() != null) {
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "",
                    Integer.valueOf(ctx.INT_LIT().toString()));
            return OK;
        /*} else if (ctx.CHAR_LIT() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.CHAR, "");
            return OK;
        } else if (ctx.FLOAT_LIT() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.FLOAT, "");
            return OK;*/
        } else if (ctx.KEY_NIX() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.NIX, "", null);
            return OK;
        }
        return OK;
    }

    @Override
    public Integer visitControl_stmt(JovaParser.Control_stmtContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitIf_stmt(JovaParser.If_stmtContext ctx) {
        /* Idea:
            * Add an ifInstruction which only knows the currentLabelIndex (i) and whether or not an else block exists.
            * Traverse the expression as usual (this should add instructions for binary/unary expressions).
            * Set label for the if-block (i)
            * Traverse the compound statement (instructions from the if-block will be added to the list).
            * [Optional when else-block exists] Set label for the else-block (i+1)
            * [Optional] Traverse compound statement (instructions from the else-block are added).
        * This should work for nested if-statements as well but not sure if it's a good idea in general.
        */
        if (ctx.else_inst != null){
            addInstruction(new IfInstruction(currentLabelIndex, true));
        } else {
            addInstruction(new IfInstruction(currentLabelIndex, false));
        }

        visitExpr(ctx.expr());

        LabelInstruction ifBlockLabel = new LabelInstruction(currentLabelIndex);
        currentLabelIndex += 1;
        addInstruction(ifBlockLabel);

        if (ctx.else_inst != null){
            LabelInstruction elseBlockLabel = new LabelInstruction(currentLabelIndex);
            currentLabelIndex += 1;

            visit(ctx.if_inst); // instructions for if-block are created within the compound statement
            addInstruction(elseBlockLabel);
            visit(ctx.else_inst);
        } else {
            visit(ctx.if_inst); // instructions for if-block are created within the compound statement
        }

        return OK;
    }

    @Override
    public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    public void addInstruction(Object newInstruction){
        currentClass.getCurrentCallable().instructions.add(newInstruction);
    }
}
