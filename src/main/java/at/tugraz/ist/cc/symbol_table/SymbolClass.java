package at.tugraz.ist.cc.symbol_table;

import java.util.*;

public class SymbolClass {
    private final String className;
    // TODO: maybe change the Object to a Interface and the the SymbolVariable and the SymbolClass implements the interface
    private final Collection<AbstractMap.SimpleEntry<SymbolModifier, Object>> member;
    private final Collection<SymbolMethod> methods;

    // TODO: maybe move the current things to the TypeCheckerJovaVisitorImpl or to a new singleton class?
    private SymbolType currentSymbolType;
    private SymbolPrimitiveType currentSymbolPrimitiveType;
    private String currentClassName;
    private Collection<String> currentIds;
    private SymbolMethod currentMethod;
    private Collection<Object> currentParams;

    public SymbolClass(String name) {
        className = name;
        this.member = new ArrayList<>();
        this.methods = new ArrayList<>();
        currentParams = new ArrayList<>();
        currentIds = new ArrayList<>();
    }

    public void buildCurrentMembers(SymbolModifier modifier){

        if (    currentIds == null ||
                currentSymbolType == null ||
                currentSymbolType == SymbolType.PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if member already with same name already exists
        currentIds.forEach(id -> {
            switch (currentSymbolType){
                case CLASS:
                    member.add(new AbstractMap.SimpleEntry<>(modifier, SymbolTable.getInstance().getClassByName(id)));
                    break;
                case PRIMITIVE:
                    member.add(new AbstractMap.SimpleEntry<>(modifier, new SymbolVariable<>(currentSymbolPrimitiveType, 0, id)));
                    break;
                default:
                    System.exit(666);
            }
        });

        currentIds = null;
        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
    }

    public void addMethod(SymbolModifier modifier, String name){
        if (    currentSymbolType == null ||
                currentSymbolType == SymbolType.PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if class is already there => name is not enough => function overloading
        // TODO check if already exists

        switch (currentSymbolType){
            case CLASS:
                currentMethod = new SymbolMethod(modifier, name, SymbolType.CLASS, currentParams);
                break;
            case PRIMITIVE:
                currentMethod = new SymbolMethod(modifier, name, SymbolType.PRIMITIVE , currentParams);
                break;
            default:
                System.exit(666);
        }

        methods.add(currentMethod);

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentParams = new ArrayList<>();
    }

    public void saveLocalVariables()
    {
        for (String s : currentIds) {
            switch (currentSymbolType){
                case CLASS:
                    currentMethod.addVariable(SymbolTable.getInstance().getClassByName(currentClassName));
                    break;
                case PRIMITIVE:
                    currentMethod.addVariable(new SymbolVariable<>(currentSymbolPrimitiveType, 0, s));
                    break;
                default:
                    System.exit(666);
            }
        }

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentParams = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolClass that = (SymbolClass) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, member, methods);
    }

    public void setCurrentParams(List<Object> currentParams) {
        this.currentParams = currentParams;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public void setCurrentSymbolType(SymbolType currentSymbolType) {
        this.currentSymbolType = currentSymbolType;
    }

    public void setCurrentIds(List<String> currentIds) {
        this.currentIds = currentIds;
    }

    public void setCurrentSymbolPrimitiveType(SymbolPrimitiveType currentSymbolPrimitiveType) {
        this.currentSymbolPrimitiveType = currentSymbolPrimitiveType;
    }

    public String getClassName() {
        return className;
    }
}
