package at.tugraz.ist.cc;

import java.util.*;

public class SymbolClass {
    private final String className;
    private Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> memberPrimitives;
    private Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolClass>> memberClasses;
    private Collection<String> methods;


    private SymbolModifier currentModifier;
    private int type;
    List<String> currentIds;
    private String currentPrimitiveType;


    // TODO check if ctor is really necessary in assigment sheet
    private boolean hasCtor;

    public void setCurrentModifier(SymbolModifier currentModifier) {
        this.currentModifier = currentModifier;
    }

    public void setPrimitiveType(String currentPrimitiveType) {
        this.currentPrimitiveType = currentPrimitiveType;
    }

    public void setType(int type) {
        this.type = type;
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
                currentPrimitiveType == null ||
                currentModifier == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO remove magic values!
        //     private static final int TYPE_CLASS = 31;
        //    private static final int TYPE_PRIMITIVE = 32;
        SymbolModifier mod = (modifierString == "PRIVATE") ? SymbolModifier.PRIVATE : SymbolModifier.PUBLIC;
        currentIds.forEach(id -> {
            if (type == 31) {
                memberClasses.add(new AbstractMap.SimpleEntry<>(mod, new SymbolClass(id)));
            } else if(type == 32) {
                SymbolVariable<?> variable = null;
                switch (currentPrimitiveType){

                    case "int":
                        variable = new SymbolVariable<>(Type.INT, 0, id);
                        break;
                    case "string":
                        variable = new SymbolVariable<>(Type.STRING, 0, id);
                        break;
                    case "bool":
                        variable = new SymbolVariable<>(Type.BOOL, 0, id);
                        break;
                    default:
                        System.exit(-999);
                }

                memberPrimitives.add(new AbstractMap.SimpleEntry<>(mod, variable));
            } else {
                System.exit(666);
            }
        });

        currentIds = null;
        currentModifier = null;
        currentPrimitiveType = null;

    }
}
