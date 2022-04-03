package at.tugraz.ist.cc;

import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolPrimitiveType;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;
import org.antlr.v4.runtime.ParserRuleContext;

public final class CompatibilityCheckUtils {

    public static final SymbolPrimitiveType TYPE_INT = SymbolPrimitiveType.INT;
    public static final SymbolPrimitiveType TYPE_STR = SymbolPrimitiveType.STRING;
    public static final SymbolPrimitiveType TYPE_BOOL = SymbolPrimitiveType.BOOL;
    public static final SymbolPrimitiveType TYPE_NIX = SymbolPrimitiveType.NIX;
    public static final SymbolPrimitiveType TYPE_FLOAT = SymbolPrimitiveType.FLOAT;
    public static final SymbolPrimitiveType TYPE_CHAR = SymbolPrimitiveType.CHAR;
    public static final SymbolType TYPE_CLASS = SymbolType.CLASS;
    public static final SymbolType TYPE_PRIMITIVE = SymbolType.PRIMITIVE;

    private static final int TYPE_ERROR = -30;
    private static final int OK = 0;

    private CompatibilityCheckUtils() {
    }

    public static SymbolVariable checkOperatorCompatibility(SymbolVariable lhsVar, SymbolVariable rhsVar, JovaParser.ExprContext ctx, SymbolClass currentClass) {

        // arithmetic and relational operations
        if (lhsVar.getType() == TYPE_CLASS || rhsVar.getType() == TYPE_CLASS){
            return checkRelOpClass(lhsVar, rhsVar, ctx, currentClass);
        }

        if (lhsVar.getActualType() == TYPE_STR || rhsVar.getActualType() == TYPE_STR || lhsVar.getActualType() == TYPE_NIX || rhsVar.getActualType() == TYPE_NIX){
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
            return null;
        }

        if (ctx.ADDOP() != null || ctx.MULOP() != null) {
            if (lhsVar.getActualType() == TYPE_INT && rhsVar.getActualType() == TYPE_INT) {
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "");
            } else if (lhsVar.getActualType() == TYPE_FLOAT && rhsVar.getActualType() == TYPE_FLOAT){
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.FLOAT, "");
            } else if ((lhsVar.getActualType() == TYPE_BOOL || lhsVar.getActualType() == TYPE_INT) && (rhsVar.getActualType() == TYPE_BOOL || rhsVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), "int", "int");
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.INT, "");
            } else if ((lhsVar.getActualType() == TYPE_FLOAT || lhsVar.getActualType() == TYPE_INT) && (rhsVar.getActualType() == TYPE_FLOAT || rhsVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), "float", "float");
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.FLOAT, "");
            } else {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
                return null;
            }
        }
        if (ctx.RELOP() != null){
            if (lhsVar.getActualType() == TYPE_INT && rhsVar.getActualType() == TYPE_INT) {
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            } else if (lhsVar.getActualType() == TYPE_FLOAT && rhsVar.getActualType() == TYPE_FLOAT){
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            } else if ((lhsVar.getActualType() == TYPE_BOOL || lhsVar.getActualType() == TYPE_INT) && (rhsVar.getActualType() == TYPE_BOOL || rhsVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), "int", "int");
                return new SymbolVariable(SymbolType.PRIMITIVE, SymbolPrimitiveType.BOOL, "");
            } else if ((lhsVar.getActualType() == TYPE_FLOAT || lhsVar.getActualType() == TYPE_INT) && (rhsVar.getActualType() == TYPE_FLOAT || rhsVar.getActualType() == TYPE_INT)) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ctx.op.getText(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), "float", "float");
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
            if (!ctx.op.getText().contains("==") && !ctx.op.getText().contains("!=")) {
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), lhsVar.getTypeAsString(), rhsVar.getTypeAsString(), ctx.op.getText());
                return null;
            } else if (lhsVar.getType() == TYPE_CLASS && rhsVar.getType() == TYPE_CLASS && !lhsVar.equalTypeAndActualType(rhsVar)){
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
        if (shouldVar.getActualType() !=  isVar.getActualType()) {

            if (    ((shouldVar.getActualType() == TYPE_BOOL || shouldVar.getActualType() == TYPE_INT) && (isVar.getActualType() == TYPE_BOOL || isVar.getActualType() == TYPE_INT)) /* allow coercion from int to bool and vice versa */
                ||
                    (shouldVar.getActualType() == TYPE_FLOAT && isVar.getActualType() == TYPE_INT) /* allow coercion from int to float */

                    ||
                    (shouldVar.getActualType() == TYPE_STR && isVar.getActualType() == TYPE_CHAR) /* allow coercion from char to string*/ ) {

                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), "=",
                        shouldVar.getTypeAsString(), isVar.getTypeAsString(), shouldVar.getTypeAsString(), shouldVar.getTypeAsString());
                return OK;
            } else if (shouldVar.getType() == TYPE_CLASS && isVar.getActualType() == TYPE_NIX){
                return OK;
            }

            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    shouldVar.getTypeAsString(), isVar.getTypeAsString(), "=");
            return TYPE_ERROR;
        }
        return OK;
    }

    public static Integer checkReturnValue(SymbolVariable actualReturnValue, SymbolClass currentClass, JovaParser.Ret_stmtContext ctx) {
        SymbolVariable expectedReturnType = currentClass.getCurrentCallable().getReturnValue();

        if (expectedReturnType.equalTypeAndActualType(actualReturnValue) || expectedReturnType.getType().equals(SymbolType.CLASS) && actualReturnValue.getActualType() == TYPE_NIX) {
            return OK;
        } else if ((actualReturnValue.getActualType() == TYPE_BOOL || actualReturnValue.getActualType() == TYPE_INT) && (expectedReturnType.getActualType() == TYPE_BOOL || expectedReturnType.getActualType() == TYPE_INT)) {
            ErrorHandler.INSTANCE.addReturnTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnValue.getTypeAsString(), expectedReturnType.getTypeAsString());
            return OK;
        }
        if (expectedReturnType.getActualType() == TYPE_FLOAT && actualReturnValue.getActualType() == TYPE_INT) {
            // allow coercion from int to float
            ErrorHandler.INSTANCE.addReturnTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnValue.getTypeAsString(), expectedReturnType.getTypeAsString());
            return OK;
        }
        if (expectedReturnType.getActualType() == TYPE_STR && actualReturnValue.getActualType() == TYPE_CHAR){
            // allow coercion from char to string
            ErrorHandler.INSTANCE.addReturnTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnValue.getTypeAsString(), expectedReturnType.getTypeAsString());
            return OK;
        }
        else {
            ErrorHandler.INSTANCE.addIncompatibleReturnTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), actualReturnValue.getTypeAsString());
            return TYPE_ERROR;
        }
    }


    public static SymbolVariable checkTernaryOperatorCompatibility(SymbolVariable whenType, SymbolVariable thenType, SymbolVariable elseType, JovaParser.ExprContext ctx, SymbolClass currentClass) {

        // check condition
        if (whenType.getActualType() != TYPE_BOOL && whenType.getActualType() != TYPE_INT) {
            ErrorHandler.INSTANCE.addUnaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), whenType.getTypeAsString(), "?");
            return null;
        } else if (whenType.getActualType() == TYPE_INT) {
            ErrorHandler.INSTANCE.addUnaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), "?", whenType.getTypeAsString(), "bool");
        }

        // check resulting expressions (then/else)
        if (thenType.getType() == TYPE_CLASS || thenType.getActualType() == TYPE_NIX){
            if (elseType.getType() == TYPE_CLASS || elseType.getActualType() == TYPE_NIX){
                if (thenType.getType() == TYPE_CLASS && elseType.getType() == TYPE_CLASS){
                    if (!thenType.equals(elseType)){
                        ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), thenType.getTypeAsString(), elseType.getTypeAsString(), ":");
                        return null;
                    }
                    return elseType;
                }
                if (thenType.getActualType() == TYPE_NIX){
                    return elseType;
                }
                return thenType;
            }
            ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), thenType.getTypeAsString(), elseType.getTypeAsString(), ":");
            return null;
        }

        if (thenType.getActualType() == TYPE_INT){
            if (elseType.getActualType() == TYPE_BOOL) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ":", thenType.getTypeAsString(), elseType.getTypeAsString(), "int", "int");
                return thenType;
            } else if (elseType.getActualType() != TYPE_INT){
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), thenType.getTypeAsString(), elseType.getTypeAsString(), ":");
                return null;
            }
            return thenType;
        }

        if (thenType.getActualType() == TYPE_BOOL){
            if (elseType.getActualType() == TYPE_INT) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ":", thenType.getTypeAsString(), elseType.getTypeAsString(), "bool", "bool");
                return thenType;
            } else if (elseType.getActualType() != TYPE_BOOL){
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), thenType.getTypeAsString(), elseType.getTypeAsString(), ":");
                return null;
            }
            return thenType;
        }

        if (thenType.getActualType() == TYPE_FLOAT){
            if (elseType.getActualType() == TYPE_INT) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(),":", thenType.getTypeAsString(), elseType.getTypeAsString(), "float", "float");
                return thenType;
            } else if (elseType.getActualType() != TYPE_FLOAT){
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), thenType.getTypeAsString(), elseType.getTypeAsString(), ":");
                return null;
            }
            return thenType;
        }

        if (thenType.getActualType() == TYPE_STR){
            if (elseType.getActualType() == TYPE_CHAR) {
                ErrorHandler.INSTANCE.addBinaryTypeCoercionWarning(ctx.start.getLine(), ctx.start.getCharPositionInLine(), ":", thenType.getTypeAsString(), elseType.getTypeAsString(), "string", "string");
                return thenType;
            } else if (elseType.getActualType() != TYPE_STR){
                ErrorHandler.INSTANCE.addBinaryTypeError(ctx.start.getLine(), ctx.start.getCharPositionInLine(), thenType.getTypeAsString(), elseType.getTypeAsString(), ":");
                return null;
            }
            return thenType;
        }

        return null;
    }

    public static Integer checkConditionCompatibility(SymbolVariable condition_type, ParserRuleContext ctx) {

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

}
