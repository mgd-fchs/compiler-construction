package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.ArrayList;
import java.util.List;

public final class CompatibilityCheckUtils {

    public static final int TYPE_INT = SymbolPrimitiveType.INT.getValue();
    public static final int TYPE_STR = SymbolPrimitiveType.STRING.getValue();
    public static final int TYPE_BOOL = SymbolPrimitiveType.BOOL.getValue();
    public static final int TYPE_CLASS = SymbolType.CLASS.getValue();
    public static final int TYPE_NIX = SymbolPrimitiveType.NIX.getValue();;
    private static final int TYPE_ERROR = -30;
    private static final int OK = 0;
    public static SymbolVariable currentMember = null;

    private CompatibilityCheckUtils(){
    }

    public static Integer checkOperatorCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx, SymbolClass currentClass){

        //TODO: check equal or unequal for class and nix types
        // if operator is handed a method invocation
        if (SymbolType.valueOf(lhs_type) == null && SymbolPrimitiveType.valueOf(lhs_type) == null){
            String leftID = ctx.left.start.getText();
            lhs_type = getMethodReturnPrimitiveType(currentClass, leftID);
        }

        if (SymbolType.valueOf(rhs_type) == null && SymbolPrimitiveType.valueOf(rhs_type) == null){
            String rightID = ctx.right.start.getText();
            rhs_type = getMethodReturnPrimitiveType(currentClass, rightID);
        }

        // arithmetic and relational operations
        String lhs_typestr = getTypeStr(lhs_type, ctx.left.start.getText(), currentClass);
        String rhs_typestr = getTypeStr(rhs_type, ctx.right.start.getText(), currentClass);

        if (ctx.ADDOP() != null || ctx.MULOP() != null || ctx.RELOP() != null){
            if (lhs_type == TYPE_INT && rhs_type == TYPE_INT) {
                return SymbolPrimitiveType.INT.getValue();
            } else if ((lhs_type == TYPE_BOOL || lhs_type == TYPE_INT) && (rhs_type == TYPE_BOOL || rhs_type == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhs_typestr, rhs_typestr, "int", "int");
                return SymbolPrimitiveType.INT.getValue();
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhs_typestr, rhs_typestr, ctx.op.getText());
                return TYPE_ERROR;
            }
        }

        // logical operations
        else if (ctx.AND() != null || ctx.OR() != null){
            if (lhs_type == TYPE_BOOL && rhs_type == TYPE_BOOL){
                return SymbolPrimitiveType.BOOL.getValue();
            } else if ((lhs_type == TYPE_BOOL || lhs_type == TYPE_INT) && (rhs_type == TYPE_BOOL || rhs_type == TYPE_INT)){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhs_typestr, rhs_typestr, "bool", "bool");
                return SymbolPrimitiveType.BOOL.getValue();
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhs_typestr, rhs_typestr, ctx.op.getText());
                return TYPE_ERROR;
            }
        }

        else {
            return TYPE_ERROR;
        }
    }

    private static String getTypeStr(Integer typeInt, String id, SymbolClass currentClass) {
        String typeStr;

        if (typeInt == TYPE_CLASS){
            if (currentClass.getCurrentCallable() != null) {
                typeStr = currentClass.getCurrentCallable().getLocalVariableById(id).getTypeAsString();
            } else {
                typeStr = currentClass.getCurrentScopeVariable(id).getTypeAsString();
            }
        }
        else {
            typeStr = SymbolPrimitiveType.valueOf(typeInt).toString().toLowerCase();
        }
        return typeStr;
    }

    public static Integer checkTernaryOperatorCompatibility(Integer whenType, Integer thenType, Integer elseType, JovaParser.ExprContext ctx, SymbolClass currentClass){
        // TODO: Check error messages for ternary operator
        // TODO: Add some test cases (incl. nix-type!)
        // check condition
        if (whenType != TYPE_BOOL && whenType != TYPE_INT) {
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), "?");
            return TYPE_ERROR;
        } else if (whenType == TYPE_INT){
            ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), "?", SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), "bool", "bool");
            return SymbolPrimitiveType.BOOL.getValue();
        } else {
            return SymbolPrimitiveType.BOOL.getValue();

        }
    }

    public static Integer checkConditionCompatibility(Integer condition_type, JovaParser.ExprContext ctx, SymbolClass currentClass){
        String typeString = getTypeStr(condition_type, ctx.start.getText(), currentClass);
        if (condition_type != TYPE_BOOL && condition_type != TYPE_INT) {
            ErrorHandler.INSTANCE.addIncompatibleCondTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), typeString);
            return TYPE_ERROR;
        } else if (condition_type == TYPE_INT){
            ErrorHandler.INSTANCE.addConditionTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), typeString, "bool");
            return SymbolPrimitiveType.BOOL.getValue();
        } else {
            return SymbolPrimitiveType.BOOL.getValue();
        }
    }

    public static Integer checkReturnValue(Integer actualReturnValue, SymbolClass currentClass, JovaParser.Ret_stmtContext ctx) {
        String actualReturnString = null;
        SymbolType actualSymbolType = null;

        // actual return value
        if (SymbolPrimitiveType.valueOf(actualReturnValue) != null) {
            if (actualReturnValue == TYPE_NIX) {
                return actualReturnValue;
            }
            actualReturnString = SymbolPrimitiveType.valueOf(actualReturnValue).toString();
        } else if (SymbolType.valueOf(actualReturnValue) != null) {
            actualReturnString = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getTypeAsString();
            actualSymbolType = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getType();
        } else if (currentMember != null) {
            checkReturnValueMember(currentClass, ctx, currentMember);
            currentMember = null;
        } else {
            return TYPE_ERROR;
        }

        Object expectedReturnType = getExpectedReturnType(currentClass, ctx);
        if (expectedReturnType instanceof SymbolVariable){ // use actualSymbolType instead
            if(expectedReturnType.equals(actualReturnValue)){
                return OK;
            } else {
                return checkReturnValueCoercion(actualReturnValue, ((SymbolType) expectedReturnType).getValue(), ctx, actualReturnString);
            }
        } else if (expectedReturnType instanceof Integer){
            if(expectedReturnType == actualReturnValue){
                return actualReturnValue;
            } else {
                return checkReturnValueCoercion(actualReturnValue, ((Integer) expectedReturnType), ctx, actualReturnString);
            }
        } else {
            return TYPE_ERROR;
        }
    }

    private static Object getExpectedReturnType(SymbolClass currentClass, JovaParser.Ret_stmtContext ctx){
        SymbolType expectedType;
        Integer expectedValue;

        // expected return value
        if (currentClass.getCurrentAccessedMethod().getReturnValue().getActualType() instanceof SymbolPrimitiveType) {
            expectedValue = ((SymbolPrimitiveType) currentClass.getCurrentAccessedMethod().getReturnValue().getActualType()).getValue();
            return expectedValue;
        } else {
            expectedType = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getType();
            return expectedType;
        }
    }

    private static Object checkReturnValueMember(SymbolClass currentClass, JovaParser.Ret_stmtContext ctx, SymbolVariable returnedMember){
       /* get expected type

        Object expectedReturnType = getExpectedReturnType(currentClass, ctx);

        // get actual type

        if (currentClass.getCurrentAccessedMethod().getReturnValue().getActualType() instanceof SymbolPrimitiveType) {
            Integer expectedValue = ((SymbolPrimitiveType) currentClass.getCurrentAccessedMethod().getReturnValue().getActualType()).getValue();

        } else {
            SymbolType expectedSymbolType = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getType();
            if (actualSymbolType != null && expectedSymbolType != actualSymbolType) {
                return checkReturnValueCoercion(actualReturnValue, expectedSymbolType.getValue(), ctx, actualReturnString);
            } else {
                return actualReturnValue;
            }

        }
        return expectedValue;
        */
        return OK;
    }

    private static Integer checkReturnValueCoercion(Integer actualValue, Integer expectedValue, JovaParser.Ret_stmtContext ctx, String actualReturnString){
        SymbolPrimitiveType actualType = SymbolPrimitiveType.valueOf(actualValue);
        SymbolPrimitiveType expectedType = SymbolPrimitiveType.valueOf(expectedValue);
        Integer returnValue;

        if (actualType == SymbolPrimitiveType.BOOL || actualType == SymbolPrimitiveType.INT){
            if (expectedType == SymbolPrimitiveType.BOOL || expectedType == SymbolPrimitiveType.INT){
                ErrorHandler.INSTANCE.addReturnTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualType.toString(), expectedType.toString());
                returnValue = expectedValue;
            } else {
                ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnString);
                returnValue = TYPE_ERROR;
            }
        } else {
            ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnString);
            returnValue = TYPE_ERROR;
        }

        return returnValue;
    }

    private static int getMethodReturnPrimitiveType(SymbolClass currentClass, String id){
        SymbolVariable methodRetVal;
        Integer primType = null;
        methodRetVal = currentClass.getMethodReturnValueById(id);
        if (methodRetVal.getActualType() instanceof SymbolPrimitiveType){
            primType = ((SymbolPrimitiveType) methodRetVal.getActualType()).getValue();
        }
        return primType;
    }

    private static SymbolVariable getMethodReturnClassType(SymbolClass currentClass, String id){
        SymbolVariable methodRetVal;
        methodRetVal = currentClass.getMethodReturnValueById(id);
        if (methodRetVal != null){
            return methodRetVal;
        } else {
            // TODO: Add error here
            return null;
        }
    }

    public static int checkExpressionAssginment(Integer exprReturnValue, String assignedID, SymbolClass currentClass, JovaParser.Assign_stmtContext ctx){
        SymbolVariable assignedVariable = currentClass.getCurrentCallable().getLocalVariableById(assignedID);

        // case: expression returns primitive type
        if (SymbolPrimitiveType.valueOf(exprReturnValue) != null) {
            if (assignedVariable.getActualType() instanceof SymbolPrimitiveType){
                if(((SymbolPrimitiveType) assignedVariable.getActualType()).getValue() == exprReturnValue){
                    return OK;
                } else {
                    // TODO: find out which error should be thrown here
                    String actualReturnString = getTypeStr(exprReturnValue, assignedID, currentClass);
                    ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnString);
                    return TYPE_ERROR;
                }
            }
            // case: expression is a function call
        } else if (SymbolType.valueOf(exprReturnValue) == null && SymbolPrimitiveType.valueOf(exprReturnValue) == null){
            String exprID = ctx.ass.start.getText();
            exprReturnValue = getMethodReturnPrimitiveType(currentClass, exprID);

        }// case: expression is an id_expression

        // case: expression is an object allocation
        return OK;
    }
}
