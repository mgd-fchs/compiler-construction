package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.*;
import at.tugraz.ist.cc.symbol_table.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CodeGeneratorVisitor extends JovaBaseVisitor<Integer> {
    public static final int OK = 0;
    public static final int ERROR_GENERAL = -1;

    private SymbolClass currentClass;
    private final SymbolTable symbolTable;
    private Integer currentConstructorIndex;
    private Integer currentMethodIndex;
    private final Integer currentLabelIndex;
    private Boolean assignMember;
    private MemberAccessInstruction currentMemberAccessInstruction;

    public CodeGeneratorVisitor() {
        symbolTable = SymbolTable.getInstance();
        currentClass = null;
        currentConstructorIndex = 0;
        currentLabelIndex = 0;
        currentMethodIndex = 0;
        currentMemberAccessInstruction = null;
        assignMember = false;
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx) {
        try {
            visitChildren(ctx);
        } catch (Exception e) {
            // TODO remove:
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Integer visitClass_decl(JovaParser.Class_declContext ctx) {
        visitClass_head(ctx.class_head());
        visitClass_body(ctx.class_body());
        currentMethodIndex = 0;
        currentConstructorIndex = 0;
        return OK;
    }

    @Override
    public Integer visitType(JovaParser.TypeContext ctx) {
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitClass_head(JovaParser.Class_headContext ctx) {
        currentClass = symbolTable.getClassByName(ctx.CLASS_TYPE().toString(), ctx).orElseThrow();
        return OK;
    }

    @Override
    public Integer visitClass_body(JovaParser.Class_bodyContext ctx) {
        visitChildren(ctx);
        return OK;
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

    @Override
    public Integer visitMethod_decl(JovaParser.Method_declContext ctx) {
        currentClass.setCurrentCallable(currentClass.getMethods().get(currentMethodIndex));
        visitChildren(ctx);
        currentMethodIndex += 1;
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
        visitExpr(ctx.expr());
        ReturnInstruction newInstruction = new ReturnInstruction(currentClass.getCurrentCallable(), currentClass.currentSymbolVariable);
        addInstruction(newInstruction);
        return OK;
    }

    @Override
    public Integer visitAssign_stmt(JovaParser.Assign_stmtContext ctx) {
        assignMember = false;
        visitId_expr(ctx.id_expr());
        SymbolVariable lhsVar = currentClass.currentSymbolVariable;

        List<BaseInstruction> instructions = currentClass.getCurrentCallable().instructions;
        BaseInstruction newInstruction = null;
        MemberAccessInstruction lastInstruction = null;

        if (assignMember){
            lastInstruction = (MemberAccessInstruction) instructions.get(instructions.size()-1);
        }
        if (ctx.ass != null) {
            visitExpr(ctx.expr());
            newInstruction = new AssignLocalInstruction(currentClass.getCurrentCallable(), lhsVar, currentClass.currentSymbolVariable);

            if (lastInstruction != null){
                lastInstruction.setPutValue(currentClass.currentSymbolVariable);
            } else {
                addInstruction(newInstruction);
            }
        } else if (ctx.alloc != null) {
            visitObject_alloc(ctx.object_alloc());
            newInstruction = new AllocInstruction(currentClass.getCurrentCallable(),
                    currentClass.currentSymbolVariable, Collections.EMPTY_LIST); // TODO add right params

            if (lastInstruction != null){
                lastInstruction.setPutValue(newInstruction.getResult());
            }
            addInstruction(newInstruction);
        }

        return OK;
    }

    @Override
    public Integer visitMember_access(JovaParser.Member_accessContext ctx) {
       /* if (currentMemberAccessInstruction == null){
            if (ctx.method_invocation() != null){
                SymbolVariable classSymbolVar = currentClass.currentSymbolVariable;
                visitMethod_invocation(ctx.method_invocation());
                currentMemberAccessInstruction = new MemberAccessInstruction(currentClass.getCurrentCallable(), classSymbolVar, currentClass.currentSymbolVariable);
            } else {
                currentMemberAccessInstruction = new MemberAccessInstruction(currentClass.getCurrentCallable(), currentClass.currentSymbolVariable,
                        new SymbolVariable(currentClass.currentSymbolVariable, ctx.ID().toString(), false));
            }
        } else {*/
            SymbolVariable memberRef = currentClass.currentSymbolVariable;

            if (ctx.method_invocation() != null){
                visitMethod_invocation(ctx.method_invocation());
                // currentMemberAccess = new MemberAccess(memberRef, currentClass.currentSymbolVariable);
            } else {
                currentMemberAccessInstruction = new MemberAccessInstruction(currentClass.getCurrentCallable(), memberRef,
                        ((SymbolClass) memberRef.getActualType()).getMemberIfExists(ctx.ID().toString()).orElseThrow().getValue());

                currentClass.currentSymbolVariable = currentMemberAccessInstruction.getResult();
                addInstruction(currentMemberAccessInstruction);
                assignMember = true;
            }
        //}

        return OK;
    }

    @Override
    public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        // TODO what if the method is from inside the own class (without this keyword)
        SymbolClass classOfMethodInvocation = (SymbolClass) currentClass.currentSymbolVariable.getActualType();

        currentClass.setArgList(new ArrayList<>());

        if (ctx.arg_list() != null) {
            visitArg_list(ctx.arg_list());
        }

        String methodName = ctx.ID().toString();
        SymbolMethod foundMethod =
                classOfMethodInvocation.getMatchingMethod(methodName, currentClass.getCurrentArgList()).orElseThrow();

        MethodInvocationInstruction newInstruction = new MethodInvocationInstruction(currentClass.getCurrentCallable(),
                currentClass.currentSymbolVariable /* TODO has this currentClass.currentSymbolVariable the right ref??? */
                , foundMethod, Collections.EMPTY_LIST); // TODO Params
        addInstruction(newInstruction);

        currentClass.currentSymbolVariable = newInstruction.getResult();
        assignMember = false;
        visitChildren(ctx);
        return OK;
    }

    @Override
    public Integer visitId_expr(JovaParser.Id_exprContext ctx) {

        currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.CLASS, currentClass, "");

        if (ctx.ID() != null) {
            SymbolVariable localVar = currentClass.getLocalVariable(ctx.ID().toString());

            if (localVar == null){
                currentClass.currentSymbolVariable = currentClass.getCurrentScopeVariable(ctx.ID().toString());
                currentMemberAccessInstruction = new MemberAccessInstruction(currentClass.getCurrentCallable(),
                        new SymbolVariable(currentClass, true), currentClass.currentSymbolVariable);
                addInstruction(currentMemberAccessInstruction);
                currentClass.currentSymbolVariable = currentMemberAccessInstruction.getResult();
                assignMember = true;
            } else {
                currentClass.currentSymbolVariable = localVar;
            }

        } else if (ctx.method_invocation() != null) {
            visitMethod_invocation(ctx.method_invocation());
        }
        if (ctx.member_access() != null) {

            for (JovaParser.Member_accessContext expr : ctx.member_access()) {
                visitMember_access(expr);
            }
            currentMemberAccessInstruction = null;
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
      //  List<BaseInstruction> backupInstructions = currentClass.getCurrentCallable().getInstructions();

        if (ctx.op != null) {
            // case: operation, check operand types
            visit(ctx.left);
            SymbolVariable leftVariable = currentClass.currentSymbolVariable;

            visit(ctx.right);
            SymbolVariable rightVariable = currentClass.currentSymbolVariable;

            BaseInstruction newInstruction = CodeGeneratorUtils.createBinaryInstruction(
                    currentClass.getCurrentCallable(), OperatorTypes.getOp(ctx.op.getText()),
                    leftVariable, rightVariable);
            //currentClass.getCurrentCallable().instructions = backupInstructions;

            currentClass.currentSymbolVariable = newInstruction.getResult();
            addInstruction(newInstruction);

        } else if (ctx.when != null) {
            // case: ternary operator
            currentClass.getCurrentCallable().instructions = new ArrayList<>();

            visitExpr(ctx.when);
            Object conditionalExpression = currentClass.getCurrentCallable().instructions.get(currentClass.getCurrentCallable().instructions.size() - 1);
            currentClass.getCurrentCallable().instructions = new ArrayList<>();

            visit(ctx.then);
            List<BaseInstruction> ifInstructions = currentClass.getCurrentCallable().instructions;
            List<BaseInstruction> elseInstructions = new ArrayList<>();

            if (ctx.el != null) {
                currentClass.getCurrentCallable().instructions = new ArrayList<>();
                visit(ctx.el);
                elseInstructions = currentClass.getCurrentCallable().instructions;
            }

            //currentClass.getCurrentCallable().instructions = backupInstructions;
            addInstruction(new ConditionalInstruction(currentClass.getCurrentCallable(), conditionalExpression, ifInstructions, elseInstructions));

        } else {
            //currentClass.getCurrentCallable().instructions = new ArrayList<>();
            visitPrimary_expr(ctx.primary_expr());

            //backupInstructions.addAll(currentClass.getCurrentCallable().instructions);
            //currentClass.getCurrentCallable().instructions = backupInstructions;
        }

        return OK;
    }

    @Override
    public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        visitChildren(ctx);
        BaseInstruction newInstruction = CodeGeneratorUtils.createBinaryInstruction(
                currentClass.getCurrentCallable(), OperatorTypes.getOp(ctx.op.getText()),
                currentClass.currentSymbolVariable, null);
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

        classObjectAlloc = correspondingClass.orElseThrow();
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
        if (ctx.BOOL_LIT() != null) {
            int boolValue;
            if (ctx.BOOL_LIT().toString().equals("true")) {
                boolValue = 1;
            } else {
                boolValue = 0;
            }
            SymbolVariable newVar = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "", boolValue);

            currentClass.currentSymbolVariable = newVar;
            return OK;
        } else if (ctx.STRING_LIT() != null) {
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
        } else if (ctx.KEY_NIX() != null) {
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

        List<BaseInstruction> backupInstructions = currentClass.getCurrentCallable().instructions;

        currentClass.getCurrentCallable().instructions = new ArrayList<>();

        visitExpr(ctx.expr());
        BaseInstruction conditionalExpression = currentClass.getCurrentCallable().instructions.get(currentClass.getCurrentCallable().instructions.size() - 1);
        currentClass.getCurrentCallable().instructions = new ArrayList<>();

        visit(ctx.if_inst);
        List<BaseInstruction> ifInstructions = currentClass.getCurrentCallable().instructions;
        List<BaseInstruction> elseInstructions = new ArrayList<>();

        if (ctx.else_inst != null) {
            currentClass.getCurrentCallable().instructions = new ArrayList<>();
            visit(ctx.else_inst);
            elseInstructions = currentClass.getCurrentCallable().instructions;
        } else {
            elseInstructions = null;
        }

        currentClass.getCurrentCallable().instructions = backupInstructions;
        addInstruction(new ConditionalInstruction(currentClass.getCurrentCallable(), conditionalExpression, ifInstructions, elseInstructions));

        return OK;
    }

    @Override
    public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        List<BaseInstruction> backupInstructions = currentClass.getCurrentCallable().instructions;

        currentClass.getCurrentCallable().instructions = new ArrayList<>();

        visitExpr(ctx.expr());
        Object conditionalExpression = currentClass.getCurrentCallable().instructions.get(currentClass.getCurrentCallable().instructions.size() - 1);
        currentClass.getCurrentCallable().instructions = new ArrayList<>();

        visit(ctx.compound_stmt());
        List<BaseInstruction> ifInstructions = currentClass.getCurrentCallable().instructions;

        currentClass.getCurrentCallable().instructions = backupInstructions;
        addInstruction(new ConditionalInstruction(currentClass.getCurrentCallable(), conditionalExpression, ifInstructions, null));

        return OK;
    }

    public void addInstruction(BaseInstruction newInstruction) {
        currentClass.getCurrentCallable().instructions.add(newInstruction);
    }
}
