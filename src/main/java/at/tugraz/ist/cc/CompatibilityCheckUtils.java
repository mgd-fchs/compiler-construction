package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public final class CompatibilityCheckUtils {

    public static final int TYPE_INT = SymbolPrimitiveType.INT.getValue();
    public static final int TYPE_STR = SymbolPrimitiveType.STRING.getValue();
    public static final int TYPE_BOOL = SymbolPrimitiveType.BOOL.getValue();
    public static final int TYPE_CLASS = SymbolType.CLASS.getValue();
    public static final int TYPE_PRIMITIVE = SymbolType.PRIMITIVE.getValue();
    public static final int TYPE_METHOD = SymbolType.METHOD.getValue();
    public static final int TYPE_NIX = SymbolPrimitiveType.NIX.getValue();
    ;
    private static final int TYPE_ERROR = -30;
    private static final int OK = 0;
    public static SymbolVariable currentMember = null;

    private CompatibilityCheckUtils() {
    }

    public static Integer checkOperatorCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx, SymbolClass currentClass) {

        //TODO: check equal or unequal for class and nix types
        // if operator is handed a method invocation
        SymbolVariable lhsRetVar = null;
        SymbolVariable rhsRetVar = null;
        String lhs_typestr = getTypeStr(lhs_type, ctx.left.start.getText(), currentClass);
        String rhs_typestr = getTypeStr(rhs_type, ctx.right.start.getText(), currentClass);

        if (lhs_type == TYPE_METHOD || rhs_type ==TYPE_METHOD) {
            if (lhs_type == TYPE_METHOD) {
                String leftID = ctx.left.start.getText();
                lhsRetVar = getMethodReturnType(currentClass, leftID);

                if (lhsRetVar.getType().getValue() == TYPE_CLASS){
                    lhs_type = TYPE_CLASS;
                } else if (lhsRetVar.getType().getValue() == TYPE_PRIMITIVE){
                    lhs_type = ((SymbolPrimitiveType) lhsRetVar.getActualType()).getValue();
                    lhs_typestr = SymbolPrimitiveType.valueOf(lhs_type).toString();
                }
            }

            if (rhs_type == TYPE_METHOD) {
                String rightID = ctx.left.start.getText();
                rhsRetVar = getMethodReturnType(currentClass, rightID);

                if (rhsRetVar.getType().getValue() == TYPE_CLASS){
                    rhs_type = TYPE_CLASS;
                } else if (rhsRetVar.getType().getValue() == TYPE_PRIMITIVE){
                    rhs_type = ((SymbolPrimitiveType) rhsRetVar.getActualType()).getValue();
                    rhs_typestr = SymbolPrimitiveType.valueOf(rhs_type).toString();
                }
            }
        }
        // arithmetic and relational operations

        if (lhs_type == TYPE_CLASS || rhs_type == TYPE_CLASS){
            return checkRelOpClass(ctx, currentClass);
        }

        if (ctx.ADDOP() != null || ctx.MULOP() != null || ctx.RELOP() != null) {
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
        else if (ctx.AND() != null || ctx.OR() != null) {
            if (lhs_type == TYPE_BOOL && rhs_type == TYPE_BOOL) {
                return SymbolPrimitiveType.BOOL.getValue();
            } else if ((lhs_type == TYPE_BOOL || lhs_type == TYPE_INT) && (rhs_type == TYPE_BOOL || rhs_type == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhs_typestr, rhs_typestr, "bool", "bool");
                return SymbolPrimitiveType.BOOL.getValue();
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhs_typestr, rhs_typestr, ctx.op.getText());
                return TYPE_ERROR;
            }
        } else {
            return TYPE_ERROR;
        }
    }

    public static Integer checkRelOpClass(JovaParser.ExprContext ctx, SymbolClass currentClass){
        String lhsTypestr;
        String rhsTypestr;
        Integer lhsType;
        Integer rhsType;

        if (currentClass.getCurrentCallable() != null) {
            lhsTypestr = currentClass.getCurrentCallable().getLocalVariableById(ctx.left.start.getText()).getTypeAsString();
            rhsTypestr = currentClass.getCurrentCallable().getLocalVariableById(ctx.right.start.getText()).getTypeAsString();
            lhsType = currentClass.getCurrentCallable().getLocalVariableById(ctx.left.start.getText()).getType().getValue();
            rhsType = currentClass.getCurrentCallable().getLocalVariableById(ctx.right.start.getText()).getType().getValue();

        } else {
            lhsTypestr = currentClass.getCurrentScopeVariable(ctx.left.start.getText()).getTypeAsString();
            rhsTypestr = currentClass.getCurrentScopeVariable(ctx.right.start.getText()).getTypeAsString();

            lhsType = currentClass.getCurrentScopeVariable(ctx.left.start.getText()).getType().getValue();
            rhsType = currentClass.getCurrentScopeVariable(ctx.right.start.getText()).getType().getValue();
        }

        // both operands must be class type
        if (lhsType != rhsType){
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsTypestr, rhsTypestr, ctx.op.getText());
            return TYPE_ERROR;
        }

        // only operator allowed between class types is equals or unequals
        if (ctx.op.getText() == "==" || ctx.op.getText() == "!="){
            return TYPE_BOOL;
        } else {
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsTypestr, rhsTypestr, ctx.op.getText());
            return TYPE_ERROR;
        }
    }

    public static Integer checkExpressionAssignment(Integer exprReturnValue, String assignedID, SymbolClass currentClass, JovaParser.Assign_stmtContext ctx){
        // TODO: Error messages show incorrect types
        // TODO: Add coercion warnings

        SymbolVariable assignedVariable = currentClass.getCurrentCallable().getLocalVariableById(assignedID);

        if (assignedVariable == null){
            ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), assignedID);
            return TYPE_ERROR;
        }

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
        } else if (exprReturnValue == TYPE_METHOD){
            String exprID = ctx.ass.start.getText();
            SymbolVariable returnValue = getMethodReturnType(currentClass, exprID);
            if (returnValue.getType().getValue() == TYPE_CLASS){
                if (returnValue.getActualType() == assignedVariable.getActualType()){
                    return OK;
                } else {
                    String actualReturnString = returnValue.getTypeAsString();
                    ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnString);
                }
            } else if (returnValue.getType().getValue() == TYPE_PRIMITIVE){
                if (returnValue.getActualType() == assignedVariable.getActualType()) {
                    return OK;
                } else {
                    String actualReturnString = returnValue.getTypeAsString();
                    ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnString);
                    return TYPE_ERROR;
                }
            }

        }// case: expression is an id_expression

        // case: expression is an object allocation
        return OK;
    }

    public static Integer checkReturnValue(Integer actualReturnValue, SymbolClass currentClass, JovaParser.Ret_stmtContext ctx) {
        String actualReturnString = null;
        SymbolVariable actualSymbolType = null;

        // actual return value
        if (SymbolPrimitiveType.valueOf(actualReturnValue) != null) {
            if (actualReturnValue == TYPE_NIX) {
                return actualReturnValue;
            }
            actualReturnString = SymbolPrimitiveType.valueOf(actualReturnValue).toString();
        } else if (actualReturnValue == SymbolType.CLASS.getValue()) {
            actualReturnString = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getTypeAsString();
            actualSymbolType = currentClass.getCurrentScopeVariable(ctx.retval.start.getText());
        } else if (actualReturnValue == SymbolType.METHOD.getValue()) {
            actualSymbolType = getMethodReturnType(currentClass, "id");
        } else if (currentMember != null) {
            Integer retVal = checkReturnValueMember(currentClass, ctx, currentMember);
            currentMember = null;
            return retVal;
        } else {
            return TYPE_ERROR;
        }

        SymbolVariable expectedReturnType = getExpectedReturnType(currentClass, ctx);
        if (expectedReturnType.getType().getValue() == TYPE_CLASS) {
            String expectedClassName = ((SymbolClass) expectedReturnType.getActualType()).getClassName();
            if (expectedClassName == actualReturnString) {
                return OK;
            } else {
                return checkReturnValueCoercion(actualReturnValue, expectedReturnType.getType().getValue(), ctx, actualReturnString);
            }
        } else if (expectedReturnType.getType().getValue() == TYPE_PRIMITIVE) {
            if (((SymbolPrimitiveType) expectedReturnType.getActualType()).getValue() == actualReturnValue) {
                return actualReturnValue;
            } else {
                return checkReturnValueCoercion(actualReturnValue,((SymbolPrimitiveType) expectedReturnType.getActualType()).getValue(), ctx, actualReturnString);
            }
        } else {
            return TYPE_ERROR;
        }
    }

    private static String getTypeStr(Integer typeInt, String id, SymbolClass currentClass) {
        String typeStr = null;

        if (typeInt == TYPE_CLASS) {
            if (currentClass.getCurrentCallable() != null) {
                typeStr = currentClass.getCurrentCallable().getLocalVariableById(id).getTypeAsString();
            } else {
                typeStr = currentClass.getCurrentScopeVariable(id).getTypeAsString();
            }
        } else if (typeInt == TYPE_PRIMITIVE) {
            typeStr = SymbolPrimitiveType.valueOf(typeInt).toString().toLowerCase();
        } else {
            return null;
        }
        return typeStr;
    }

    public static Integer checkTernaryOperatorCompatibility(Integer whenType, Integer thenType, Integer elseType, JovaParser.ExprContext ctx, SymbolClass currentClass) {
        // TODO: Check error messages for ternary operator
        // TODO: Add some test cases (incl. nix-type!)
        // check condition
        if (whenType != TYPE_BOOL && whenType != TYPE_INT) {
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), "?");
            return TYPE_ERROR;
        } else if (whenType == TYPE_INT) {
            ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), "?", SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), SymbolPrimitiveType.valueOf(whenType).toString().toLowerCase(), "bool", "bool");
            return SymbolPrimitiveType.BOOL.getValue();
        } else {
            return SymbolPrimitiveType.BOOL.getValue();

        }
    }

    public static Integer checkConditionCompatibility(Integer condition_type, JovaParser.ExprContext ctx, SymbolClass currentClass) {
        String typeString = getTypeStr(condition_type, ctx.start.getText(), currentClass);
        if (condition_type != TYPE_BOOL && condition_type != TYPE_INT) {
            ErrorHandler.INSTANCE.addIncompatibleCondTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), typeString);
            return TYPE_ERROR;
        } else if (condition_type == TYPE_INT) {
            ErrorHandler.INSTANCE.addConditionTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), typeString, "bool");
            return SymbolPrimitiveType.BOOL.getValue();
        } else {
            return SymbolPrimitiveType.BOOL.getValue();
        }
    }

    private static SymbolVariable getExpectedReturnType(SymbolClass currentClass, JovaParser.Ret_stmtContext ctx) {
        SymbolVariable expectedType;

        // expected return value
        if (currentClass.getCurrentAccessedMethod() != null){
         expectedType = currentClass.getCurrentAccessedMethod().getReturnValue();
        } else {
            expectedType = null;
        }
        return expectedType;
    }

    private static Integer checkReturnValueMember(SymbolClass currentClass, JovaParser.Ret_stmtContext ctx, SymbolVariable returnedMember) {
        // TODO: Handle method members (see "incompatible_return/fail04.jova")
        // get expected type
        SymbolVariable expectedReturnType = getExpectedReturnType(currentClass, ctx);

        // get actual type
        if (returnedMember.getType() == SymbolType.PRIMITIVE) {
            Integer actualType =  ((SymbolPrimitiveType) returnedMember.getActualType()).getValue();
            if (expectedReturnType.getType().getValue() == TYPE_PRIMITIVE) {
                Integer expType = ((SymbolPrimitiveType) expectedReturnType.getActualType()).getValue();
                if(expType == actualType){
                    return actualType;
                } else {
                    return checkReturnValueCoercion(actualType, expType, ctx, SymbolPrimitiveType.valueOf(actualType).toString());
                }
            } else {
                if (expectedReturnType.getType().getValue() == TYPE_PRIMITIVE) {
                    Integer intRetType = (Integer) expectedReturnType.getActualType();
                    return checkReturnValueCoercion(actualType, intRetType, ctx, SymbolPrimitiveType.valueOf(actualType).toString());
                } else if (expectedReturnType.getType().getValue() == TYPE_CLASS) {
                    return checkReturnValueCoercion(actualType, TYPE_CLASS, ctx, returnedMember.getName());
                } else {
                    // should be unreachable
                    return TYPE_ERROR;
                }
            }
        } else if (returnedMember.getType() == SymbolType.CLASS) {
            SymbolVariable actualSymbolType = (SymbolVariable) returnedMember;
            if (expectedReturnType.getType().getValue() == TYPE_PRIMITIVE){
                ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualSymbolType.getTypeAsString());
                return TYPE_ERROR;
            }
            if (actualSymbolType != null && (expectedReturnType.getActualType().getClass() != actualSymbolType.getActualType().getClass())) {
                return checkReturnValueCoercion(returnedMember.getType().getValue(), returnedMember.getType().getValue(), ctx, returnedMember.getTypeAsString());
            } else {
                return returnedMember.getType().getValue();
            }
        }
        return OK;
    }

    private static Integer checkReturnValueCoercion(Integer actualValue, Integer expectedValue, JovaParser.Ret_stmtContext ctx, String actualReturnString) {
        SymbolPrimitiveType actualType = SymbolPrimitiveType.valueOf(actualValue);
        SymbolPrimitiveType expectedType = SymbolPrimitiveType.valueOf(expectedValue);
        Integer returnValue;

        if (actualType == SymbolPrimitiveType.BOOL || actualType == SymbolPrimitiveType.INT) {
            if (expectedType == SymbolPrimitiveType.BOOL || expectedType == SymbolPrimitiveType.INT) {
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

    private static SymbolVariable getMethodReturnType(SymbolClass currentClass, String id) {
        SymbolVariable methodReturnVariable;
        // TODO: Get methods by ID & signature
        methodReturnVariable = currentClass.getCurrentAccessedMethod().getReturnValue();
        return methodReturnVariable;
    }
}
