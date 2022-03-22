package at.tugraz.ist.cc.symbol_table;

import java.util.*;

public class SymbolClass {
    private final String className;
    private Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> memberPrimitives;
    private Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolClass>> memberClasses;
    private Collection<SymbolMethod> methods;

    private SymbolType currentSymbolType;
    private SymbolPrimitiveType currentSymbolPrimitiveType;
    private String currentClassName;

    private SymbolModifier currentModifier;


    List<String> currentIds;


    private SymbolMethod currentMethod;
    private List<Object> currentParams;

    public void setCurrentParams(List<Object> currentParams) {
        this.currentParams = currentParams;
    }

    // TODO check if ctor is really necessary in assigment sheet
    private boolean hasCtor;

    public void setCurrentModifier(SymbolModifier currentModifier) {
        this.currentModifier = currentModifier;
    }


    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public void setCurrentSymbolType(SymbolType currentSymbolType) {
        this.currentSymbolType = currentSymbolType;
    }

    public void setCurrentSymbolPrimitiveType(SymbolPrimitiveType currentSymbolPrimitiveType) {
        this.currentSymbolPrimitiveType = currentSymbolPrimitiveType;
    }

    public String getClassName() {
        return className;
    }

    public void setCurrentIds(List<String> currentIds) {
        this.currentIds = currentIds;
    }

    public SymbolClass(String name) {
        className = name;
        this.memberPrimitives = new ArrayList<>();
        this.memberClasses = new ArrayList<>();
        this.methods = new ArrayList<>();
        hasCtor = false;
        currentParams = new ArrayList<>();
        currentIds = new ArrayList<>();
    }

    public boolean hasCtor(){
        return hasCtor;
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
        return Objects.hash(className, memberPrimitives, memberClasses, methods);
    }

    public void buildCurrentMembers(String modifierString){

        if (    currentIds == null ||
                currentSymbolType == null ||
                currentSymbolType == SymbolType.PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }

        SymbolModifier mod = (modifierString.equals("private")) ? SymbolModifier.PRIVATE : SymbolModifier.PUBLIC;
        currentIds.forEach(id -> {
            switch (currentSymbolType){
                case CLASS:
                    memberClasses.add(new AbstractMap.SimpleEntry<>(mod, new SymbolClass(id)));
                    break;
                case PRIMITIVE:
                    SymbolVariable<?> variable = new SymbolVariable<>(currentSymbolPrimitiveType, 0, id);
                    memberPrimitives.add(new AbstractMap.SimpleEntry<>(mod, variable));
                    break;
                default:
                    System.exit(666);
            }
        });

        currentIds = null;
        currentModifier = null;
        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
    }

    public void addMethod(String modifierString, String name){
        if (    currentSymbolType == null ||
                currentSymbolType == SymbolType.PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if class is already there => name is not enough => function overloading
        SymbolModifier mod = (modifierString.equals("private")) ? SymbolModifier.PRIVATE : SymbolModifier.PUBLIC;
        // TODO check if already exists

        switch (currentSymbolType){
            case CLASS:
                currentMethod = new SymbolMethod(mod, name, SymbolType.CLASS, currentParams);
                break;
            case PRIMITIVE:
                currentMethod = new SymbolMethod(mod, name, SymbolType.PRIMITIVE , currentParams);
                break;
            default:
                System.exit(666);
        }

        methods.add(currentMethod);

        currentModifier = null;
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
}
