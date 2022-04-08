package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.TypeCheckerJovaVisitorImpl;
import at.tugraz.ist.cc.error.ErrorHandler;

import java.util.*;
import java.util.stream.Collectors;

import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class SymbolClass {
    public static final String MAIN_CLASS_NAME = "Main";
    private final String className;
    private final Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> members;
    private final Collection<SymbolMethod> methods;
    private final Collection<SymbolConstructor> constructors;

    private SymbolType currentSymbolType;
    private SymbolPrimitiveType currentSymbolPrimitiveType;
    private String currentClassName;
    private Collection<String> currentIds;
    private SimpleCallable currentCallable;
    private List<SymbolVariable> currentParams;
    private List<SymbolVariable> currentArgList;
    public SymbolVariable currentSymbolVariable;

    public SymbolClass(String name) {
        className = name;
        this.members = new ArrayList<>();
        this.methods = new ArrayList<>();
        currentParams = new ArrayList<>();
        currentIds = new ArrayList<>();
        constructors = new ArrayList<>();

        // needs to be 0 to signal that no method-invocation is currently checked
        currentArgList = null;
    }

    public void  buildCurrentMembers(SymbolModifier modifier, JovaParser.Member_declContext ctx){

        if (    currentIds == null ||
                currentSymbolType == null ||
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(70);
        }

        for (String id : currentIds) {
            SymbolVariable member = null;
            String typeName = null;

            switch (currentSymbolType){
                case CLASS:
                    typeName = currentClassName;
                    Optional<SymbolClass> foundClass = SymbolTable.getInstance().getClassByName(currentClassName, ctx);

                    if (foundClass.isPresent()){
                        member = new SymbolVariable(SymbolType.CLASS, foundClass.get(), id);
                    }

                    break;
                case PRIMITIVE:
                    typeName = currentSymbolPrimitiveType.toString();
                    member = new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, id);
                    break;
                default:
                    System.exit(71);
            }


            if(members.stream().anyMatch(element -> element.getValue().getName().equals(id))) {
                ErrorHandler.INSTANCE.addMemberDoubleDefError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        id, typeName, className);
            } else {
                if (member != null){
                    members.add(new AbstractMap.SimpleEntry<>(modifier, member));
                }
            }
        }

        currentIds = null;
        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
    }

    public int addMethod(SymbolModifier modifier, String name, JovaParser.Method_headContext ctx){
        if (    currentSymbolType == null ||
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(72);
        }
        SymbolMethod symbolMethod = null;
        int errorOccurred = 0;
        switch (currentSymbolType){
            case CLASS:
                Optional<SymbolClass> foundClass = SymbolTable.getInstance().getClassByName(currentClassName, ctx);
                symbolMethod = new SymbolMethod(modifier, name, new SymbolVariable(SymbolType.CLASS, foundClass.get(), "returnValue"), currentParams);
                break;

            case PRIMITIVE:
                symbolMethod = new SymbolMethod(modifier, name, new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, "returnValue") , currentParams);
                break;

            default:
                System.exit(73);
        }

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentParams = new ArrayList<>();



        if(symbolMethod.checkParamDoubleDeclaration(ctx.params().param_list()) != 0) {
            errorOccurred = TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_VARIABLE;
        }

        if (SymbolMethod.IO_METHODS.contains(symbolMethod) || methods.contains(symbolMethod)) {
            ErrorHandler.INSTANCE.addMethodDoubleDefError( ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    symbolMethod.getName(), className, symbolMethod.getParamTypesAsString());
            return TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_METHOD;
        }

        if ( className.equals(SymbolClass.MAIN_CLASS_NAME) &&
                (symbolMethod.getAccessSymbol() != SymbolModifier.PUBLIC ||
                        !symbolMethod.getName().equals(SymbolMethod.MAIN_METHOD_NAME) ||
                        !(symbolMethod.getReturnValue().getActualType() instanceof SymbolPrimitiveType) ||
                        symbolMethod.getReturnValue().getActualType() != SymbolPrimitiveType.INT)){
            ErrorHandler.INSTANCE.addMainMemberError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
            return TypeCheckerJovaVisitorImpl.ERROR_MAIN_WITH_WRONG_METHOD;
        }

        if (errorOccurred == 0) {
            methods.add(symbolMethod);
            currentCallable = symbolMethod;
        }

        return errorOccurred;
    }

    public SymbolVariable buildParam (String variableName, JovaParser.Param_listContext ctx){
        switch (currentSymbolType){
            case CLASS:
                Optional<SymbolClass> foundClass = SymbolTable.getInstance().getClassByName(currentClassName, ctx);
                return new SymbolVariable(SymbolType.CLASS, foundClass.get(), variableName);

            case PRIMITIVE:
                SymbolPrimitiveType symbolPrimitiveType = currentSymbolPrimitiveType;
                return new SymbolVariable(SymbolType.PRIMITIVE, symbolPrimitiveType, variableName);

            default:
                System.exit(74);
                return null;
        }
    }

    public int saveLocalVariables(JovaParser.DeclarationContext ctx)
    {
        for (String id : currentIds) {
            SymbolVariable symbolVariable = null;
            String typeName = null;

            switch (currentSymbolType) {
                case CLASS:
                    typeName = currentClassName;
                    Optional<SymbolClass> foundClass = SymbolTable.getInstance().getClassByName(currentClassName, ctx);

                    if (foundClass.isPresent()){
                        symbolVariable = new SymbolVariable(SymbolType.CLASS, foundClass.get(), id);
                    }

                    break;
                case PRIMITIVE:
                    typeName = currentSymbolPrimitiveType.toString();
                    symbolVariable = new SymbolVariable(SymbolType.PRIMITIVE, currentSymbolPrimitiveType, id);
                    break;
                default:
                    System.exit(75);
            }

            if( currentCallable.getParams().stream().anyMatch(element -> element.getName().equals(id)) ||
                currentCallable.getLocalVariables().stream().anyMatch(element -> element.getName().equals(id))) {
                ErrorHandler.INSTANCE.addVarDoubleDefError(
                        ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        id, typeName, currentCallable.getName(),
                        currentCallable.getParamTypesAsString());
                continue;
            }

            if (symbolVariable != null) {
                currentCallable.addVariable(symbolVariable);
            }
        }

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentParams = new ArrayList<>();
        return 0;
    }

    public int buildConstructor(String className, JovaParser.CtorContext ctx) {
        SymbolConstructor symbolConstructor = new SymbolConstructor(this, currentParams);

        int errorOccurred = 0;
        if(symbolConstructor.checkParamDoubleDeclaration(ctx.params().param_list()) != 0) {
            errorOccurred = TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_VARIABLE;
        }

        if (constructors.contains(symbolConstructor)) {
            ErrorHandler.INSTANCE.addMethodDoubleDefError(
                    ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    className, className, symbolConstructor.getParamTypesAsString());
            errorOccurred = TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_METHOD;
        }

        if (errorOccurred == 0) {
            constructors.add(symbolConstructor);
            currentCallable = symbolConstructor;
        }

        currentParams = new ArrayList<>();

        return errorOccurred;
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

    public Collection<SymbolConstructor> getConstructors() {
        return constructors;
    }

    public void addArgument(SymbolVariable var) {
        currentArgList.add(var);
    }

    public SymbolVariable getCurrentScopeVariable(String name) {
        SymbolVariable var = null;

        var = getLocalVariable(name);
        if (var == null) {
            Optional<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> member_entry = getMemberIfExists(name);
            if (member_entry.isPresent()) {
                var = member_entry.get().getValue();
            }
        }

        return var;
    }

    public Optional<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> getMemberIfExists(String name) {
        return members.stream().filter(element -> element.getValue().getName().equals(name)).findFirst();
    }

    public Collection<SymbolMethod> getMethods() {
        return methods;
    }

    private SymbolVariable getLocalVariable(String name) {
        return currentCallable.getMethodVariable(name);
    }

    public void setArgList(List<SymbolVariable> list) {
        currentArgList = list;
    }

    public List<SymbolVariable> getCurrentArgList() {
        return currentArgList;
    }

    public SimpleCallable getCurrentCallable() {
        return currentCallable;
    }

    public SymbolType getCurrentSymbolType() {
        return currentSymbolType;
    }

    public SymbolPrimitiveType getCurrentSymbolPrimitiveType() {
        return currentSymbolPrimitiveType;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolClass that = (SymbolClass) o;
        return Objects.equals(className, that.className);
    }

    public Optional<SymbolMethod> getMatchingMethod(String methodName, List<SymbolVariable> currentArgList) {
        Optional<SymbolMethod> foundMethod = methods.stream()
               .filter(method -> method.getName().equals(methodName) && method.checkValidArgList(currentArgList))
               .findAny();

        if (foundMethod.isEmpty()) {
            foundMethod = SymbolMethod.IO_METHODS.stream()
                    .filter(method -> method.getName().equals(methodName) && method.checkValidArgList(currentArgList))
                    .findAny();
        }

        return foundMethod;
    }
}
