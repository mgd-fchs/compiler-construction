package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.ArithmeticBinaryInstruction;
import at.tugraz.ist.cc.instructions.AssignLocalInstruction;
import at.tugraz.ist.cc.instructions.BaseInstruction;
import at.tugraz.ist.cc.instructions.OperatorTypes;
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
                    }

                    if (optimizerSymbolTable.get(rhs) != null){
                        rhs = optimizerSymbolTable.get(rhs);
                    }


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

                    }

                    }

                else if (instruction instanceof AssignLocalInstruction) {
                  SymbolVariable lhs = ((AssignLocalInstruction)instruction).lhs;
                  SymbolVariable rhs = ((AssignLocalInstruction)instruction).rhs;

                  optimizerSymbolTable.put(lhs, rhs);
                  optimizedInstructions.add(instruction);

                } else {
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
