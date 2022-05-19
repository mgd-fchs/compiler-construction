package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.*;
import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.*;

public class OptimizerUtils {
    public static HashMap<SymbolVariable, SymbolVariable> optimizerSymbolTable = new HashMap<>();
    public static HashMap<SymbolVariable, Boolean> codeEliminationTable = new HashMap<>();

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

                        if (optimizerSymbolTable.get(lhs) != null) {
                            lhs = optimizerSymbolTable.get(lhs);
                            ((ArithmeticBinaryInstruction) instruction).setLhs(lhs);
                        }

                        if (optimizerSymbolTable.get(rhs) != null) {
                            rhs = optimizerSymbolTable.get(rhs);
                            ((ArithmeticBinaryInstruction) instruction).setRhs(rhs);
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

                    } else if (instruction instanceof AssignLocalInstruction) {
                        SymbolVariable lhs = ((AssignLocalInstruction) instruction).lhs;
                        SymbolVariable rhs = ((AssignLocalInstruction) instruction).rhs;

                        if (optimizerSymbolTable.get(rhs) != null) {
                            rhs = optimizerSymbolTable.get(rhs);
                            ((AssignLocalInstruction) instruction).setRhs(rhs);
                        }

                        optimizerSymbolTable.put(lhs, rhs);
                        optimizedInstructions.add(instruction);

                    } else if (instruction instanceof ReturnInstruction) {
                        SymbolVariable retVal = ((ReturnInstruction) instruction).getReturnValue();

                        if (optimizerSymbolTable.get(retVal) != null) {
                            retVal = optimizerSymbolTable.get(retVal);
                            ((ReturnInstruction) instruction).setReturnValue(retVal);
                        }

                        optimizedInstructions.add(instruction);

                    } else {
                        optimizedInstructions.add(instruction);
                    }
                });

        return optimizedInstructions;
    }

    public static LinkedList<BaseInstruction> deadCodeElimination(LinkedList<BaseInstruction> instructions, SymbolCallable method) {
        boolean optimized_in_last_round = true;
        boolean keepAssignment = false;

        ListIterator<BaseInstruction> listIterator = instructions.listIterator(instructions.size());

        List<SymbolVariable> allVars = method.getAllVars();
        LinkedList<BaseInstruction> optimizedInstructions = new LinkedList<>();


        while (optimized_in_last_round) {
            optimized_in_last_round = false;
            allVars.forEach(var -> codeEliminationTable.put(var, false));
            optimizedInstructions = new LinkedList<>();

            while (listIterator.hasPrevious()) {
                BaseInstruction instruction = listIterator.previous();

                if (instruction instanceof AssignLocalInstruction) {
                    SymbolVariable lhs = ((AssignLocalInstruction) instruction).lhs;

                    if (codeEliminationTable.get(lhs)) {
                        // if assigned variable is live, keep the instruction and set lhs to dead
                        optimizedInstructions.addFirst(instruction);
                        keepAssignment = true;
                        codeEliminationTable.put(lhs, false);
                    } else {
                        keepAssignment = false;
                        optimized_in_last_round = true;
                    }
                }
                else {
                    if (keepAssignment) {
                        if (instruction instanceof BinaryInstruction) {
                            SymbolVariable lhs = ((BinaryInstruction) instruction).leftParam;
                            SymbolVariable rhs = ((BinaryInstruction) instruction).rightParam;

                            if (lhs.getValue() == null) {
                                codeEliminationTable.put(lhs, true);
                            }
                            if (rhs.getValue() == null) {
                                codeEliminationTable.put(rhs, true);
                            }
                        } else if (instruction instanceof UnaryInstruction) {
                            SymbolVariable param = ((UnaryInstruction) instruction).getParameter();

                            if (param.getValue() == null) {
                                codeEliminationTable.put(param, true);
                            }
                        } else if (instruction instanceof MethodInvocationInstruction) {
                            ((MethodInvocationInstruction) instruction).getParams().forEach(
                                    param -> codeEliminationTable.put(param, true)
                            );
                        }

                        optimizedInstructions.addFirst(instruction);
                    } else {
                        if (instruction instanceof ReturnInstruction) {
                            SymbolVariable retVal = ((ReturnInstruction) instruction).getReturnValue();
                            if (retVal.getValue() == null) {
                                codeEliminationTable.put(retVal, true);
                            }
                            optimizedInstructions.addFirst(instruction);
                        } else {
                            optimized_in_last_round = true;
                        }
                    }
                }
            }

            listIterator = optimizedInstructions.listIterator(optimizedInstructions.size());
        }

        return optimizedInstructions;
    }
}
