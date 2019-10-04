package project.hsm.xml.dtdindent;

import static com.sun.xml.dtdparser.DTDEventListener.*;

public enum Occurrence {
    ZERO_OR_MORE,
    ONE_OR_MORE,
    ZERO_OR_ONE,
    ONCE;

    public static Occurrence fromShort(short occurrence) {
        switch (occurrence) {
            case OCCURENCE_ZERO_OR_MORE:
                return ZERO_OR_MORE;
            case OCCURENCE_ONE_OR_MORE:
                return ONE_OR_MORE;
            case OCCURENCE_ZERO_OR_ONE:
                return ZERO_OR_ONE;
            case OCCURENCE_ONCE:
                return ONCE;
            default:
                throw new IllegalArgumentException("Invalid occurrence");
        }
    }

    public String toSymbol() {
        switch (this) {
            case ZERO_OR_MORE:
                return "*";
            case ONE_OR_MORE:
                return "+";
            case ZERO_OR_ONE:
                return "?";
            case ONCE:
                return "";
            default:
                throw new RuntimeException("Non-exhaustive pattern match");
        }
    }
}
