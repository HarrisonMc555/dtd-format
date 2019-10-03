package project.hsm.xml.dtdindent;

class Attribute {
    String elementName;
    String attributeName;
    String attributeType;
    String[] enumeration;
    short attributeUse;
    String defaultValue;

    Attribute(String elementName, String attributeName, String attributeType,
                     String[] enumeration, short attributeUse, String defaultValue) {
        this.elementName = elementName;
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.enumeration = enumeration;
        this.attributeUse = attributeUse;
        this.defaultValue = defaultValue;
    }
}
