package at.tugraz.ist.cc.instructions;

public enum OperatorTypes {
    ADD("+"), SUB("-"), MUL("*"), DIV("/"), MOD("%"), GREATER(">"), SMALLER("<"),
    GREATER_EQUAL(">="), SMALLER_EQUAL("<="), EQUAL("=="), UNEQUAL("!="), AND("&&"), OR("||"),
    NOT("!");

    private String value;

    OperatorTypes(String value) {
        this.value = value;
    }
}
