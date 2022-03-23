package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.TypeCheckerJovaVisitorImpl;
import at.tugraz.ist.cc.error.ErrorHandler;

import java.util.*;

import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class SymbolClass {
    private final String className;
    private final Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> members;
    private final Collection<SymbolMethod> methods;

    // TODO: maybe move the current things to the TypeCheckerJovaVisitorImpl or to a new singleton class?
    private SymbolType currentSymbolType;
    private SymbolPrimitiveType currentSymbolPrimitiveType;
    private String currentClassName;
    private Collection<String> currentIds;
    private SymbolMethod currentMethod;
    private List<SymbolVariable> currentParams;

    public SymbolClass(String name) {
        className = name;
        this.members = new ArrayList<>();
        this.methods = new ArrayList<>();
        currentParams = new ArrayList<>();
        currentIds = new ArrayList<>();
    }

    public int buildCurrentMembers(SymbolModifier modifier, JovaParser.Member_declContext ctx){

        if (    currentIds == null ||
                currentSymbolType == null ||
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if member already with same name already exists
        for (String id : currentIds) {
            SymbolVariable member = null;
            switch (currentSymbolType){
                case CLASS:
                    member = new SymbolVariable(SymbolType.CLASS, SymbolTable.getInstance().getClassByName(currentClassName), id);
                    break;
                case PRIMITIVE:
                    member = new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, id);
                    break;
                default:
                    System.exit(666);
            }

            SymbolVariable finalMemberHelper = member;
            if(members.stream().anyMatch(element -> element.getValue().equals(finalMemberHelper))) {
                ErrorHandler.INSTANCE.addMemberDoubleDefError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        member.getName(), member.getTypeAsString(), modifier.toString());
                currentIds = null;
                currentSymbolPrimitiveType = null;
                currentSymbolType = null;

                return TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_VARIABLE;
            }

            members.add(new AbstractMap.SimpleEntry<>(modifier, member));
        }

        currentIds = null;
        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        return 0;
    }

    public int addMethod(SymbolModifier modifier, String name,JovaParser.Method_headContext ctx){
        if (    currentSymbolType == null ||
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if class is already there => name is not enough => function overloading
        // TODO check if already exists
        SymbolMethod symbolMethod = null;
        switch (currentSymbolType){
            case CLASS:
                symbolMethod = new SymbolMethod(modifier, name, new SymbolVariable(SymbolType.CLASS, SymbolTable.getInstance().getClassByName(currentClassName), null), currentParams);
                break;
            case PRIMITIVE:
                symbolMethod = new SymbolMethod(modifier, name, new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, null) , currentParams);
                break;
            default:
                System.exit(666);
        }

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentParams = new ArrayList<>();

        boolean found = methods.contains(symbolMethod);
        if (found) {
            ErrorHandler.INSTANCE.addMethodDoubleDefError(
                    ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    symbolMethod.getName(), className, symbolMethod.getParamTypesAsString());
            return TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_METHOD;
        }

        if(symbolMethod.checkParamDoubleDeclaration(ctx.params().param_list()) != 0) {
            return TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_VARIABLE;
        }

        methods.add(symbolMethod);
        currentMethod = symbolMethod;
        return 0;
    }

    public void saveLocalVariables()
    {
        for (String id : currentIds) {
            switch (currentSymbolType){
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
        currentParams = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, members, methods);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolClass that = (SymbolClass) o;
        return Objects.equals(className, that.className);
    }
}
