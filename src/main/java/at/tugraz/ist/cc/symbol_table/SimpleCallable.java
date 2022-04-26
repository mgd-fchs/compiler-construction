package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.ErrorHandler;
import at.tugraz.ist.cc.instructions.BaseInstruction;

import java.util.*;

public abstract class SimpleCallable {
    protected final String name;
    protected final List<SymbolVariable> params;
    protected final List<SymbolVariable> localVariables;
    protected final List<SymbolVariable> tempVariable;
    protected final SymbolVariable returnValue;
    public List<BaseInstruction> instructions;
    private final Map<SymbolVariable, Integer> localArrayMapping;
    private int localArrayIndex;

    public SimpleCallable(String name, List<SymbolVariable> params, SymbolVariable returnValue) {
        this.name = name;
        this.returnValue = returnValue;

        localArrayIndex = 1; // starting at zero, because the pos 0 will be used for this
        this.localArrayMapping = new HashMap<>();
        this.params = params;
        // adding param mapping for params
        this.params.forEach(param -> localArrayMapping.put(param, localArrayIndex++));


        this.localVariables = new ArrayList<>();
        this.tempVariable = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public void addVariable(SymbolVariable symbolVariable) {
        localVariables.add(symbolVariable);
        localArrayMapping.put(symbolVariable, localArrayIndex++);
    }

    private int getLocalArrayIndexBySymbolVariable(SymbolVariable symbolVariable) {
        return localArrayMapping.get(symbolVariable);
    }

    public int checkParamDoubleDeclaration(JovaParser.Param_listContext ctx) {
        Collection<String> names = new ArrayList<>();
        boolean doubleDecl = false;
        for (SymbolVariable param : params) {
            String currentName = param.getName();
            if (names.contains(currentName)) {

                ErrorHandler.INSTANCE.addVarDoubleDefError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        currentName, param.getTypeAsString(), this.getName(), getParamTypesAsString());
                doubleDecl = true;
                continue;
            }
            names.add(currentName);
        }
        return (doubleDecl) ? -1 : 0;
    }

    public String[] getParamTypesAsString() {
        return params.stream().map(element -> {
            Object actualType = element.getActualType();
            if (actualType instanceof SymbolClass) {
                return ((SymbolClass) actualType).getClassName();
            } else if (actualType instanceof SymbolPrimitiveType) {
                return ((SymbolPrimitiveType) actualType).toString().toLowerCase();
            } else {
                // should not be reachable
                System.exit(69);
                return null;
            }
        }).toArray(String[]::new);
    }

    public boolean checkValidArgList(List<SymbolVariable> argList) {
        if (params.size() != argList.size()) {
            return false;
        }

        for (int i = 0; i < params.size(); i++) {
            if (!params.get(i).equalTypeAndActualType(argList.get(i))) {
                return false;
            }
        }

        return true;
    }

    public List<SymbolVariable> getParams() {
        return params;
    }

    public List<SymbolVariable> getLocalVariables() {
        return localVariables;
    }

    public String getName() {
        return name;
    }

    public Object getLocalVariableType(String id) {
        Optional<SymbolVariable> found = localVariables.stream().filter(element -> element.getName().equals(id)).findFirst();
        return found.get().getActualType();
    }

    public SymbolVariable getMethodVariable(String name) {
        for (SymbolVariable v : params) {
            if (v.getName().equals(name)) {
                return v;
            }
        }

        for (SymbolVariable v : localVariables) {
            if (v.getName().equals(name)) {
                return v;
            }
        }

        return null;
    }

    public SymbolVariable getLocalVariableById(String id) {
        Optional<SymbolVariable> found = localVariables.stream().filter(element -> element.getName().equals(id)).findFirst();
        if (found.isEmpty()) {
            return null;
        }
        return found.get();
    }

    public SymbolVariable getReturnValue() {
        return new SymbolVariable(returnValue);
    }

    public List<BaseInstruction> getInstructions() {
        return new ArrayList<>(List.copyOf(instructions));
    }


    /**
     * returns true if the objects are the same or if the names of the methods and also
     * the parameter order and types are the same. the return value of the method is not
     * taken into account
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCallable that = (SimpleCallable) o;

        if (!Objects.equals(name, that.name)) return false;
        if (params.size() != that.params.size()) return false;

        for (int i = 0; i < params.size(); ++i) {
            SymbolVariable thisParam = params.get(i);
            SymbolVariable thatParam = that.params.get(i);

            if (!thisParam.equalTypeAndActualType(thatParam)) {
                return false;
            }
        }

        // only reachable if all params where equal
        return true;
    }

    public SymbolVariable getNewTempSymbolVariable(SymbolVariable result) {
        SymbolVariable deepCopy = new SymbolVariable(result, "tmp_" + (localArrayIndex), true);
        tempVariable.add(deepCopy);
        localArrayMapping.put(deepCopy, localArrayIndex++);
        return deepCopy;
    }
}
