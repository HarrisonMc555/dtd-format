package project.hsm.xml.dtdindent;

public class ChildElement {
    String elementName;
    Occurrence occurrence;

    public ChildElement(String elementName, Occurrence occurrence) {
        this.elementName = elementName;
        this.occurrence = occurrence;
    }

    public String asXML() {
        return elementName + occurrence.toSymbol();
    }
}
