package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;

public final class CompatibilityCheckUtils {

    public static final int TYPE_INT = 41;
    public static final int TYPE_STR = 42;
    public static final int TYPE_BOOL = 43;
    public static final int TYPE_NIX = 44;

    private CompatibilityCheckUtils(){
    }

    // TODO: Check if generated error messages are correct (must include type!)
    public static boolean checkOperatorCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx){

        // arithmetic and relational operations
        if (ctx.ADDOP() != null || ctx.MULOP() != null || ctx.RELOP() != null){
            if (lhs_type == TYPE_INT && rhs_type == TYPE_INT){
                return true;
            } else if (lhs_type == TYPE_BOOL || rhs_type == TYPE_BOOL){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhs_type.toString(), rhs_type.toString(), "int", "int");
                return true;
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhs_type.toString(), rhs_type.toString(), ctx.op.getText());
                return false;
            }
        }

        // logical operations
        if (ctx.AND() != null || ctx.OR() != null){
            if (lhs_type == TYPE_BOOL && rhs_type == TYPE_BOOL){
                return true;
            } else if (lhs_type == TYPE_INT || rhs_type == TYPE_INT){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhs_type.toString(), rhs_type.toString(), "bool", "bool");
                return true;
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhs_type.toString(), rhs_type.toString(), ctx.op.getText());
                return false;
            }
        }

        else {
            return false;
        }
    }

    // TODO: Check if generated error messages are correct (must include type!)
    public static boolean checkTernaryOperatorCompatibility(Integer lhs_type, Integer middle_type, Integer rhs_type, JovaParser.ExprContext ctx){
        return false;
    }

    public static boolean checkConditionCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx){
        return false;
    }
}
