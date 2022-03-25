package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolTable;

import java.util.Locale;

public final class CompatibilityCheckUtils {

    public static final int TYPE_INT = SymbolPrimitiveType.INT.getValue();
    public static final int TYPE_STR = SymbolPrimitiveType.STRING.getValue();
    public static final int TYPE_BOOL = SymbolPrimitiveType.BOOL.getValue();
    public static final int TYPE_NIX = 44;

    private CompatibilityCheckUtils(){
    }
    // TODO @all Discuss if this should be part of the symbol table

    public static boolean checkOperatorCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx){
        // arithmetic and relational operations
        if (ctx.ADDOP() != null || ctx.MULOP() != null || ctx.RELOP() != null){
            if (lhs_type == TYPE_INT && rhs_type == TYPE_INT) {
                return true;
            } else if ((lhs_type == TYPE_BOOL || lhs_type == TYPE_INT) && (rhs_type == TYPE_BOOL || rhs_type == TYPE_INT)){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), SymbolPrimitiveType.valueOf(lhs_type).toString().toLowerCase(), SymbolPrimitiveType.valueOf(rhs_type).toString().toLowerCase(), "int", "int");
                return true;
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), SymbolPrimitiveType.valueOf(lhs_type).toString().toLowerCase(), SymbolPrimitiveType.valueOf(rhs_type).toString().toLowerCase(), ctx.op.getText());
                return false;
            }
        }

        // logical operations
        if (ctx.AND() != null || ctx.OR() != null){
            if (lhs_type == TYPE_BOOL && rhs_type == TYPE_BOOL){
                return true;
            } else if (lhs_type == TYPE_INT || rhs_type == TYPE_INT){
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), SymbolPrimitiveType.valueOf(lhs_type).toString().toLowerCase(), SymbolPrimitiveType.valueOf(rhs_type).toString().toLowerCase(), "bool", "bool");
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
    
    public static boolean checkTernaryOperatorCompatibility(Integer lhs_type, Integer middle_type, Integer rhs_type, JovaParser.ExprContext ctx){
        return false;
    }

    public static boolean checkConditionCompatibility(Integer lhs_type, Integer rhs_type, JovaParser.ExprContext ctx){
        return false;
    }
}
