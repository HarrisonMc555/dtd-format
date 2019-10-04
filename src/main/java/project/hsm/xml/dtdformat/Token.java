package project.hsm.xml.dtdformat;

public class Token {
    private ChildElement childElement;
    private ModelGroup modelGroup;

    public Token(ChildElement childElement) {
        this.childElement = childElement;
    }

    public Token(ModelGroup modelGroup) {
        this.modelGroup = modelGroup;
    }

    public boolean isChildElement() {
        return childElement != null;
    }

    public boolean isModelGroup() {
        return modelGroup != null;
    }

    public ChildElement getChildElement() {
        assert childElement != null;
        return childElement;
    }

    public ModelGroup getModelGroup() {
        assert modelGroup != null;
        return modelGroup;
    }

    public String asXML() {
        if (isModelGroup()) {
            return modelGroup.asXML();
        } else {
            return childElement.asXML();
        }
    }
}
