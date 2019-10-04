package project.hsm.xml.dtdformat;

import java.util.*;
import java.util.stream.*;

public class Element {
    public String name;
    public ModelGroup modelGroup;
    public List<Attribute> attributes;
    public ContentModelType contentModelType;

    public Element(String name, ContentModelType contentModelType) {
        this.name = name;
        modelGroup = new ModelGroup(Connector.CHOICE);
        this.contentModelType = contentModelType;
        this.attributes = new ArrayList<>();
    }

    public String asXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!ELEMENT ").append(name).append(" ");
        switch (contentModelType) {
            case ANY:
                sb.append("ANY");
                break;
            case EMPTY:
                sb.append("EMPTY");
                break;
            case MIXED:
                String sep = Connector.CHOICE.toSeparatorString();
                sb.append("(#PCDATA")
                  .append(modelGroup.children.stream()
                                             .map(Token::asXML)
                                             .map(s -> sep + s)
                                             .collect(Collectors.joining()))
                  .append(")")
                  .append(modelGroup.occurrence.toSymbol());
                break;
            case CHILDREN:
                sb.append(modelGroup.asXML());
                break;
        }
        sb.append(">");
        return sb.toString();
    }

    public String attributesAsXML() {
        if (attributes.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<!ATTLIST ").append(name);
        for (Attribute a : attributes) {
            sb.append("\n        ").append(a.name).append(" ").append(a.type);
            if (a.enumeration != null && a.enumeration.length > 0) {
                sb.append("(")
                  .append(String.join(Connector.CHOICE.toSeparatorString(),
                                      a.enumeration))
                  .append(")");
            }
            if (a.use != AttributeUse.NORMAL) {
                sb.append(" ").append(a.use.toString());
            }
            if (a.defaultValue != null) {
                sb.append(" ").append(a.defaultValue);
            }
        }
        sb.append("\n        >");
        return sb.toString();
    }

    public boolean hasAttributes() {
        return !attributes.isEmpty();
    }
}
