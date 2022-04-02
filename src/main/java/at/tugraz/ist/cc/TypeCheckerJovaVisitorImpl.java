package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.stream.Collectors;

public class TypeCheckerJovaVisitorImpl extends JovaBaseVisitor<Integer>{
    public static final int OK = 0;
    public static final int ERROR_GENERAL = -1;


    public static final int TYPE_CLASS = SymbolType.CLASS.getValue();
    public static final int TYPE_PRIMITIVE = SymbolType.PRIMITIVE.getValue();
    public static final int TYPE_METHOD = SymbolType.METHOD.getValue();
    public static final int TYPE_ERROR = -30;

    public static final int ERROR_DOUBLE_DEFINITION_CLASS = -50;
    public static final int ERROR_DOUBLE_DECLARATION_METHOD = -51;
    public static final int ERROR_DOUBLE_DECLARATION_VARIABLE = -52;

    public static final int ERROR_MAIN_INSTANTIATION = -60;
    public static final int ERROR_MAIN_WITH_MEMBER = -61;
    public static final int ERROR_MAIN_WITH_WRONG_METHOD = -62;
    public static final int ERROR_MAIN_INSTANTIATE = -63;

    public static final int ERROR_UNKOWN_TYPE = -70;

    public static final int ERROR_ID_UNDEF = -80;

    public static final int ERROR_UNKNOWN_CTOR = -90;



    private SymbolClass currentClass;
    private SymbolVariable currentVar;
    private final SymbolTable symbolTable;
    private boolean isReturnStatement = false;
    public SymbolVariable currentMember;

    public TypeCheckerJovaVisitorImpl() {
        symbolTable = SymbolTable.getInstance();
        currentClass = null;
        currentVar = null;
    }

    @Override
    public Integer visitProgram(JovaParser.ProgramContext ctx){
        // TODO: GENERAL remove all outputs like this, before submission
        try {
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
        int returnError = visitClass_head(ctx.class_head());

        if (returnError != OK) {
            return returnError;
        }

        return visitClass_body(ctx.class_body());
    }

    @Override
    public Integer visitType(JovaParser.TypeContext ctx) {
        if (ctx.CLASS_TYPE() != null){
            if (symbolTable.getClassByName(ctx.CLASS_TYPE().toString(), ctx).isEmpty()){
                return ERROR_UNKOWN_TYPE;
            }

            if (ctx.CLASS_TYPE().toString().equals(SymbolClass.MAIN_CLASS_NAME)) {
                ErrorHandler.INSTANCE.addMainInstatiationError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
                return ERROR_MAIN_INSTANTIATION;
            }

            currentClass.setCurrentClassName(ctx.CLASS_TYPE().toString());
            currentClass.setCurrentSymbolType(SymbolType.CLASS);

            return OK;
        } else if(ctx.PRIMITIVE_TYPE() != null) {
            currentClass.setCurrentSymbolPrimitiveType(SymbolPrimitiveType.valueOf(ctx.PRIMITIVE_TYPE().toString().toUpperCase()));
            currentClass.setCurrentSymbolType(SymbolType.PRIMITIVE);

            return OK;
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
        return OK;
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

        String classType = ctx.CLASS_TYPE().toString();
        int wrongClassName = OK;
        if (!currentClass.getClassName().equals(classType)) {
            ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), classType);
            wrongClassName = ERROR_GENERAL;
        }

        int typeErrorParams = visitParams(ctx.params());
        if (typeErrorParams != OK || wrongClassName != OK) {
            return ERROR_GENERAL;
        }

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

        int typeError = visitType(ctx.type());
        if (typeError != OK){
            return typeError;
        }

        visitId_list(ctx.id_list());

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
        if (currentClass.getClassName().equals(SymbolClass.MAIN_CLASS_NAME) && currentClass.getMethods().size() > 0) {
            ErrorHandler.INSTANCE.addMainMemberError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
            return ERROR_GENERAL;
        }

        int headStatus = visitMethod_head(ctx.method_head());

        // if the head is not ok or double definition we stop looking at the method
        if (headStatus != OK) {
            return headStatus;
        }

        return visitMethod_body(ctx.method_body());
    }

    @Override
    public Integer visitMethod_head(JovaParser.Method_headContext ctx) {
        int typeErrorReturn = visitType(ctx.type());

        // saving current values which will be overriden when calling
        String currentClassSaving = this.currentClass.getCurrentClassName();
        SymbolPrimitiveType currentPrimitiveSaving = currentClass.getCurrentSymbolPrimitiveType();
        SymbolType currentTypeSaving = currentClass.getCurrentSymbolType();

        int typeErrorParams = visitParams(ctx.params());
        if (typeErrorReturn != OK || typeErrorParams != OK) {
            return ERROR_GENERAL;
        }

        // restoring if no error happened
        // TODO: saving/restoring is a ugly solution
        currentClass.setCurrentSymbolPrimitiveType(currentPrimitiveSaving);
        currentClass.setCurrentClassName(currentClassSaving);
        currentClass.setCurrentSymbolType(currentTypeSaving);

        return currentClass.addMethod(
                SymbolModifier.valueOf(ctx.AMOD().toString().toUpperCase()), ctx.ID().toString(), ctx);
    }

    @Override
    public Integer visitParams(JovaParser.ParamsContext ctx) {
        return (ctx.param_list() != null) ? visitParam_list(ctx.param_list()) : OK;
    }

    @Override
    public Integer visitParam_list(JovaParser.Param_listContext ctx) {
        boolean typeErrorOccurred = false;
        List<TerminalNode> ids = ctx.ID();
        List<JovaParser.TypeContext> types = ctx.type();
        List<SymbolVariable> params = new ArrayList<>();

        if (currentClass.getClassName().equals(SymbolClass.MAIN_CLASS_NAME) && ids.size() != 0) {
            ErrorHandler.INSTANCE.addMainMemberError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
            return ERROR_MAIN_WITH_WRONG_METHOD;
        }

        for (int id = 0; id < ids.size(); ++id) {
            SymbolVariable variable;
            String variableName = ids.get(id).toString();

            int error = visitType(types.get(id));
            if (error != OK) {
                typeErrorOccurred = true;
                continue;
            } else {
                variable = currentClass.buildParam(variableName, ctx);
            }

            params.add(variable);
        }

        if (typeErrorOccurred) {
            return ERROR_GENERAL;
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
        int error = visitType(ctx.type());
        if (error != OK) {
            return error;
        }

        error = visitId_list(ctx.id_list());
        if (error != OK) {
            return error;
        }

        return currentClass.saveLocalVariables(ctx);
    }

    @Override
    public Integer visitRet_stmt(JovaParser.Ret_stmtContext ctx) {

        Integer visitResult = visit(ctx.expr());
        if (visitResult == TYPE_ERROR){
            return TYPE_ERROR;
        } else {
            SymbolVariable variable = currentClass.currentSymbolVariable;
            Integer checkResult = CompatibilityCheckUtils.checkReturnValue(variable, currentClass, ctx);
            if (checkResult == TYPE_ERROR){
               return TYPE_ERROR;
            }
        }
        return OK;
    }


    @Override
    public Integer visitAssign_stmt(JovaParser.Assign_stmtContext ctx) {
        int errorIdExpr = visitId_expr(ctx.id_expr());

        if (errorIdExpr == TYPE_ERROR){
            return TYPE_ERROR;
        }

        SymbolVariable assignedVarShould = currentClass.currentSymbolVariable;

        if (ctx.expr() != null) {
            int errorExpr = visitExpr(ctx.expr());
        } else if (ctx.object_alloc() != null) {
            int errorObjc = visitObject_alloc(ctx.object_alloc());
        }

        if (ctx.ass != null){
            Integer exprReturnValue = visit(ctx.expr());

            if (exprReturnValue == TYPE_ERROR){
                return TYPE_ERROR;
            }

            SymbolVariable assignedVarIs = currentClass.currentSymbolVariable;
            Integer validAssignment = CompatibilityCheckUtils.checkExpressionAssignment(assignedVarShould, assignedVarIs, ctx, currentClass);

            if (validAssignment == TYPE_ERROR){
                return TYPE_ERROR;
            }
        }
        return errorIdExpr;
    }

    @Override
    public Integer visitMember_access(JovaParser.Member_accessContext ctx) {
        SymbolVariable var = currentClass.getCurrentMemberAccess();

        if (var.getType() == SymbolType.PRIMITIVE) {
            String id = "";
            if (ctx.ID() != null || ctx.method_invocation() != null) {
                id = ctx.ID().toString();
                ErrorHandler.INSTANCE.addDoesNotHaveFieldError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        var.getActualType().toString().toLowerCase(), id);
                return TYPE_ERROR;
            } /*else if (ctx.method_invocation() != null) {
                // pls dont judge me for this code
                id = ctx.method_invocation().ID().toString();
                String[] params = new String[0];

                if (ctx.method_invocation().arg_list() != null) {
                    List<SymbolVariable> args_backup = currentClass.getCurrentArgList();
                    currentClass.setArgList(new ArrayList<>());
                    int tmp = visitArg_list(ctx.method_invocation().arg_list());
                    if (tmp != OK) {
                        currentClass.setArgList(args_backup);
                        return -1;
                    }

                    params = currentClass.getCurrentArgList().stream().map(SymbolVariable::getTypeAsString).toArray(String[]::new);
                    currentClass.setArgList(args_backup);
                }

                ErrorHandler.INSTANCE.addCannotInvokeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        var.getActualType().toString().toLowerCase(), id, params);
            }*/
            return OK;
        }

        SymbolClass class_accessed = (SymbolClass) var.getActualType();
        if (ctx.ID() != null) {
            Optional<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> member_entry =
                    class_accessed.getMemberIfExists(ctx.ID().toString());

            if (member_entry.isEmpty()) {
                ErrorHandler.INSTANCE.addDoesNotHaveFieldError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        class_accessed.getClassName(), ctx.ID().toString());
                return TYPE_ERROR;
            }

            SymbolVariable member = member_entry.get().getValue();

            if (member_entry.get().getKey().equals(SymbolModifier.PRIVATE) && !(((SymbolClass) currentClass.getCurrentMemberAccess().getActualType()).getClassName()).equals(currentClass.getClassName())) {
                ErrorHandler.INSTANCE.addMemberAccessError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        ctx.ID().toString(), class_accessed.getClassName());
                return TYPE_ERROR;
            }

            if (currentClass.currentlyGatheringArguments()) {
                currentClass.setCurrentArgVariable(member);
            }
            currentClass.setCurrentMemberAccess(member);
            return OK;
        }

        if (ctx.method_invocation() != null) {
            SymbolMethod backup = currentClass.getCurrentAccessedMethod();
            if (visitMethod_invocation(ctx.method_invocation()) != OK) {
                return TYPE_ERROR;
            }
            SymbolVariable ret_var = new SymbolVariable(currentClass.getCurrentAccessedMethod().getReturnValue().getType(), currentClass.getCurrentAccessedMethod().getReturnValue().getActualType(), "");
            if (currentClass.currentlyGatheringArguments()) {
                //currentClass.addArgument(var);
                currentClass.setCurrentArgVariable(ret_var);
            }
            currentClass.setCurrentAccessedMethod(backup);
            currentClass.setCurrentMemberAccess(ret_var);
            return OK;
        }

        // TODO: does this make sense?
        return visitChildren(ctx);
    }

    @Override public Integer visitMethod_invocation(JovaParser.Method_invocationContext ctx) {
        SymbolClass class_accessed = currentClass.getCurrentClassAccess();
        Collection<SymbolMethod> methods = class_accessed.getMatchingMethods(ctx.ID().toString());

        List<SymbolVariable> args_backup = currentClass.getCurrentArgList();
        currentClass.setArgList(new ArrayList<>());
        if (ctx.arg_list() != null) {
            int tmp = visitArg_list(ctx.arg_list());
            if (tmp != OK) {
                currentClass.setArgList(args_backup);
                return TYPE_ERROR;
            }
        }

        for (SymbolMethod method : methods) {
            if (method.checkValidArgList(currentClass.getCurrentArgList())) {
                int ret = TYPE_METHOD;

                if (currentClass.getCurrentMemberAccess() != null && method.getAccessSymbol().equals(SymbolModifier.PRIVATE)
                        && !(((SymbolClass) currentClass.getCurrentMemberAccess().getActualType()).getClassName()).equals(currentClass.getClassName())) {
                    ErrorHandler.INSTANCE.addMethodAccessError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                            method.getName(), class_accessed.getClassName(), method.getParamTypesAsString());
                    ret = TYPE_ERROR;
                }

                currentClass.setCurrentAccessedMethod(method);
                currentClass.currentSymbolVariable = method.getReturnValue();
                currentClass.setArgList(args_backup);
                return ret;
            }
        }

        String[] params = new String[0];
        if (ctx.arg_list() != null) {
            params = currentClass.getCurrentArgList().stream().map(SymbolVariable::getTypeAsString).toArray(String[]::new);
        }

        if (currentClass.getCurrentMemberAccess() != null) {
            ErrorHandler.INSTANCE.addCannotInvokeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    class_accessed.getClassName(), ctx.ID().toString(), params);
        }
        else {
            ErrorHandler.INSTANCE.addUndefMethodError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString(), params);
        }

        currentClass.setArgList(args_backup);
        return -1;
    }

    @Override public Integer visitId_expr(JovaParser.Id_exprContext ctx) {
        if (currentClass.currentlyGatheringArguments()) {
            if (ctx.ID() != null) {
                SymbolVariable var = currentClass.getCurrentScopeVariable(ctx.ID().toString());
                if (var == null) {
                    ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString());
                    return TYPE_ERROR;
                }

                if (ctx.member_access() == null || ctx.member_access().size() == 0) {
                    currentClass.setCurrentArgVariable(var);
                }
            }
        }

        if (ctx.method_invocation() != null) {
            SymbolMethod backup = currentClass.getCurrentAccessedMethod();
            if (visitMethod_invocation(ctx.method_invocation()) < OK) {
                return TYPE_ERROR;
            }
            currentClass.setCurrentAccessedMethod(backup);

            SymbolVariable var = new SymbolVariable(currentClass.getCurrentAccessedMethod().getReturnValue().getType(), currentClass.getCurrentAccessedMethod().getReturnValue().getActualType(), "");
            if (currentClass.currentlyGatheringArguments()) {
                currentClass.setCurrentArgVariable(var);
            }
            currentVar = var;
        }

        if (ctx.member_access() != null && ctx.member_access().size() != 0) {
            SymbolVariable var_accessed = null;
            if (ctx.KEY_THIS() != null) {
                var_accessed = new SymbolVariable(SymbolType.CLASS, currentClass, "");
            } else if (ctx.ID() != null) {
                var_accessed = currentClass.getCurrentScopeVariable(ctx.ID().toString());
                if (var_accessed == null) {
                    ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ID().toString());
                    return TYPE_ERROR;
                }
            } else if (ctx.method_invocation() != null) {
                var_accessed = currentVar;
            }

            SymbolVariable backup = currentClass.getCurrentMemberAccess();

            currentClass.setCurrentMemberAccess(var_accessed);
            int ret = 0;
            for (JovaParser.Member_accessContext expr : ctx.member_access()) {
                ret = visitMember_access(expr);
                if (ret < 0) {
                    return ret;
                }
            }
            currentVar = currentClass.getCurrentMemberAccess();
            currentClass.setCurrentMemberAccess(backup);

//            return ret;
        } else if (ctx.ID() != null) {
            currentVar = currentClass.getCurrentScopeVariable(ctx.ID().getText());
            currentClass.currentSymbolVariable = currentVar;
        } else if (ctx.KEY_THIS() != null) {
            currentVar = new SymbolVariable(SymbolType.CLASS, currentClass, "");
            currentClass.currentSymbolVariable = currentVar;
        }

        if (currentVar == null) {
            // variable cannot be assigned without being defined first
            ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), String.valueOf(ctx.ID().getText()));
            currentVar = null;
            return TYPE_ERROR;
        }
        // check variable type and reset current variable
        Object varType = currentVar.getActualType();

        // variable cannot be assigned without being defined first
        // ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), String.valueOf(ctx.ID().getText()));
        currentVar = null;
        return OK;
    }

    @Override public Integer visitArg_list(JovaParser.Arg_listContext ctx) {
        int ret = 0;
        SymbolVariable backup = currentClass.getCurrentArgVariable();
        currentClass.setCurrentArgVariable(null);
        for (JovaParser.ExprContext expr : ctx.expr()) {
            ret = visitExpr(expr);

            if (ret < 0) {
                currentClass.setCurrentArgVariable(backup);
                return ret;
            }

            if (currentClass.getCurrentArgVariable() != null) {
                currentClass.addArgument(currentClass.getCurrentArgVariable());
            }
        }
        currentClass.setCurrentArgVariable(backup);
        return ret;
    }

    @Override public Integer visitExpr(JovaParser.ExprContext ctx) {
        // TODO: Where do we need to throw errors in expressions?

        if (ctx.op != null) {
            // case: operation, check operand types
            Integer lhs_type = visit(ctx.left);
            SymbolVariable leftVariable = currentClass.currentSymbolVariable;

            Integer rhs_type = visit(ctx.right);
            SymbolVariable rightVariable = currentClass.currentSymbolVariable;

            if (lhs_type == TYPE_ERROR || rhs_type == TYPE_ERROR) {
                return TYPE_ERROR;
            }

            // check types are compatible
            SymbolVariable opReturnVar = CompatibilityCheckUtils.checkOperatorCompatibility(leftVariable, rightVariable, ctx, currentClass);
            if (opReturnVar == null){
                return TYPE_ERROR;
            }
            currentClass.currentSymbolVariable = opReturnVar;
            return OK;

        } else if(ctx.when != null){
            // case: ternary operator
            Integer whenType = visit(ctx.when);
            SymbolVariable whenVariable = currentClass.currentSymbolVariable;
            Integer thenType = visit(ctx.then);
            SymbolVariable thenVariable =currentClass.currentSymbolVariable;
            Integer elseType = visit(ctx.el);
            SymbolVariable elseVariable =currentClass.currentSymbolVariable;

            if (whenType == TYPE_ERROR || thenType == TYPE_ERROR || elseType == TYPE_ERROR) {
                return TYPE_ERROR;
            }

            SymbolVariable opReturnVar = CompatibilityCheckUtils.checkTernaryOperatorCompatibility(whenVariable, thenVariable, elseVariable, ctx, currentClass);
            if (opReturnVar == null){
                return TYPE_ERROR;
            }
            currentClass.currentSymbolVariable = opReturnVar;
        } else {
            // case: primary expression
            // currentSymbolVariable must be set within the primary expression
            Integer primVisitRet = visitChildren(ctx);

            if (primVisitRet == TYPE_ERROR){
                return TYPE_ERROR;
            }
        }

        return OK;
    }

    @Override public Integer visitUnary_expr(JovaParser.Unary_exprContext ctx) {
        Integer returnValue = visit(ctx.primary_expr());
        if (returnValue == TYPE_ERROR){
            return TYPE_ERROR;
        }
        SymbolVariable opReturnVar = CompatibilityCheckUtils.checkUnary(currentClass.currentSymbolVariable, ctx);
        if (opReturnVar == null){
            return TYPE_ERROR;
        }
        currentClass.currentSymbolVariable = opReturnVar;
        return OK;
    }

    @Override public Integer visitPrimary_expr(JovaParser.Primary_exprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitObject_alloc(JovaParser.Object_allocContext ctx) {
        String className = ctx.CLASS_TYPE().toString();

        if (SymbolClass.MAIN_CLASS_NAME.equals(className)) {
            ErrorHandler.INSTANCE.addMainInstatiationError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
            return ERROR_MAIN_INSTANTIATE;
        }

        // TODO this puts an undefined id error if class is not found => might be the the wrong error at this situation
        Optional<SymbolClass> correspondingClass = symbolTable.getClassByName(className, ctx);
        SymbolClass classObjectAlloc;

        if (correspondingClass.isEmpty()) {
            return ERROR_ID_UNDEF;
        } else {
            classObjectAlloc = correspondingClass.get();
            currentClass.setCurrentObjectAlloc(classObjectAlloc);
        }

        List<SymbolVariable> fetchedArgs;
        if (ctx.ctor_args() != null) {
            currentClass.setArgList(new ArrayList<>());
            visitCtor_args(ctx.ctor_args());
            fetchedArgs = currentClass.getCurrentArgList();
            currentClass.resetArgList();
        } else {
            // default constructor
            return OK;
        }


        Collection<SymbolConstructor> availableCtors = classObjectAlloc.getConstructors();

        for (SymbolConstructor ctor : availableCtors) {
            if (ctor.checkValidArgList(fetchedArgs)) {
                return OK;
            }
        }

        String[] params = fetchedArgs.stream().map(SymbolVariable::getTypeAsString).toArray(String[]::new);
        // TODO: Causes double error (see testUndeclFail03)
        ErrorHandler.INSTANCE.addUndefMethodError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                correspondingClass.get().getClassName(), params);

        return ERROR_UNKNOWN_CTOR;
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

        if (currentClass.currentlyGatheringArguments()) {
            SymbolPrimitiveType type = null; //SymbolPrimitiveType.NIX; TODO: support this
            if (ctx.BOOL_LIT() != null) {
                type = SymbolPrimitiveType.BOOL;
            } else if (ctx.INT_LIT() != null) {
                type = SymbolPrimitiveType.INT;
            } else if (ctx.STRING_LIT() != null) {
                type = SymbolPrimitiveType.STRING;
            }

            if (type != null) {
                currentClass.addPrimitiveArgument(type);
            }
            else {
                return -2;
            }

            return OK;
        }

        if (ctx.BOOL_LIT() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            return OK;
        } else if (ctx.STRING_LIT() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.STRING, "");
            return OK;
        } else if (ctx.INT_LIT() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "");
            return OK;
        } else if (ctx.KEY_NIX() != null){
            currentClass.currentSymbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.NIX, "");
            return OK;
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
        if (conditionType == TYPE_ERROR){
            return TYPE_ERROR;
        }

        SymbolVariable validCondition = currentClass.currentSymbolVariable;
        Integer condResult = CompatibilityCheckUtils.checkConditionCompatibility(validCondition, ctx.expr(), currentClass);

        if (condResult == TYPE_ERROR){
            return TYPE_ERROR;
        }

        return visitChildren(ctx);
    }

    @Override
    public Integer visitWhile_stmt(JovaParser.While_stmtContext ctx) {
        Integer conditionType = visit(ctx.expr());
        if (conditionType == TYPE_ERROR){
            return TYPE_ERROR;
        }

        SymbolVariable validCondition = currentClass.currentSymbolVariable;
        Integer condResult = CompatibilityCheckUtils.checkConditionCompatibility(validCondition, ctx.expr(), currentClass);

        if (condResult == TYPE_ERROR) {
            return TYPE_ERROR;
        }
        return visitChildren(ctx);
    }

}
