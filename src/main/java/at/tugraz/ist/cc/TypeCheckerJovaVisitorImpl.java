package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.stream.Collectors;

public class TypeCheckerJovaVisitorImpl extends JovaBaseVisitor<Integer>{
    public static final int OK = 0;

    public static final int TYPE_CLASS = SymbolType.CLASS.getValue();
    public static final int TYPE_PRIMITIVE = SymbolType.PRIMITIVE.getValue();
    public static final int TYPE_ERROR = -30;

    public static final int ERROR_DOUBLE_DEFINITION_CLASS = -50;
    public static final int ERROR_DOUBLE_DECLARATION_METHOD = -51;
    public static final int ERROR_DOUBLE_DECLARATION_VARIABLE = -52;

    public static final int ERROR_MAIN_INSTANTIATION = -60;
    public static final int ERROR_MAIN_WITH_MEMBER = -61;
    public static final int ERROR_MAIN_WITH_WRONG_METHOD = -62;

    public static final int ERROR_UNKOWN_TYPE = -70;

    public static final int ERROR_ID_UNDEF = -80;

    private SymbolClass currentClass;
    private SymbolVariable currentVar;
    private final SymbolTable symbolTable;

    public TypeCheckerJovaVisitorImpl() {
        symbolTable = SymbolTable.getInstance();
        currentClass = null;
        currentVar = null;
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx){
        // TODO: GENERAL remove all outputs like this, before submission
        try {
            System.out.println("visitProgram");
            visitChildren(ctx);
        } catch (Exception e) {
            throw e;
        } finally {
            // TODO change singleton to normal class?
            SymbolTable.reset();
        }

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

        if(symbolTable.addClass(newClass, ctx) != 0) {
            // skip further checking of the class if it is a class double definition
            currentClass = null;
            return ERROR_DOUBLE_DEFINITION_CLASS;
        }

        currentClass = newClass;
        return visitChildren(ctx);
    }

    @Override public Integer visitClass_body(JovaParser.Class_bodyContext ctx) {
        return  visitChildren(ctx);
    }

    @Override
    public Integer visitCtor(JovaParser.CtorContext ctx) {
        if (currentClass.getClassName().equals(SymbolClass.MAIN_CLASS_NAME)) {
            ErrorHandler.INSTANCE.addMainMemberError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
            return ERROR_MAIN_WITH_MEMBER;
        }

        visitParams(ctx.params());
        currentClass.buildConstructor(ctx.CLASS_TYPE().toString(), ctx);
        return visitCtor_body(ctx.ctor_body());
    }

    @Override
    public Integer visitCtor_body(JovaParser.Ctor_bodyContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitMember_decl(JovaParser.Member_declContext ctx) {
        if (currentClass.getClassName().equals(SymbolClass.MAIN_CLASS_NAME)) {
            ErrorHandler.INSTANCE.addMainMemberError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
            return ERROR_MAIN_WITH_MEMBER;
        }

        visitChildren(ctx);

        currentClass.buildCurrentMembers(SymbolModifier.valueOf(ctx.AMOD().toString().toUpperCase()), ctx);
        return OK;
    }

    @Override
    public Integer visitId_list(JovaParser.Id_listContext ctx) {
        List<TerminalNode> ids = ctx.ID();
        List<String> idsString = ids.stream().map(Object::toString).collect(Collectors.toList());
        currentClass.setCurrentIds(idsString);

        return OK;
    }

    @Override public Integer visitMethod_decl(JovaParser.Method_declContext ctx) {
        int headStatus = visitMethod_head(ctx.method_head());

        // if the head is not ok or double definition we stop looking at the method
        if (headStatus != OK) {
            return headStatus;
        }

        return visitMethod_body(ctx.method_body());
    }

    @Override
    public Integer visitMethod_head(JovaParser.Method_headContext ctx) {
        visitChildren(ctx);

        int error = currentClass.addMethod(
                SymbolModifier.valueOf(ctx.AMOD().toString().toUpperCase()), ctx.ID().toString(), ctx);

        if (error != 0) {
            // skip further checking of the method if it is a error
            return error;
        }

        return OK;
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
                Optional<SymbolClass> foundClass = symbolTable.getClassByName(className, ctx);

                if (foundClass.isPresent()){
                    variable = new SymbolVariable(SymbolType.CLASS, foundClass.get(), variableName);
                } else {
                    return ERROR_UNKOWN_TYPE;
                }
            } else {
                System.out.println(-8);
            }
            params.add(variable);
        }

        currentClass.setCurrentParams(params);
        return OK;
    }

    @Override
    public Integer visitMethod_body(JovaParser.Method_bodyContext ctx) {
        /* TODO if an error occurs at a child we should stop i think?
            if so, it will be necessary to visit the childs by "hand" without callingv isitChildren.
            or we override the visitChildren like a previous idea i think
        * */
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

        return currentClass.saveLocalVariables(ctx);
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
        System.out.println("visitMember_access");
        // TODO: support method_invocation + member_access? (hier gemeint mit Class als return-type?

        if (ctx.DOTOP() == null) {
            // TODO: is this possible?
            System.out.println("no dot found at memberaccess???");
            System.exit(34);
        }

        SymbolVariable var = currentClass.getCurrentMemberAccess();

        if (var.getType() == SymbolType.PRIMITIVE) {
            // TODO: put error in member-access
            String id = (ctx.ID() != null) ? ctx.ID().toString() : "";
            ErrorHandler.INSTANCE.addDoesNotHaveFieldError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    var.getType().toString().toLowerCase(), id);
            return -1;
        }

        SymbolClass class_accessed = (SymbolClass) var.getActualType();

        if (ctx.ID() != null) {
            if (!class_accessed.checkIfVariableExists(ctx.ID().toString())) {
                ErrorHandler.INSTANCE.addDoesNotHaveFieldError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        class_accessed.getClassName(), ctx.ID().toString());
                return -1;
            }

            return 0;
        }

        if (ctx.method_invocation() != null) {
            // TODO implement multiple method-invocs
            if (class_accessed.getMatchingMethods(ctx.method_invocation().ID().toString()).size() > 0) {
                return visitChildren(ctx);
            }
            else {
                ErrorHandler.INSTANCE.addDoesNotHaveFieldError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        class_accessed.getClassName(), ctx.method_invocation().ID().toString());
                return -1;
            }
        }

        return visitChildren(ctx);
    }

    @Override public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        System.out.println("visitMethod_invocation");

        SymbolClass class_accessed = currentClass.getCurrentClassAccess();

        Collection<SymbolMethod> methods = class_accessed.getMatchingMethods(ctx.ID().toString());
        class_accessed.setArgList();
        if (ctx.arg_list() != null) {
            visit(ctx.arg_list());
        }

        for (SymbolMethod method : methods) {
            if (class_accessed.checkValidArgList(method)) {
                class_accessed.resetArgList();
                return 0;
            }
        }

        String params = "";
        if (ctx.arg_list() != null) {
            params = class_accessed.getArgListTypes();
        }


        if (currentClass.getCurrentMemberAccess() != null) {
            ErrorHandler.INSTANCE.addCannotInvokeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    class_accessed.getClassName(), ctx.ID().toString(), params);
        }
        else {
            ErrorHandler.INSTANCE.addUndefMethodError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString(), params);
        }

        currentClass.resetArgList();
        return 0;
    }

    @Override public Integer visitId_expr(JovaParser.Id_exprContext ctx) {
        System.out.println("Visiting ID expression!");
        SymbolClass class_accessed = currentClass.getCurrentClassAccess();


        if (class_accessed.currentlyGatheringArguments()) {
            if (ctx.ID() != null) {
                if (!currentClass.checkIfVariableExists(ctx.ID().toString())) {
                    // TODO: add which error?
                    ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString());
                    return -1;
                }

                return 0;
            }
        }

        if (ctx.member_access() != null) {
            SymbolVariable var_accessed = null;
            if (ctx.KEY_THIS() != null) {
                var_accessed = new SymbolVariable(SymbolType.CLASS, currentClass, "");
            } else if (ctx.ID() != null) {
                var_accessed = currentClass.getCurrentScopeVariable(ctx.ID().toString());
                if (var_accessed == null) {
                    ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString());
                    return -1;
                }
            }

            SymbolVariable backup = currentClass.getCurrentMemberAccess();
            currentClass.setCurrentMemberAccess(var_accessed);
            int ret = visitChildren(ctx);
            currentClass.setCurrentMemberAccess(backup);
            return ret;
        }

        if(ctx.ID() != null) {
            // case: is function-level variable
            if (currentClass.getCurrentMethod() != null) {
                currentVar = currentClass.getCurrentMethod().getLocalVariableById(ctx.ID().getText());
            }
            // case: is class-level variable
            if (currentVar == null) {
                currentVar = currentClass.getMemberById(ctx.ID().getText());
            }
            // check variable type and reset current variable
            Object varType = currentVar.getActualType();

            if (varType instanceof SymbolPrimitiveType){
                currentVar = null;
                return ((SymbolPrimitiveType) varType).getValue();
            } else if (currentVar.getType() instanceof SymbolType) {
                currentVar = null;
                return TYPE_CLASS;
            }
        } else {
            // variable cannot be assigned without being defined first
            ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), String.valueOf(ctx.ID().getText()));
            currentVar = null;
            return TYPE_ERROR;
        }

        currentVar = null;
        return visitChildren(ctx);
    }

    @Override public Integer visitArg_list(JovaParser.Arg_listContext ctx) {
        System.out.println("visitArg_list");
        return visitChildren(ctx);
    }

    @Override public Integer visitExpr(JovaParser.ExprContext ctx) {
        System.out.println("Visit expression!");

        if (ctx.op != null) {
            // case: operation, check operand types
            Integer lhs_type = visit(ctx.left);
            Integer rhs_type = visit(ctx.right);

            // check both types are valid
            if (lhs_type == TYPE_ERROR || rhs_type == TYPE_ERROR) {
                return TYPE_ERROR;
            }

            // check types are compatible
            return CompatibilityCheckUtils.checkOperatorCompatibility(lhs_type, rhs_type, ctx);

        } else if(ctx.when != null){
            // case: ternary operator
            Integer whenType = visit(ctx.when);
            Integer thenType = visit(ctx.then);
            Integer elseType = visit(ctx.el);
            return CompatibilityCheckUtils.checkTernaryOperatorCompatibility(whenType, thenType, elseType, ctx);

        } else {
            // case: primary expression
            return visitChildren(ctx.prim);
        }

    }

    @Override public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        Integer returnValue = visit(ctx.primary_expr());
        return returnValue;
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
        Integer returnValue = visit(ctx.expr());
        return returnValue;
    }

    @Override
    public Integer visitLiteral(JovaParser.LiteralContext ctx) {
        // return the type of the literal

        SymbolClass class_accessed = currentClass.getCurrentClassAccess();

        if (class_accessed.currentlyGatheringArguments()) {
            SymbolPrimitiveType type = null; //SymbolPrimitiveType.NIX;
            if (ctx.BOOL_LIT() != null) {
                type = SymbolPrimitiveType.BOOL;
            } else if (ctx.INT_LIT() != null) {
                type = SymbolPrimitiveType.INT;
            } else if (ctx.STRING_LIT() != null) {
                type = SymbolPrimitiveType.STRING;
            }

            if (type != null) {
                class_accessed.addPrimitiveArgument(type);
            }
            else {
                System.out.println("visitLiteral: invalid type");
                System.exit(25);
            }

            return OK;
        }

        if (ctx.BOOL_LIT() != null){
            return SymbolPrimitiveType.BOOL.getValue();
        } else if (ctx.STRING_LIT() != null){
            return SymbolPrimitiveType.STRING.getValue();
        } else if (ctx.INT_LIT() != null){
            return SymbolPrimitiveType.INT.getValue();
        } else if (ctx.KEY_NIX() != null){
            return SymbolPrimitiveType.NIX.getValue();
        } else {
            ErrorHandler.INSTANCE.addUnknownTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), String.valueOf(ctx.stop.getText()));
            return TYPE_ERROR;
        }
    }

    @Override
    public Integer visitControl_stmt(JovaParser.Control_stmtContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitIf_stmt(JovaParser.If_stmtContext ctx) {
        Integer conditionType = visit(ctx.expr());
        if (CompatibilityCheckUtils.checkConditionCompatibility(conditionType, ctx.expr()) == TYPE_ERROR){
            return TYPE_ERROR;
        }

        return visitChildren(ctx);
    }

    @Override
    public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        Integer conditionType = visit(ctx.expr());
        if (CompatibilityCheckUtils.checkConditionCompatibility(conditionType, ctx.expr()) == TYPE_ERROR){
            return TYPE_ERROR;
        }
        return visitChildren(ctx);
    }

}
