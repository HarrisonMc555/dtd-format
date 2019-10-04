package project.hsm.xml.dtdindent;

import static com.sun.xml.dtdparser.DTDEventListener.*;

public enum AttributeUse {
    NORMAL,
    IMPLIED,
    FIXED,
    REQUIRED;

    public static AttributeUse fromShort(short attributeUse) {
        switch (attributeUse) {
            case USE_NORMAL:
                return NORMAL;
            case USE_IMPLIED:
                return IMPLIED;
            case USE_FIXED:
                return FIXED;
            case USE_REQUIRED:
                return REQUIRED;
            default:
                throw new IllegalArgumentException("Invalid attribute use");
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case NORMAL:
                return "";
            case IMPLIED:
                return "#IMPLIED";
            case FIXED:
                return "#FIXED";
            case REQUIRED:
                return "#REQUIRED";
            default:
                throw new RuntimeException("Non-exhaustive pattern match");
        }
    }
}
