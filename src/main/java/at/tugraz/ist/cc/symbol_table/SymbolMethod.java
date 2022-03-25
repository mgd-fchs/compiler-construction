package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.ErrorHandler;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SymbolMethod {
    private final SymbolModifier accessSymbol;
    private final String name;
    private final SymbolVariable returnValue;
    // TODO refactor
    private final List<SymbolVariable> params;
    // TODO add reference to return value if class

    private final List<SymbolVariable> localVariables;


    public SymbolMethod(SymbolModifier accessSymbol, String name, SymbolVariable returnValue, List<SymbolVariable> params) {
        // TODO check if there are no params with same name
        this.accessSymbol = accessSymbol;
        this.name = name;
        this.returnValue = returnValue;
        this.params = params;
        localVariables = new ArrayList<>();
    }

    public SymbolModifier getAccessSymbol() {
        return accessSymbol;
    }

    public String getName() {
        return name;
    }

    public SymbolVariable getReturnValue() {
        return returnValue;
    }

    public List<SymbolVariable> getParams() {
        return params;
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

    public List<SymbolVariable> getLocalVariables() {
        return localVariables;
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
            if (params.get(i).getType() != that.params.get(i).getType()) {
                return false;
            }
        }

        return true;
    }
}
