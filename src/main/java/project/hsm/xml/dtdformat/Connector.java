package project.hsm.xml.dtdformat;

import com.sun.xml.dtdparser.*;

public enum Connector {
    CHOICE,
    SEQUENCE;

    public static Connector fromShort(short connector) {
        switch (connector) {
            case DTDEventListener.CHOICE:
                return CHOICE;
            case DTDEventListener.SEQUENCE:
                return SEQUENCE;
            default:
                throw new IllegalArgumentException("Invalid connector");
        }
    }

    public String toSeparatorString() {
        switch (this) {
            case CHOICE:
                return " | ";
            case SEQUENCE:
                return ", ";
            default:
                throw new RuntimeException("Non-exhaustive pattern match");
        }
    }
}
