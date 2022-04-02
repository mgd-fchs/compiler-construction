package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

public final class CompatibilityCheckUtils {

    public static final SymbolPrimitiveType TYPE_INT = SymbolPrimitiveType.INT;
    public static final SymbolPrimitiveType TYPE_STR = SymbolPrimitiveType.STRING;
    public static final SymbolPrimitiveType TYPE_BOOL = SymbolPrimitiveType.BOOL;
    public static final SymbolPrimitiveType TYPE_NIX = SymbolPrimitiveType.NIX;
    public static final SymbolType TYPE_CLASS = SymbolType.CLASS;
    public static final SymbolType TYPE_PRIMITIVE = SymbolType.PRIMITIVE;
    public static final SymbolType TYPE_METHOD = SymbolType.METHOD;

    private static final int TYPE_ERROR = -30;
    private static final int OK = 0;
    public static SymbolVariable currentMember = null;

    private CompatibilityCheckUtils() {
    }

    public static SymbolVariable checkOperatorCompatibility(SymbolVariable lhsVar, SymbolVariable rhsVar, JovaParser.ExprContext ctx, SymbolClass currentClass) {

        // arithmetic and relational operations
        if (lhsVar.getType() == TYPE_CLASS || rhsVar.getType() == TYPE_CLASS){
            // TODO @Magdi: Test me!
            return checkRelOpClass(lhsVar, rhsVar, ctx, currentClass);
        }

        if (lhsVar.getActualType() == TYPE_STR || rhsVar.getActualType() == TYPE_STR || lhsVar.getActualType() == TYPE_NIX || rhsVar.getActualType() == TYPE_NIX){
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
            return null;
        }

        if (ctx.ADDOP() != null || ctx.MULOP() != null) {
            if (lhsVar.getActualType() == TYPE_INT && rhsVar.getActualType() == TYPE_INT) {
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "");
            } else if ((lhsVar.getActualType() == TYPE_BOOL || lhsVar.getActualType() == TYPE_INT) && (lhsVar.getActualType() == TYPE_BOOL || lhsVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), "int", "int");
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "");
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
                return null;
            }
        }
        if (ctx.RELOP() != null){
            if (lhsVar.getActualType() == TYPE_INT && rhsVar.getActualType() == TYPE_INT) {
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            } else if ((lhsVar.getActualType() == TYPE_BOOL || lhsVar.getActualType() == TYPE_INT) && (lhsVar.getActualType() == TYPE_BOOL || lhsVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), "int", "int");
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
                return null;
            }
        }

        // logical operations
        else if (ctx.AND() != null || ctx.OR() != null) {
            if (lhsVar.getActualType() == TYPE_BOOL && rhsVar.getActualType() == TYPE_BOOL) {
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            } else if ((lhsVar.getActualType() == TYPE_BOOL || lhsVar.getActualType() == TYPE_INT) && (rhsVar.getActualType() == TYPE_BOOL || rhsVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), "bool", "bool");
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
                return null;
            }
        } else {
            return null;
        }
    }

    public static SymbolVariable checkRelOpClass(SymbolVariable lhsVar, SymbolVariable rhsVar, JovaParser.ExprContext ctx, SymbolClass currentClass){

        if ((lhsVar.getActualType() == TYPE_NIX || lhsVar.getType() == TYPE_CLASS) && (rhsVar.getActualType() == TYPE_NIX || rhsVar.getType() == TYPE_CLASS)){
            if (ctx.op.getText() != "==" && ctx.op.getText() != "!=") {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
                return null;
            }
        } else {
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
            return null;
        }
        return new SymbolVariable(TYPE_PRIMITIVE, TYPE_BOOL, "");
    }

    public static Integer checkExpressionAssignment(SymbolVariable shouldVar, SymbolVariable isVar, JovaParser.Assign_stmtContext ctx, SymbolClass currentClass){
        // TODO: Assignment should throw incompatible operator error ("=")

        // case: expression returns primitive type
        if (shouldVar.getActualType() !=  isVar.getActualType()) {
            if ((shouldVar.getActualType() == TYPE_BOOL || shouldVar.getActualType() == TYPE_INT) && (isVar.getActualType() == TYPE_BOOL || isVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addReturnTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), isVar.getTypeAsString(), shouldVar.getTypeAsString());
                return OK;
            }
            ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), isVar.getTypeAsString());
            return TYPE_ERROR;
        }
        return OK;
    }

    public static Integer checkReturnValue(SymbolVariable actualReturnValue, SymbolClass currentClass, JovaParser.Ret_stmtContext ctx) {

        if (actualReturnValue.getActualType() == TYPE_NIX) {
            return OK;
        }

        SymbolVariable expectedReturnType = currentClass.getCurrentCallable().getReturnValue(); //TODO: Get correct method!

        if (expectedReturnType == null || expectedReturnType.getActualType() == actualReturnValue.getActualType()) {
            return OK;
        } else if ((actualReturnValue.getActualType() == TYPE_BOOL || actualReturnValue.getActualType() == TYPE_INT) && (expectedReturnType.getActualType() == TYPE_BOOL || expectedReturnType.getActualType() == TYPE_INT)) {
            ErrorHandler.INSTANCE.addReturnTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnValue.getTypeAsString(), expectedReturnType.getTypeAsString());
            return OK;
        }
        else {
            ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnValue.getTypeAsString());
            return TYPE_ERROR;
        }
    }

    public static SymbolVariable checkTernaryOperatorCompatibility(SymbolVariable whenType, SymbolVariable thenType, SymbolVariable elseType, JovaParser.ExprContext ctx, SymbolClass currentClass) {
        // TODO: Check error messages for ternary operator
        // TODO: Reconsider return value + do we need to check if else and then type are the same?
        // TODO: Add some test cases (incl. nix-type!)
        // check condition
        if (whenType.getActualType() != TYPE_BOOL && whenType.getActualType() != TYPE_INT) {
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), whenType.getTypeAsString(), thenType.getTypeAsString(), "?");
            return null;
        } else if (whenType.getActualType() == TYPE_INT) {
            ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), "?", whenType.getTypeAsString(), thenType.getTypeAsString(), "bool", "bool");
            return thenType;
        } else {
            return thenType;
        }
    }

    public static Integer checkConditionCompatibility(SymbolVariable condition_type, JovaParser.ExprContext ctx, SymbolClass currentClass) {

        if (condition_type.getActualType() != TYPE_BOOL && condition_type.getActualType() != TYPE_INT) {
            ErrorHandler.INSTANCE.addIncompatibleCondTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), condition_type.getTypeAsString());
            return TYPE_ERROR;
        } else if (condition_type.getActualType() == TYPE_INT) {
            ErrorHandler.INSTANCE.addConditionTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), condition_type.getTypeAsString(), "bool");
            return OK;
        } else {
            return OK;
        }
    }

    public static SymbolVariable checkUnary(SymbolVariable unaryVar, JovaParser.Unary_exprContext ctx){
        if (ctx.ADDOP() != null){
            if (unaryVar.getActualType() != TYPE_INT){
                if (unaryVar.getActualType() == TYPE_BOOL){
                    ErrorHandler.INSTANCE.addUnaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.ADDOP().getText(), unaryVar.getTypeAsString(), TYPE_INT.toString());
                    return new SymbolVariable(TYPE_PRIMITIVE, TYPE_INT, "");
                } else {
                    ErrorHandler.INSTANCE.addUnaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), unaryVar.getTypeAsString(),ctx.ADDOP().getText());
                }
            } else {
                return new SymbolVariable(TYPE_PRIMITIVE, TYPE_INT, "");
            }
        }
        if (ctx.NOT()!= null){
            if (unaryVar.getActualType() != TYPE_BOOL){
                if (unaryVar.getActualType() == TYPE_INT){
                    ErrorHandler.INSTANCE.addUnaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.NOT().getText(), unaryVar.getTypeAsString(), TYPE_BOOL.toString());
                    return new SymbolVariable(TYPE_PRIMITIVE, TYPE_INT, "");
                } else {
                    ErrorHandler.INSTANCE.addUnaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), unaryVar.getTypeAsString(),ctx.NOT().getText());
                }
            } else {
                return new SymbolVariable(TYPE_PRIMITIVE, TYPE_BOOL, "");
            }
        }

        return null;
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
        methodReturnVariable = currentClass.getCurrentCallable().getReturnValue();
        return methodReturnVariable;
    }
}
