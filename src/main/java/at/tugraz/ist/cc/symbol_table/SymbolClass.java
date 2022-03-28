package at.tugraz.ist.cc.symbol_table;

import at.tugraz.ist.cc.JovaParser;
import at.tugraz.ist.cc.TypeCheckerJovaVisitorImpl;
import at.tugraz.ist.cc.error.ErrorHandler;

import java.util.*;
import java.util.stream.Collectors;

import static at.tugraz.ist.cc.symbol_table.SymbolType.CLASS;
import static at.tugraz.ist.cc.symbol_table.SymbolType.PRIMITIVE;

public class SymbolClass {
    public static final String MAIN_CLASS_NAME = "Main";
    private final String className;
    private final Collection<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> members;
    private final Collection<SymbolMethod> methods;
    private final Collection<SymbolConstructor> constructors;

    // TODO: maybe move the current things to the TypeCheckerJovaVisitorImpl or to a new singleton class?
    private SymbolType currentSymbolType;
    private SymbolPrimitiveType currentSymbolPrimitiveType;
    private String currentClassName;
    private Collection<String> currentIds;
    private SymbolMethod currentMethod;
    private SymbolConstructor currentConstructor;
    private SymbolVariable currentMemberAccess;
    private List<SymbolVariable> currentParams;
    private List<SymbolVariable> currentArgList;

    public SymbolClass(String name) {
        className = name;
        this.members = new ArrayList<>();
        this.methods = new ArrayList<>();
        currentParams = new ArrayList<>();
        currentIds = new ArrayList<>();
        constructors = new ArrayList<>();
        currentMemberAccess = null;

        // needs to be 0 to signal that no method-invocation is currently checked
        currentArgList = null;
    }

    public void  buildCurrentMembers(SymbolModifier modifier, JovaParser.Member_declContext ctx){

        if (    currentIds == null ||
                currentSymbolType == null ||
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
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
                    System.exit(666);
            }


            if(members.stream().anyMatch(element -> element.getValue().getName().equals(id))) {
                ErrorHandler.INSTANCE.addMemberDoubleDefError(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        id, typeName, modifier.toString());
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

    public int addMethod(SymbolModifier modifier, String name,JovaParser.Method_headContext ctx){
        if (    currentSymbolType == null ||
                currentSymbolType == PRIMITIVE && currentSymbolPrimitiveType == null) {
            // should be non reachable!!!!
            System.exit(-999);
        }
        // TODO check if class is already there => name is not enough => function overloading
        // TODO check if already exists
        SymbolMethod symbolMethod = null;
        int errorOccurred = 0;
        switch (currentSymbolType){
            case CLASS:
                Optional<SymbolClass> foundClass = SymbolTable.getInstance().getClassByName(currentClassName, ctx);

                if (foundClass.isPresent()){
                    symbolMethod = new SymbolMethod(modifier, name, new SymbolVariable(SymbolType.CLASS, foundClass.get(), null), currentParams);
                } else {
                    // TODO: I think we have to continue parsing the params for right logging => so the return would be false?
                    errorOccurred = TypeCheckerJovaVisitorImpl.ERROR_ID_UNDEF;
                }

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

        // TODO it might be the case that the output order of the errors is not right.
        if ( className.equals(SymbolClass.MAIN_CLASS_NAME) &&
                    (symbolMethod.getAccessSymbol() != SymbolModifier.PUBLIC ||
                    !(symbolMethod.getReturnValue().getActualType() instanceof SymbolPrimitiveType) ||
                    symbolMethod.getReturnValue().getActualType() != SymbolPrimitiveType.INT)){
            ErrorHandler.INSTANCE.addMainMemberError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
            return TypeCheckerJovaVisitorImpl.ERROR_MAIN_WITH_WRONG_METHOD;
        }

        if(symbolMethod.checkParamDoubleDeclaration(ctx.params().param_list()) != 0) {
            errorOccurred = TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_VARIABLE;
        }

        if (methods.contains(symbolMethod)) {
            ErrorHandler.INSTANCE.addMethodDoubleDefError(
                    ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    symbolMethod.getName(), className, symbolMethod.getParamTypesAsString());
            errorOccurred = TypeCheckerJovaVisitorImpl.ERROR_DOUBLE_DECLARATION_METHOD;
        }

        if (errorOccurred == 0) {
            methods.add(symbolMethod);
            currentMethod = symbolMethod;
        }

        return errorOccurred;
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
                    System.exit(666);
            }

            if( currentMethod.getParams().stream().anyMatch(element -> element.getName().equals(id)) ||
                currentMethod.getLocalVariables().stream().anyMatch(element -> element.getName().equals(id))) {
                ErrorHandler.INSTANCE.addVarDoubleDefError(
                        ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                        id, typeName, currentMethod.getName(),
                        currentMethod.getParamTypesAsString());
                continue;
            }

            if (symbolVariable != null) {
                if (currentSymbolType == CLASS && currentClassName.equals(SymbolClass.MAIN_CLASS_NAME)) {
                    ErrorHandler.INSTANCE.addMainInstatiationError(ctx.start.getLine(), ctx.start.getCharPositionInLine());
                    continue;
                }
                currentMethod.addVariable(symbolVariable);
            }
        }

        currentSymbolPrimitiveType = null;
        currentSymbolType = null;
        currentParams = new ArrayList<>();
        return 0;
    }

    public int buildConstructor(String className, JovaParser.CtorContext ctx) {
        if (!this.className.equals(className)) {
            ; // TODO which error?
        }


        SymbolConstructor symbolConstructor = new SymbolConstructor(className, currentParams);

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
            currentConstructor = symbolConstructor;
        }

        currentParams = new ArrayList<>();

        return errorOccurred;
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

    public SymbolVariable getMemberById(String id){
        Optional<AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable>> found = members.stream().filter(element -> element.getValue().getName().equals(id)).findFirst();
        return found.get().getValue();
    }
    public String getClassName() {
        return className;
    }

    public SymbolVariable getCurrentMemberAccess() {
        return currentMemberAccess;
    }

    public void setCurrentMemberAccess(SymbolVariable currentMemberAccess) {
        this.currentMemberAccess = currentMemberAccess;
    }

    public SymbolClass getCurrentClassAccess() {
        if (getCurrentMemberAccess() != null) {
            return (SymbolClass) getCurrentMemberAccess().getActualType();
        }
        return this;
    }

    public Collection<SymbolMethod> getMatchingMethods(String method) {
        return methods.stream().filter(element -> element.getName().equals(method)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addPrimitiveArgument(SymbolPrimitiveType type) {
        currentArgList.add(new SymbolVariable(PRIMITIVE, type, ""));
    }

    public boolean checkIfVariableExists(String arg) {
        SymbolVariable var = getCurrentScopeVariable(arg);

        if (var == null) {
            return false;
        }

        if (currentlyGatheringArguments()) {
            currentArgList.add(var);
        }
        return true;
    }

    public void addArgument(SymbolVariable var) {
        currentArgList.add(var);
    }

    public SymbolVariable getCurrentScopeVariable(String name) {
        SymbolVariable var = null;

        try {
            AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable> member_entry = getMemberIfExists(name);
            if (member_entry != null) {
                var = member_entry.getValue();
            }
        } catch (IndexOutOfBoundsException ex) { }

        if (var == null) {
            var = getLocalVariable(name);
        }

        return var;
    }

    public AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable> getMemberIfExists(String name) {
        AbstractMap.SimpleEntry<SymbolModifier, SymbolVariable> var = null;
        try {
             var = members.stream().filter(element -> element.getValue().getName().equals(name))
                    .collect(Collectors.toCollection(ArrayList::new)).get(0);
        } catch (Exception ex) {}

        return var;
    }

    private SymbolVariable getLocalVariable(String name) {
        return currentMethod.getMethodVariable(name);
    }


    public boolean checkValidArgList(SymbolMethod method, List<SymbolVariable> argList) {
        int size = method.getParams().size();

        if (size != argList.size()) {
            return false;
        }

        if (size == 0) {
            return true;
        }

        for (int i = 0; i < size; i++) {
            if (!(method.getParams().get(i).getType() == argList.get(i).getType()
                    && method.getParams().get(i).getActualType() == argList.get(i).getActualType())) {// TODO check if == works
                return false;
            }
        }

        return true;
    }

    public void resetArgList() {
        currentArgList = null;
    }

    public void setArgList(List<SymbolVariable> list) {
        currentArgList = list;
    }

    public List<SymbolVariable> getCurrentArgList() {
        return currentArgList;
    }

    public boolean currentlyGatheringArguments() {
        return (currentArgList != null);
    }

    public String getArgListTypes()
    {
        StringBuilder types = new StringBuilder();

        for (SymbolVariable var : currentArgList) {
            types.append(var.getActualType().toString().toLowerCase());
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

    public SymbolMethod getCurrentMethod() {return currentMethod;}

}
