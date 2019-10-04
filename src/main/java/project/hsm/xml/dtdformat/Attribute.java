package project.hsm.xml.dtdformat;

class Attribute {
    String name;
    String type;
    String[] enumeration;
    AttributeUse use;
    String defaultValue;

    Attribute(
            String name, String type, String[] enumeration, AttributeUse use,
            String defaultValue) {
        this.name = name;
        this.type = type;
        this.enumeration = enumeration;
        this.use = use;
        this.defaultValue = defaultValue;
    }
}
