package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.*;
import at.tugraz.ist.cc.symbol_table.*;

import java.util.*;

public class CodeGeneratorVisitor extends JovaBaseVisitor<Integer> {
    public static final int OK = 0;

    private SymbolClass currentClass;
    private final SymbolTable symbolTable;
    private Integer currentConstructorIndex;
    private Integer currentMethodIndex;
    private final Integer currentLabelIndex;
    private Boolean assignMember;
    private MemberAccessInstruction currentMemberAccessInstruction;
    private BaseInstruction primitiveInstruction;

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
            // TODO remove: at the end
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
        SymbolVariable rhs_var;

        if (ctx.ass != null) {
            visitExpr(ctx.expr());
            rhs_var = currentClass.currentSymbolVariable;

        } else if (ctx.alloc != null) {
            visitObject_alloc(ctx.object_alloc());
            rhs_var = currentClass.currentSymbolVariable;
        } else {
            throw new RuntimeException();
        }

        // necassary to differentiate if the last was a instruction or if there is non => ...
        LinkedList<BaseInstruction> backupInstructions = currentClass.getCurrentCallable().instructions;
        currentClass.getCurrentCallable().instructions = new LinkedList<>();

        visitId_expr(ctx.id_expr());

        if (!currentClass.getCurrentCallable().instructions.isEmpty()) {
            BaseInstruction lastInstruction = currentClass.getCurrentCallable().instructions.getLast();

            if (lastInstruction instanceof MemberAccessInstruction) {
                // setting member
                ((MemberAccessInstruction) lastInstruction).setPutValue(rhs_var);
                Collection<BaseInstruction> lhs_instructions = currentClass.getCurrentCallable().instructions;
                currentClass.getCurrentCallable().instructions = backupInstructions;
                currentClass.getCurrentCallable().instructions.addAll(lhs_instructions);

                return OK;
            }
        } else {
            currentClass.getCurrentCallable().instructions = backupInstructions;
        }


        BaseInstruction assignLocalInstruction = new AssignLocalInstruction(currentClass.getCurrentCallable(),
                currentClass.currentSymbolVariable, rhs_var);
        addInstruction(assignLocalInstruction);

        return OK;
    }

    @Override
    public Integer visitMember_access(JovaParser.Member_accessContext ctx) {
        SymbolVariable memberRef = currentClass.currentSymbolVariable;

        if (ctx.method_invocation() != null) {
            visitMethod_invocation(ctx.method_invocation());
            // currentMemberAccess = new MemberAccess(memberRef, currentClass.currentSymbolVariable);
        } else {
            currentMemberAccessInstruction = new MemberAccessInstruction(currentClass.getCurrentCallable(), memberRef,
                    ((SymbolClass) memberRef.getActualType()).getMemberIfExists(ctx.ID().toString()).orElseThrow().getValue());

            addInstruction(currentMemberAccessInstruction);
            assignMember = true;
        }

        return OK;
    }

    @Override
    public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        SymbolClass classOfMethodInvocation = (SymbolClass) currentClass.currentSymbolVariable.getActualType();
        SymbolVariable backupVar = currentClass.currentSymbolVariable;
        currentClass.currentSymbolVariable = null;

        List<SymbolVariable> backupArgList = currentClass.getCurrentArgList();
        currentClass.setArgList(new ArrayList<>());

        if (ctx.arg_list() != null) {
            visitArg_list(ctx.arg_list());
        }

        currentClass.currentSymbolVariable = backupVar;
        String methodName = ctx.ID().toString();
        SymbolMethod foundMethod =
                classOfMethodInvocation.getMatchingMethod(methodName, currentClass.getCurrentArgList()).orElseThrow();

        MethodInvocationInstruction newInstruction = new MethodInvocationInstruction(currentClass.getCurrentCallable(),
                currentClass.currentSymbolVariable, foundMethod, currentClass.getCurrentArgList());
        addInstruction(newInstruction);

        currentClass.setArgList(backupArgList);
        assignMember = false;

        return OK;
    }

    @Override
    public Integer visitId_expr(JovaParser.Id_exprContext ctx) {

        currentClass.currentSymbolVariable = new SymbolVariable(currentClass, true);

        if (ctx.ID() != null) {
            SymbolVariable localVar = currentClass.getLocalVariable(ctx.ID().toString());

            if (localVar == null) {
                currentClass.currentSymbolVariable = currentClass.getCurrentScopeVariable(ctx.ID().toString());
                currentMemberAccessInstruction = new MemberAccessInstruction(currentClass.getCurrentCallable(),
                        new SymbolVariable(currentClass, true), currentClass.currentSymbolVariable);

                addInstruction(currentMemberAccessInstruction);
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

            BaseInstruction newInstruction = CodeGeneratorUtils.createInstruction(
                    currentClass.getCurrentCallable(), OperatorTypes.getOp(ctx.op.getText()),
                    leftVariable, rightVariable);

            addInstruction(newInstruction);

        } else if (ctx.when != null) {
            // case: ternary operator
            LinkedList<BaseInstruction> backupInstructions = currentClass.getCurrentCallable().instructions;
            currentClass.getCurrentCallable().instructions = new LinkedList<>();

            visitExpr(ctx.when);
            LinkedList<BaseInstruction> conditionalExpression = currentClass.getCurrentCallable().instructions;

            if (conditionalExpression.isEmpty()) {
                BaseInstruction conditional = new WrapperInstruction(currentClass.getCurrentCallable(),
                        currentClass.currentSymbolVariable);
                conditionalExpression.add(conditional);
            }

            currentClass.getCurrentCallable().instructions = new LinkedList<>();
            visit(ctx.then);
            List<BaseInstruction> ifInstructions = currentClass.getCurrentCallable().instructions;

            if (ifInstructions.isEmpty()) {
                BaseInstruction literal = new WrapperInstruction(currentClass.getCurrentCallable(),
                        currentClass.currentSymbolVariable);
                ifInstructions.add(literal);
            }

            currentClass.getCurrentCallable().instructions = new LinkedList<>();
            visit(ctx.el);
            List<BaseInstruction> elseInstructions = currentClass.getCurrentCallable().instructions;

            if (elseInstructions.isEmpty()) {
                BaseInstruction literal = new WrapperInstruction(currentClass.getCurrentCallable(),
                        currentClass.currentSymbolVariable);
                elseInstructions.add(literal);
            }

            currentClass.getCurrentCallable().instructions = backupInstructions;
            addInstruction(new TernaryInstruction(currentClass.getCurrentCallable(),
                    conditionalExpression, ifInstructions, elseInstructions));

        } else {
            visitPrimary_expr(ctx.primary_expr());
        }

        return OK;
    }

    @Override
    public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        visitChildren(ctx);
        BaseInstruction newInstruction = CodeGeneratorUtils.createInstruction(
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
        SymbolClass classObjectAlloc = correspondingClass.orElseThrow();

        SymbolVariable classWrappedAsVariable = new SymbolVariable(SymbolType.CLASS, classObjectAlloc, "");

        List<SymbolVariable> backupArgList = currentClass.getCurrentArgList();
        currentClass.setArgList(new ArrayList<>());

        visitChildren(ctx);

        BaseInstruction alloc = new AllocInstruction(currentClass.getCurrentCallable(),
                classWrappedAsVariable, currentClass.getCurrentArgList());

        addInstruction(alloc);

        currentClass.setArgList(backupArgList);

        return OK;
    }

    @Override
    public Integer visitCtor_args(JovaParser.Ctor_argsContext ctx) {
        if (ctx.arg_list() != null) {
            visitArg_list(ctx.arg_list());
        }

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

        LinkedList<BaseInstruction> backupInstructions = currentClass.getCurrentCallable().instructions;

        currentClass.getCurrentCallable().instructions = new LinkedList<>();

        visitExpr(ctx.expr());
        List<BaseInstruction> conditionalExpression = currentClass.getCurrentCallable().instructions;

        if (conditionalExpression.isEmpty()) {
            BaseInstruction conditional = new WrapperInstruction(currentClass.getCurrentCallable(),
                    currentClass.currentSymbolVariable);
            conditionalExpression.add(conditional);
        }

        currentClass.getCurrentCallable().instructions = new LinkedList<>();

        visit(ctx.if_inst);
        List<BaseInstruction> ifInstructions = currentClass.getCurrentCallable().instructions;
        List<BaseInstruction> elseInstructions = new ArrayList<>();

        if (ctx.else_inst != null) {
            currentClass.getCurrentCallable().instructions = new LinkedList<>();
            visit(ctx.else_inst);
            elseInstructions = currentClass.getCurrentCallable().instructions;
        } else {
            elseInstructions = new ArrayList<>();
        }

        currentClass.getCurrentCallable().instructions = backupInstructions;
        addInstruction(new IfInstruction(currentClass.getCurrentCallable(), conditionalExpression, ifInstructions, elseInstructions));

        return OK;
    }

    @Override
    public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        LinkedList<BaseInstruction> backupInstructions = currentClass.getCurrentCallable().instructions;

        currentClass.getCurrentCallable().instructions = new LinkedList<>();

        visitExpr(ctx.expr());
        List<BaseInstruction> conditionalExpression = currentClass.getCurrentCallable().instructions;

        if (conditionalExpression.isEmpty()) {
            BaseInstruction conditional = new WrapperInstruction(currentClass.getCurrentCallable(),
                    currentClass.currentSymbolVariable);
            conditionalExpression.add(conditional);
        }

        currentClass.getCurrentCallable().instructions = new LinkedList<>();

        visit(ctx.compound_stmt());
        List<BaseInstruction> ifInstructions = currentClass.getCurrentCallable().instructions;

        currentClass.getCurrentCallable().instructions = backupInstructions;
        addInstruction(new WhileInstruction(currentClass.getCurrentCallable(), conditionalExpression, ifInstructions));

        return OK;
    }

    public void addInstruction(BaseInstruction newInstruction) {
        currentClass.getCurrentCallable().instructions.add(newInstruction);
        currentClass.currentSymbolVariable = newInstruction.getResult();
    }
}
