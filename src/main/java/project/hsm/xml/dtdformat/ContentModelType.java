package project.hsm.xml.dtdformat;

import static com.sun.xml.dtdparser.DTDEventListener.*;

public enum ContentModelType {
    EMPTY,
    ANY,
    MIXED,
    CHILDREN;

    public static ContentModelType fromShort(short contentModelType) {
        switch (contentModelType) {
            case CONTENT_MODEL_EMPTY:
                return EMPTY;
            case CONTENT_MODEL_ANY:
                return ANY;
            case CONTENT_MODEL_MIXED:
                return MIXED;
            case CONTENT_MODEL_CHILDREN:
                return CHILDREN;
            default:
                throw new IllegalArgumentException(
                        "Invalid content model type");
        }
    }
}
