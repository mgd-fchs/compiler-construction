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
    private static final int TYPE_ERROR = -30;
    public static final int TYPE_NIX = 44;

    private CompatibilityCheckUtils(){
    }
    // TODO @all Discuss if this should be part of the symbol table

    public static Integer checkOperatorCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx){

        String lhs_typestr = getTypeStr(lhs_type);
        String rhs_typestr = getTypeStr(rhs_type);

        //TODO: check equal or unequal for class and nix types

        // arithmetic and relational operations
        if (ctx.ADDOP() != null || ctx.MULOP() != null || ctx.RELOP() != null){
            if (lhs_type == TYPE_INT && rhs_type == TYPE_INT) {
                return SymbolPrimitiveType.INT.getValue();
            } else if ((lhs_type == TYPE_BOOL || lhs_type == TYPE_INT) && (rhs_type == TYPE_BOOL || rhs_type == TYPE_INT)){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhs_typestr, rhs_typestr, "int", "int");
                return SymbolPrimitiveType.INT.getValue();
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhs_typestr, rhs_typestr, ctx.op.getText());
                return TYPE_ERROR;
            }
        }

        // logical operations
        if (ctx.AND() != null || ctx.OR() != null){
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

    private static String getTypeStr(Integer type_int) {
        if (type_int == TYPE_CLASS){
            // TODO: Do we need to return the actual class type here?
            return "Ctype";
        }
        else {
            String type_str = SymbolPrimitiveType.valueOf(type_int).toString().toLowerCase();
            return type_str;
        }
    }

    public static Integer checkTernaryOperatorCompatibility(Integer whenType, Integer thenType, Integer elseType, JovaParser.ExprContext ctx){
        // TODO: Check error messages for ternary operator
        // TODO: Ensure then/else resolve to correct type
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

    public static Integer checkConditionCompatibility(Integer condition_type, JovaParser.ExprContext ctx){
        if (condition_type != TYPE_BOOL && condition_type != TYPE_INT) {
            ErrorHandler.INSTANCE.addIncompatibleCondTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), SymbolPrimitiveType.valueOf(condition_type).toString().toLowerCase());
            return TYPE_ERROR;
        } else if (condition_type == TYPE_INT){
            ErrorHandler.INSTANCE.addConditionTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), SymbolPrimitiveType.valueOf(condition_type).toString().toLowerCase(), "bool");
            return SymbolPrimitiveType.BOOL.getValue();
        } else {
            return SymbolPrimitiveType.BOOL.getValue();

        }
    }

    public static Integer checkReturnValue(Integer actualReturnValue, SymbolClass currentClass, JovaParser.Ret_stmtContext ctx){
        String actualReturnString;
        SymbolType actualSymbolType = null;

        // actual return value
        if (SymbolPrimitiveType.valueOf(actualReturnValue) != null) {
            actualReturnString = SymbolPrimitiveType.valueOf(actualReturnValue).toString();
        } else if (currentClass.getCurrentScopeVariable(ctx.retval.start.getText()) != null) {
            actualReturnString = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getTypeAsString();
            actualSymbolType = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getType();
        } else {
            ErrorHandler.INSTANCE.addUndefIdError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.retval.start.getText());
            return TYPE_ERROR;
        }

        // expected return value
        if (currentClass.getCurrentAccessedMethod().getReturnValue().getActualType() instanceof SymbolPrimitiveType) {
            Integer expectedValue = ((SymbolPrimitiveType) currentClass.getCurrentAccessedMethod().getReturnValue().getActualType()).getValue();

            if (expectedValue == actualReturnValue) {
                return actualReturnValue;
            } else {
                return checkReturnValueCoercion(actualReturnValue, expectedValue, ctx, actualReturnString);
            }
        } else {
            SymbolType expectedSymbolType = currentClass.getCurrentScopeVariable(ctx.retval.start.getText()).getType();
            if (actualSymbolType != null && expectedSymbolType != actualSymbolType){
                return checkReturnValueCoercion(actualReturnValue, expectedSymbolType.getValue(), ctx, actualReturnString);
            } else {
                return actualReturnValue;
            }

        }
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
}
