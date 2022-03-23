package at.tugraz.ist.cc.symbol_table;

import java.util.*;
import java.util.stream.Collectors;

import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class SymbolClass {
    private final String className;
    private final Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> member;
    private final Collection<SymbolMethod> methods;

    // TODO: maybe move the current things to the TypeCheckerJovaVisitorImpl or to a new singleton class?
    private SymbolType currentSymbolType;
    private SymbolPrimitiveType currentSymbolPrimitiveType;
    private String currentClassName;
    private Collection<String> currentIds;
    private SymbolMethod currentMethod;
    private List<SymbolVariable> currentParams;
    private List<SymbolVariable> currentArgList;

    public SymbolClass(String name) {
        className = name;
        this.member = new ArrayList<>();
        this.methods = new ArrayList<>();
        currentParams = new ArrayList<>();
        currentIds = new ArrayList<>();
        currentArgList = new ArrayList<>();
    }

    public void buildCurrentMembers(SymbolModifier modifier){

        if (    currentIds == null ||
                currentSymbolType == null ||
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if member already with same name already exists
        currentIds.forEach(id -> {
            switch (currentSymbolType){
                case CLASS:
                    member.add(new AbstractMap.SimpleEntry<>(modifier,
                            new SymbolVariable(SymbolType.CLASS, SymbolTable.getInstance().getClassByName(currentClassName), id)));
                    break;
                case PRIMITIVE:
                    member.add(new AbstractMap.SimpleEntry<>(modifier,
                            new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, id)));
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
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if class is already there => name is not enough => function overloading
        // TODO check if already exists

        switch (currentSymbolType){
            case CLASS:
                currentMethod = new SymbolMethod(modifier, name, new SymbolVariable(SymbolType.CLASS, SymbolTable.getInstance().getClassByName(currentClassName), null), currentParams);
                break;
            case PRIMITIVE:
                currentMethod = new SymbolMethod(modifier, name, new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, null) , currentParams);
                break;
            default:
                System.exit(666);
        }

        methods.add(currentMethod);

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentParams = new ArrayList<>();
    }

    public void saveLocalVariables() {
        for (String id : currentIds) {
            switch (currentSymbolType) {
                case CLASS:
                    currentMethod.addVariable(new SymbolVariable(SymbolType.CLASS, SymbolTable.getInstance().getClassByName(currentClassName), id));
                    break;
                case PRIMITIVE:
                    currentMethod.addVariable(new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, id));
                    break;
                default:
                    System.exit(666);
            }
        }

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentClassName = null;
        currentParams = new ArrayList<>();
    }

    public Collection<SymbolMethod> getMatchingMethods(String method) {
        return methods.stream().filter(element -> element.getName().equals(method)).collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean checkIfVariableExists(String arg) {
        SymbolVariable var = getCurrentScopeVariable(arg);

        if (var == null) {
            return false;
        }

        currentArgList.add(var);
        return true;
    }

    private SymbolVariable getCurrentScopeVariable(String name) {
        SymbolVariable var = null;

        try {
            var = getMember(name);
        } catch (IndexOutOfBoundsException ex) {

        }

        if (var == null) {
            var = getLocalVariable(name);
        }

        return var;
    }

    private SymbolVariable getMember(String name) {
        return member.stream().filter(element -> element.getValue().name.equals(name))
                .collect(Collectors.toCollection(ArrayList::new)).get(0).getValue();
    }

    private SymbolVariable getLocalVariable(String name) {
        return currentMethod.getMethodVariable(name);
    }


    public boolean checkValidArgList(SymbolMethod method) {
        int size = method.getParams().size();

        if (size != currentArgList.size()) {
            return false;
        }

        if (size == 0) {
            return true;
        }

        for (int i = 0; i < size; i++) {
            if (method.getParams().get(i).type == currentArgList.get(i).type
                && method.getParams().get(i).actualType == currentArgList.get(i).actualType)
                return true;

        }

        return false;
    }

    public void resetArgList() {
        currentArgList = new ArrayList<>();
    }

    public String getArgListTypes()
    {
        StringBuilder types = new StringBuilder();

        for (SymbolVariable var : currentArgList) {
            types.append(var.actualType.toString().toLowerCase());
            types.append(" ");
        }

        types.delete(types.length() -1, types.length());

        return types.toString();
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

    public void setCurrentParams(List<SymbolVariable> currentParams) {
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
