package at.tugraz.ist.cc;

import at.tugraz.ist.cc.instructions.*;
import at.tugraz.ist.cc.symbol_table.SymbolCallable;
import at.tugraz.ist.cc.symbol_table.SymbolClass;
import at.tugraz.ist.cc.symbol_table.SymbolVariable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OptimizerUtils {
    public static HashMap<SymbolVariable, SymbolVariable> optimizerSymbolTable = new HashMap<>();
    public static HashMap<SymbolVariable, Boolean> codeEliminationTable = new HashMap<>();

    public OptimizerUtils() {

    }

    public static LinkedList<BaseInstruction> constantFoldingConstantPropagationCopyPropagation(LinkedList<BaseInstruction> instructions) {
        LinkedList<BaseInstruction> optimizedInstructions = new LinkedList<>();

        instructions.forEach(instruction -> {
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

            } else if (instruction instanceof LogicalBinaryInstruction){

                SymbolVariable lhs = ((LogicalBinaryInstruction) instruction).leftParam;
                SymbolVariable rhs = ((LogicalBinaryInstruction) instruction).rightParam;
                OperatorTypes operator = ((LogicalBinaryInstruction) instruction).operator;
                Boolean result = null;

                if (optimizerSymbolTable.get(lhs) != null) {
                    lhs = optimizerSymbolTable.get(lhs);
                    ((LogicalBinaryInstruction) instruction).setLhs(lhs);
                }

                if (optimizerSymbolTable.get(rhs) != null) {
                    rhs = optimizerSymbolTable.get(rhs);
                    ((LogicalBinaryInstruction) instruction).setRhs(rhs);
                }

                if (lhs.getValue() != null && rhs.getValue() != null) {
                    switch (operator) {
                        case AND:
                            result = (Boolean) lhs.getValue() && (Boolean) rhs.getValue();
                            break;

                        case OR:
                            result = (Boolean) lhs.getValue() || (Boolean) rhs.getValue();
                            break;
                    }
                    instruction.getResult().setValue(result);

                } else {
                    optimizedInstructions.add(instruction);
                }

            }
            else if (instruction instanceof RelationalBinaryInstruction){

                SymbolVariable lhs = ((RelationalBinaryInstruction) instruction).leftParam;
                SymbolVariable rhs = ((RelationalBinaryInstruction) instruction).rightParam;
                OperatorTypes operator = ((RelationalBinaryInstruction) instruction).operator;
                Boolean result = null;

                if (optimizerSymbolTable.get(lhs) != null) {
                    lhs = optimizerSymbolTable.get(lhs);
                    ((RelationalBinaryInstruction) instruction).setLhs(lhs);
                }

                if (optimizerSymbolTable.get(rhs) != null) {
                    rhs = optimizerSymbolTable.get(rhs);
                    ((RelationalBinaryInstruction) instruction).setRhs(rhs);
                }

                if (lhs.getValue() != null && rhs.getValue() != null) {
                    switch (operator) {
                        case SMALLER:
                            result = (Integer) lhs.getValue() < (Integer) rhs.getValue();
                            break;

                        case SMALLER_EQUAL:
                            result = (Integer) lhs.getValue() <= (Integer) rhs.getValue();
                            break;

                        case GREATER:
                            result = (Integer) lhs.getValue() > (Integer) rhs.getValue();
                            break;

                        case GREATER_EQUAL:
                            result = (Integer) lhs.getValue() >= (Integer) rhs.getValue();
                            break;

                        case EQUAL:
                            result = (Integer) lhs.getValue() == (Integer) rhs.getValue();
                            break;

                        case UNEQUAL:
                            result = (Integer) lhs.getValue() != (Integer) rhs.getValue();
                            break;
                    }
                    instruction.getResult().setValue(result);

                } else {
                    optimizedInstructions.add(instruction);
                }

            }
            else if (instruction instanceof AssignLocalInstruction) {
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

            } else if (instruction instanceof MethodInvocationInstruction) {
                MethodInvocationInstruction methodInvocationInstruction = (MethodInvocationInstruction) instruction;
                List<SymbolVariable> copiedParams = methodInvocationInstruction.getParams()
                        .stream()
                        .map(param -> {
                            if (optimizerSymbolTable.get(param) != null) {
                                return optimizerSymbolTable.get(param);
                            } else {
                                return param;
                            }
                        })
                        .collect(Collectors.toList());
                methodInvocationInstruction.setParams(copiedParams);
                optimizedInstructions.add(instruction);

            } else if (instruction instanceof ArithmeticUnaryInstruction){
                ArithmeticUnaryInstruction unaryInstruction = (ArithmeticUnaryInstruction) instruction;
                OperatorTypes operator = unaryInstruction.operator;
                SymbolVariable param = unaryInstruction.parameter;
                Integer result = null;

                if (optimizerSymbolTable.get(param) != null) {
                    param = optimizerSymbolTable.get(param);
                    ((ArithmeticUnaryInstruction) instruction).setParameter(param);
                }

                if (param.getValue() != null) {
                    switch (operator) {
                        case ADD:
                            result = (Integer) param.getValue();
                            break;

                        case SUB:
                            result = -(Integer) param.getValue();
                            break;
                    }
                    instruction.getResult().setValue(result);

                } else {
                    optimizedInstructions.add(instruction);
                }

            } else if (instruction instanceof LogicalUnaryInstruction){
                LogicalUnaryInstruction unaryInstruction = (LogicalUnaryInstruction) instruction;
                OperatorTypes operator = unaryInstruction.operator;
                SymbolVariable param = unaryInstruction.parameter;
                Integer result = null;

                if (optimizerSymbolTable.get(param) != null) {
                    param = optimizerSymbolTable.get(param);
                    ((LogicalUnaryInstruction) instruction).setParameter(param);
                }

                if (param.getValue() != null) {
                    switch (operator) {
                        case ADD:
                            result = (Integer) param.getValue();
                            break;

                        case SUB:
                            result = -(Integer) param.getValue();
                            break;
                    }
                    instruction.getResult().setValue(result);

                } else {
                    optimizedInstructions.add(instruction);
                }

            } else {
                optimizedInstructions.add(instruction);
            }
        });

        return optimizedInstructions;
    }

    public static LinkedList<BaseInstruction> deadCodeElimination(LinkedList<BaseInstruction> instructions, SymbolCallable method) {
        boolean optimized_in_last_round = true;

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
                    SymbolVariable rhs = ((AssignLocalInstruction) instruction).rhs;

                    if (codeEliminationTable.get(lhs)) {
                        // if assigned variable is live, keep the instruction and set lhs to dead
                        optimizedInstructions.addFirst(instruction);
                        codeEliminationTable.put(lhs, false);
                        codeEliminationTable.put(rhs, true);
                    }
                } else if (instruction instanceof ReturnInstruction) {
                    SymbolVariable retVal = ((ReturnInstruction) instruction).getReturnValue();
                    if (retVal.getValue() == null) {
                        codeEliminationTable.put(retVal, true);
                    }

                    optimizedInstructions.addFirst(instruction);
                } else if (instruction instanceof MethodInvocationInstruction) {
                    ((MethodInvocationInstruction) instruction).getParams().forEach(
                            param -> codeEliminationTable.put(param, true)
                    );

                    optimizedInstructions.addFirst(instruction);
                } else if (instruction instanceof ArithmeticBinaryInstruction
                        &&
                        (((ArithmeticBinaryInstruction) instruction).operator == OperatorTypes.DIV || (((ArithmeticBinaryInstruction) instruction).operator == OperatorTypes.MOD))
                        &&
                        (((ArithmeticBinaryInstruction) instruction).rightParam.getValue() == null || (Integer) ((ArithmeticBinaryInstruction) instruction).rightParam.getValue() == 0)) {

                    if (((ArithmeticBinaryInstruction) instruction).leftParam.getValue() == null) {
                        codeEliminationTable.put(((ArithmeticBinaryInstruction) instruction).leftParam, true);
                    }

                    if (((ArithmeticBinaryInstruction) instruction).rightParam.getValue() == null) {
                        codeEliminationTable.put(((ArithmeticBinaryInstruction) instruction).rightParam, true);
                    }

                    optimizedInstructions.addFirst(instruction);
                } else if (codeEliminationTable.get(instruction.getResult())) {
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
                    }

                    optimizedInstructions.addFirst(instruction);
                }
            }

            listIterator = optimizedInstructions.listIterator(optimizedInstructions.size());
        }

        return optimizedInstructions;
    }

    private static boolean isSymbolVarUsed(LinkedList<BaseInstruction> instructionsToLock,
                                        BaseInstruction instructionsToSkip, SymbolVariable variableToSearch) {
        boolean bool =  instructionsToLock
                .stream()
                .anyMatch(baseInstruction -> {
                    if (baseInstruction.equals(instructionsToSkip)) {
                        return false;
                    } else {
                        return baseInstruction.getUsedSymbolVariables().contains(variableToSearch);
                    }
                });
        return bool;
    }

    public static void reorderLocalArrayVars(SymbolCallable method) {
        Set<SymbolVariable> usedSymbolVariables = new HashSet<>();

        method.instructions.forEach(instruction -> {
            if (instruction instanceof MethodInvocationInstruction
                    && ((MethodInvocationInstruction) instruction).isReadOrPrint()
                    && !isSymbolVarUsed(method.instructions, instruction, instruction.getResult())) {
                // no one uses the result from the print
                ((MethodInvocationInstruction) instruction).setIgnoreResult();
            } else {
                usedSymbolVariables.addAll(instruction.getUsedSymbolVariables());
            }
        });

        /*  FYI: we can not use a simple int value from the stack inside a lambda
        => https://www.baeldung.com/java-lambda-effectively-final-local-variables */

        // 0 is reserved for the class ref or when it is the main it if for the args form the user
        int[] localArrayIndex = {1};
        Map<SymbolVariable, Integer> newLocalArrayMapping = new HashMap<>();

        if (method.associatedSymbolClass.getClassName().equals(SymbolClass.MAIN_CLASS_NAME)
                && usedSymbolVariables.size() != 0) {
            localArrayIndex[0] = 0; // if we have variables we can start at 0 and overwrite the java args for the static main
        }

        usedSymbolVariables.forEach(variable -> newLocalArrayMapping.put(variable, localArrayIndex[0]++));

        method.setLocalArrayMapping(newLocalArrayMapping, localArrayIndex[0]);
    }
}
