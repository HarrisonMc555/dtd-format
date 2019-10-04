package project.hsm.xml.dtdindent;

import java.util.*;
import java.util.stream.*;

public class ModelGroup {
    public Connector connector;
    public Occurrence occurrence;
    public List<Token> children;

    public ModelGroup(Connector connector, Occurrence occurrence) {
        this.connector = connector;
        this.occurrence = occurrence;
        this.children = new ArrayList<>();
    }

    public ModelGroup(Connector connector) {
        this(connector, Occurrence.ONCE);
    }

    public ModelGroup() {
        this(Connector.CHOICE);
    }

    public void addChildElement(ChildElement childElement) {
        children.add(new Token(childElement));
    }

    public void addChildModelGroup(ModelGroup modelGroup) {
        children.add(new Token(modelGroup));
    }

    public String asXML() {
        return "("
               + children.stream()
                         .map(Token::asXML)
                         .collect(Collectors.joining(
                                 connector.toSeparatorString()))
               + ")"
               + occurrence.toSymbol();
    }
}
