package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;

import java.util.Locale;

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
}
