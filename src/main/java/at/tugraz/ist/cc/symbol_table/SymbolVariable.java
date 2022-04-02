package at.tugraz.ist.cc.symbol_table;

import java.util.Objects;


public class SymbolVariable {
    private final SymbolType type;
    private final Object actualType;
    private final String name;

    public SymbolVariable(SymbolType type, Object actualType, String name) {
        // TODO maybe change this kind of checks to asserts
        if (type == SymbolType.PRIMITIVE && !(actualType instanceof SymbolPrimitiveType) ||
                type == SymbolType.CLASS && !(actualType instanceof  SymbolClass)) {
            System.exit(76);
        }

        this.type = type;
        this.actualType = actualType;
        this.name = name;
    }

    public SymbolType getType() {
        return type;
    }

    public Object getActualType() {
        return actualType;
    }

    public String getTypeAsString() {
        if (actualType instanceof SymbolClass) {
            return ((SymbolClass) actualType).getClassName();
        } else if (actualType instanceof SymbolPrimitiveType) {
            return ((SymbolPrimitiveType) actualType).toString().toLowerCase();
        } else {
            System.exit(77);
            return null;
        }
    }

    public String getName() {
        return name;
    }

    /**
     * This only checks if the name is equal
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolVariable that = (SymbolVariable) o;
        return Objects.equals(name, that.name);
    }

    public boolean equalTypeAndActualType(SymbolVariable that) {
        return (type == SymbolType.PRIMITIVE && that.getType() == SymbolType.PRIMITIVE &&
                ((SymbolPrimitiveType) actualType).equals(that.getActualType()))
                ||
                (type == SymbolType.CLASS && that.getType() == SymbolType.CLASS &&
                ((SymbolClass) actualType).getClassName(). equals(((SymbolClass) that.getActualType()).getClassName()));
    }

}
