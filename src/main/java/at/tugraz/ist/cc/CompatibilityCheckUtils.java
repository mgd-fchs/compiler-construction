package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolTable;

import java.util.Locale;

public final class CompatibilityCheckUtils {

    public static final int TYPE_INT = SymbolPrimitiveType.INT.getValue();
    public static final int TYPE_STR = SymbolPrimitiveType.STRING.getValue();
    public static final int TYPE_BOOL = SymbolPrimitiveType.BOOL.getValue();
    private static final int TYPE_ERROR = -30;
    public static final int TYPE_NIX = 44;

    private CompatibilityCheckUtils(){
    }
    // TODO @all Discuss if this should be part of the symbol table

    public static Integer checkOperatorCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx){
        // arithmetic and relational operations
        if (ctx.ADDOP() != null || ctx.MULOP() != null || ctx.RELOP() != null){
            if (lhs_type == TYPE_INT && rhs_type == TYPE_INT) {
                return SymbolPrimitiveType.INT.getValue();
            } else if ((lhs_type == TYPE_BOOL || lhs_type == TYPE_INT) && (rhs_type == TYPE_BOOL || rhs_type == TYPE_INT)){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), SymbolPrimitiveType.valueOf(lhs_type).toString().toLowerCase(), SymbolPrimitiveType.valueOf(rhs_type).toString().toLowerCase(), "int", "int");
                return SymbolPrimitiveType.INT.getValue();
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), SymbolPrimitiveType.valueOf(lhs_type).toString().toLowerCase(), SymbolPrimitiveType.valueOf(rhs_type).toString().toLowerCase(), ctx.op.getText());
                return TYPE_ERROR;
            }
        }

        // logical operations
        if (ctx.AND() != null || ctx.OR() != null){
            if (lhs_type == TYPE_BOOL && rhs_type == TYPE_BOOL){
                return SymbolPrimitiveType.BOOL.getValue();
            } else if (lhs_type == TYPE_INT || rhs_type == TYPE_INT){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), SymbolPrimitiveType.valueOf(lhs_type).toString().toLowerCase(), SymbolPrimitiveType.valueOf(rhs_type).toString().toLowerCase(), "bool", "bool");
                return SymbolPrimitiveType.BOOL.getValue();
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), SymbolPrimitiveType.valueOf(lhs_type).toString().toLowerCase(), SymbolPrimitiveType.valueOf(rhs_type).toString().toLowerCase(), ctx.op.getText());
                return TYPE_ERROR;
            }
        }

        else {
            return TYPE_ERROR;
        }
    }

    public static Integer checkTernaryOperatorCompatibility(Integer whenType, Integer thenType, Integer elseType, JovaParser.ExprContext ctx){
        // TODO: Check error messages for ternary operator
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

    public static boolean checkConditionCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx){
        return false;
    }
}
