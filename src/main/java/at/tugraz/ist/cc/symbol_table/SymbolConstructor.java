package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.error.ErrorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SymbolConstructor {
    private final String name;
    private final List<SymbolVariable> params;
    private final List<SymbolVariable> localVariables;

    public SymbolConstructor(List<SymbolVariable> params, String name) {
        this.name = name;
        this.params = params;
        this.localVariables = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolConstructor that = (SymbolConstructor) o;

        if (params.size() != that.params.size()) return false;

        for (int i = 0; i < params.size(); ++i) {
            if (params.get(i).getType() != that.params.get(i).getType()) {
                return false;
            }
        }

        return true;
    }
}
