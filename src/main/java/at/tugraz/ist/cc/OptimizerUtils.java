package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.*;
import at.tugraz.ist.cc.symbol_table.SymbolType;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class OptimizerUtils {
    public static HashMap<SymbolVariable, SymbolVariable> optimizerSymbolTable = new HashMap<>();

    public OptimizerUtils() {

    }

    public static LinkedList<BaseInstruction> constantsFolding(LinkedList<BaseInstruction> instructions) {
        LinkedList<BaseInstruction> optimizedInstructions = new LinkedList<BaseInstruction>();

        instructions.forEach(
            instruction -> {
                if (instruction instanceof ArithmeticBinaryInstruction) {
                    SymbolVariable lhs = ((ArithmeticBinaryInstruction) instruction).leftParam;
                    SymbolVariable rhs = ((ArithmeticBinaryInstruction) instruction).rightParam;
                    OperatorTypes operator = ((ArithmeticBinaryInstruction) instruction).operator;
                    Integer result = null;

                    if (optimizerSymbolTable.get(lhs) != null){
                        lhs = optimizerSymbolTable.get(lhs);
                        ((ArithmeticBinaryInstruction)instruction).setLhs(lhs);
                    }

                    if (optimizerSymbolTable.get(rhs) != null){
                        rhs = optimizerSymbolTable.get(rhs);
                        ((ArithmeticBinaryInstruction)instruction).setRhs(rhs);
                    }


                    // TODO: Ask if we need to do this for logic operators as well
                    if (lhs.getValue() != null && rhs.getValue() != null) {
                        switch (operator) {
                            case ADD:
                                result = (Integer) lhs.getValue() + (Integer) rhs.getValue();
                                break;

                            case MUL:
                                result = (Integer) lhs.getValue() * (Integer) rhs.getValue();
                                break;

                            case SUB:
                                result = (Integer) lhs.getValue() - (Integer) rhs.getValue();
                                break;

                            case DIV:
                                if ((Integer) rhs.getValue() != 0) {
                                    result = (Integer) lhs.getValue() / (Integer) rhs.getValue();
                                } else {
                                    optimizedInstructions.add(instruction);
                                }
                                break;

                            case MOD:
                                if ((Integer) rhs.getValue() != 0) {
                                    result = (Integer) lhs.getValue() % (Integer) rhs.getValue();
                                } else {
                                    optimizedInstructions.add(instruction);
                                }
                                break;
                        }
                        instruction.getResult().setValue(result);

                    } else {
                        optimizedInstructions.add(instruction);
                    }

                }
                else if (instruction instanceof AssignLocalInstruction) {
                  SymbolVariable lhs = ((AssignLocalInstruction)instruction).lhs;
                  SymbolVariable rhs = ((AssignLocalInstruction)instruction).rhs;

                  optimizerSymbolTable.put(lhs, rhs);
                  optimizedInstructions.add(instruction);

                }
                else if (instruction instanceof ReturnInstruction) {
                    SymbolVariable retVal = ((ReturnInstruction)instruction).getReturnValue();

                    if (optimizerSymbolTable.get(retVal) != null){
                        retVal = optimizerSymbolTable.get(retVal);
                        ((ReturnInstruction) instruction).setReturnValue(retVal);
                    }

                    optimizedInstructions.add(instruction);

                }
                else {
                    optimizedInstructions.add(instruction);
                }
        });

        return optimizedInstructions;
    }

    public static LinkedList<BaseInstruction> constantsPropagation(LinkedList<BaseInstruction> instructions) {
        LinkedList<BaseInstruction> optimizedInstructions = new LinkedList<BaseInstruction>();

        instructions.forEach(
                instruction -> {
                }
        );

        return optimizedInstructions;
    }
}
