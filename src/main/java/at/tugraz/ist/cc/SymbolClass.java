package at.tugraz.ist.cc;

import java.util.*;

public class SymbolClass {
    private final String className;
    private Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> memberPrimitives;
    private Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolClass>> memberClasses;
    private Collection<SymbolMethod> methods;


    private SymbolModifier currentModifier;
    private int currentType;
    List<String> currentIds;
    private String currentPrimitiveType;
    private String currentClassType;
    private SymbolMethod currentMethod;
    private List<Object> currentParams;

    public void setCurrentParams(List<Object> currentParams) {
        this.currentParams = currentParams;
    }

    public SymbolMethod getCurrentMethod() {
        return currentMethod;
    }


    // TODO check if ctor is really necessary in assigment sheet
    private boolean hasCtor;

    public void setCurrentModifier(SymbolModifier currentModifier) {
        this.currentModifier = currentModifier;
    }

    public void setPrimitiveType(String currentPrimitiveType) {
        this.currentPrimitiveType = currentPrimitiveType.toUpperCase();
    }

    public void setCurrentClassType(String currentClassType) {
        this.currentClassType = currentClassType;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
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
                currentType == 0 ||
                currentType == 32 && currentPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO remove magic values!
        //     private static final int TYPE_CLASS = 31;
        //    private static final int TYPE_PRIMITIVE = 32;
        SymbolModifier mod = (modifierString.equals("private")) ? SymbolModifier.PRIVATE : SymbolModifier.PUBLIC;
        currentIds.forEach(id -> {
            if (currentType == 31) {
                memberClasses.add(new AbstractMap.SimpleEntry<>(mod, new SymbolClass(id)));
            } else if(currentType == 32) {
                SymbolVariable<?> variable = new SymbolVariable<>(PrimitveType.valueOf(currentPrimitiveType), 0, id);
                memberPrimitives.add(new AbstractMap.SimpleEntry<>(mod, variable));
            } else {
                System.exit(666);
            }
        });

        currentIds = null;
        currentModifier = null;
        currentPrimitiveType = null;
        currentType = 0;

    }

    public void addMethod(String modifierString, String name){
        if (
                currentType == 0 ||
                currentType == 32 && currentPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if class is already there => name is not enough => function overloading
        SymbolModifier mod = (modifierString.equals("private")) ? SymbolModifier.PRIVATE : SymbolModifier.PUBLIC;
        // TODO check if already exists

        SymbolMethod method = null;
        if (currentType == 31) {
            method = new SymbolMethod(mod, name, Type.CLASS, currentParams);
        } else if(currentType == 32) {
            method = new SymbolMethod(mod, name,Type.PRIMITIVE , currentParams);
        }

        methods.add(method);
        currentMethod = method;

        currentModifier = null;
        currentPrimitiveType = null;
        currentType = 0;
        currentParams = new ArrayList<>();
    }

    public void saveLocalVariables()
    {
        for (String s : currentIds) {

            if (currentType == 31) {
                currentMethod.addVariable(SymbolTable.getInstance().getClassByName(currentClassType));
            } else if(currentType == 32) {
                currentMethod.addVariable(new SymbolVariable<>(PrimitveType.valueOf(currentPrimitiveType), 0, s));
            }
        }

        currentPrimitiveType = null;
        currentType = 0;
        currentParams = new ArrayList<>();
    }
}
