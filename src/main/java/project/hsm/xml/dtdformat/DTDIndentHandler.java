package project.hsm.xml.dtdformat;

import com.sun.xml.dtdparser.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;

public class DTDIndentHandler extends DTDHandlerBase {
    private PrintStream out;
    private Map<String, Element> elements = new HashMap<>();
    private List<String> elementNames = new ArrayList<>();
    private Map<String, List<Attribute>> elementToAttributes = new HashMap<>();

    private Element curElement;
    private Deque<ModelGroup> modelGroupStack;

    DTDIndentHandler(OutputStream out) {
        this.out = new PrintStream(out);
    }

    public static String formatAttribute(
            String elementName, Attribute attribute) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!ATTLIST ")
          .append(elementName)
          .append(" ")
          .append(attribute.name)
          .append(" ")
          .append(attribute.type);
        if (attribute.enumeration != null && attribute.enumeration.length > 0) {
            sb.append("(")
              .append(String.join("|", attribute.enumeration))
              .append(")");
        }
        if (attribute.use != AttributeUse.NORMAL) {
            sb.append(" ").append(attribute.use.toString());
        }
        if (attribute.defaultValue != null) {
            sb.append(" ").append(attribute.defaultValue);
        }
        sb.append(">");
        return sb.toString();
    }

    @Override
    public void startDTD(InputEntity in) {
        out.println("Start DTD");
    }

    @Override
    public void startContentModel(
            String elementName, short contentModelTypeShort) {
        ContentModelType contentModelType =
                ContentModelType.fromShort(contentModelTypeShort);
        out.println(
                "startContentModel " + elementName + " " + contentModelType);
        curElement = new Element(elementName, contentModelType);
        modelGroupStack = new ArrayDeque<>();
        // For mixed, we don't get the "startModelGroup" message, so
        // we need to initialize the stack.
        if (contentModelType == ContentModelType.MIXED) {
            modelGroupStack.push(curElement.modelGroup);
        }
    }

    @Override
    public void endContentModel(
            String elementName, short contentModelTypeShort) {
        ContentModelType contentModelType =
                ContentModelType.fromShort(contentModelTypeShort);
        out.println("endContentModel " + elementName + " " + contentModelType);
        assert curElement.name.equals(elementName);
        assert curElement.contentModelType == contentModelType;
        assert modelGroupStack.size() == (
                curElement.contentModelType == ContentModelType.MIXED ? 1 : 0)
                : "All model groups should be complete when contnent model is done";
        // We should already have this
        // curElement.contentModelType = contentModelType;
        elements.put(elementName, curElement);
        elementNames.add(elementName);
        curElement = null;
        modelGroupStack = null;
    }

    @Override
    public void startModelGroup() {
        out.println("startModelGroup");
        out.print("\tBefore Stack: [");
        for (ModelGroup modelGroup : modelGroupStack) {
            out.print(" $" + modelGroup.asXML() + "$");
        }
        out.println(" ]");
        ModelGroup lastModelGroup = modelGroupStack.peekFirst();
        ModelGroup modelGroup;
        if (lastModelGroup == null) {
            // The first model group is implicitly the current element's main
            // model group.
            modelGroup = curElement.modelGroup;
        } else {
            modelGroup = new ModelGroup();
            lastModelGroup.addChildModelGroup(modelGroup);
        }
        modelGroupStack.push(modelGroup);
        out.print("\tAfter Stack: [");
        for (ModelGroup modelGroup2 : modelGroupStack) {
            out.print(" $" + modelGroup2.asXML() + "$");
        }
        out.println(" ]");
    }

    @Override
    public void endModelGroup(short occurenceShort) {
        out.println("endModelGroup " + Occurrence.fromShort(occurenceShort));
        out.print("\tBefore Stack: [");
        for (ModelGroup modelGroup : modelGroupStack) {
            out.print(" $" + modelGroup.asXML() + "$");
        }
        out.println(" ]");
        modelGroupStack.getFirst().occurrence =
                Occurrence.fromShort(occurenceShort);
        modelGroupStack.pop();
        out.print("\tAfter Stack: [");
        for (ModelGroup modelGroup : modelGroupStack) {
            out.print(" $" + modelGroup.asXML() + "$");
        }
        out.println(" ]");
    }

    @Override
    public void childElement(String elementName, short occurrenceShort) {
        Occurrence occurrence = Occurrence.fromShort(occurrenceShort);
        out.println("childElement " + elementName + " " + occurrence);
        modelGroupStack.getFirst()
                       .addChildElement(
                               new ChildElement(elementName, occurrence));
    }

    @Override
    public void mixedElement(String elementName) {
        out.println("mixedElement " + elementName);
        assert modelGroupStack.size() == 1
                : "Mixed element should be top level model group";
        modelGroupStack.getFirst()
                       .addChildElement(
                               new ChildElement(elementName, Occurrence.ONCE));
    }

    @Override
    public void connector(short connectorTypeShort) {
        out.println("connector " + Connector.fromShort(connectorTypeShort));
        modelGroupStack.getFirst().connector =
                Connector.fromShort(connectorTypeShort);
    }

    @Override
    public void attributeDecl(
            String elementName, String attributeName, String attributeType,
            String[] enumeration, short attributeUseShort,
            String defaultValue) {
        AttributeUse attributeUse = AttributeUse.fromShort(attributeUseShort);
        Attribute attribute =
                new Attribute(attributeName, attributeType, enumeration,
                              attributeUse, defaultValue);
        List<Attribute> attributes =
                elementToAttributes.computeIfAbsent(elementName,
                                                    k -> new ArrayList<>());
        attributes.add(attribute);
        out.print("attributeDecl "
                  + elementName
                  + " "
                  + attributeName
                  + " "
                  + attributeType
                  + " ");
        if (enumeration != null) {
            out.print("[ " + String.join(", ", enumeration) + "] ");
        }
        out.print(attributeUse + " " + defaultValue);
        out.println();
        //        out.println(formatAttribute(elementName, attribute));
    }

    private void addAttributesToElements() throws SAXException {
        for (Map.Entry<String, List<Attribute>> entry : elementToAttributes.entrySet()) {
            String elementName = entry.getKey();
            List<Attribute> attributes = entry.getValue();
            Element element = elements.get(elementName);
            if (element == null) {
                throw new SAXException("Attribute with unknown element \""
                                       + elementName
                                       + "\"", new NoSuchElementException(
                        "No element with name \"" + elementName + "\" found"));
            }
            element.attributes.addAll(attributes);
        }
    }

    @Override
    public void endDTD() throws SAXException {
        out.println("End DTD");
        addAttributesToElements();
        out.println("Elements:");
        for (String elementName : elementNames) {
            Element element = elements.get(elementName);
            out.println(element.asXML());
            if (element.hasAttributes()) {
                out.println(element.attributesAsXML());
            }
        }
        out.close();
    }
}
