package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.ErrorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class SimpleCallable {
    protected final String name;
    protected final List<SymbolVariable> params;
    protected final List<SymbolVariable> localVariables;


    public SimpleCallable(String name, List<SymbolVariable> params) {
        this.name = name;
        this.params = params;
        this.localVariables = new ArrayList<>();
    }

    public void addVariable(SymbolVariable symbolVariable) {
        localVariables.add(symbolVariable);
    }

    public int checkParamDoubleDeclaration(JovaParser.Param_listContext ctx) {
        Collection<String> names = new ArrayList<>();
        for (SymbolVariable param : params) {
            String currentName = param.getName();
            if (names.contains(currentName)) {

                ErrorHandler.INSTANCE.addVarDoubleDefError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        currentName, param.getTypeAsString(), this.getName());
                return -1;
            }
            names.add(currentName);
        }
        return 0;
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
                System.exit(-1);
                return null;
            }
        }).toArray(String[]::new);
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

    /**
     * returns true if the objects are the same or if the names of the methods and also
     * the parameter order and types are the same. the return value of the method is not
     * taken into account
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolMethod that = (SymbolMethod) o;

        if (!Objects.equals(name, that.name)) return false;
        if (params.size() != that.params.size()) return false;

        for (int i = 0; i < params.size(); ++i) {
            SymbolVariable thisParam = params.get(i);
            SymbolVariable thatParam= that.params.get(i);

            if (    !(thisParam.getType() == SymbolType.PRIMITIVE &&
                    thatParam.getType() == SymbolType.PRIMITIVE &&
                    ((SymbolPrimitiveType) thisParam.getActualType()).equals(((SymbolPrimitiveType)thatParam.getActualType())))
                    &&
                    !(thisParam.getType() == SymbolType.CLASS &&
                    thatParam.getType() == SymbolType.CLASS &&
                    ((SymbolClass) thisParam.getActualType()).getClassName().
                            equals(((SymbolClass) thatParam.getActualType()).getClassName()))){
                return false;
            }
        }

        // only reachable if all params where equal
        return true;
    }
}
