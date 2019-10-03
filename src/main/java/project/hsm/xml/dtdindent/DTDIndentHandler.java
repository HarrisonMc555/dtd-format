package project.hsm.xml.dtdindent;

import com.sun.xml.dtdparser.DTDHandlerBase;
import com.sun.xml.dtdparser.InputEntity;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DTDIndentHandler extends DTDHandlerBase
{
    private PrintStream out;
    private Map<String,Vector<Attribute>> elementToAttribute = new HashMap<>();

    DTDIndentHandler(OutputStream out)
    {
        this.out = new PrintStream(out);
    }

    @Override
    public void startDTD(InputEntity in)
    {
        out.println("Start DTD");
    }

    @Override
    public void attributeDecl(String elementName, String attributeName,
                              String attributeType,
                              String[] enumeration, short attributeUse,
                              String defaultValue)
    {
        Attribute attribute = new Attribute(elementName, attributeName,
                attributeType, enumeration, attributeUse, defaultValue);
        Vector<Attribute> attributes = elementToAttribute
                .computeIfAbsent(elementName, k -> new Vector<>());
        attributes.add(attribute);
        out.print("<!ATTLIST " + elementName + " " + attributeName + " "
                + attributeType);
        if (enumeration != null && enumeration.length > 0)
        {
            out.print("(" + String.join("|", enumeration) + ")");
        }
        if (attributeUse != USE_NORMAL)
        {
            out.print(" " + attributeUseToString(attributeUse));
        }
        if (defaultValue != null && !defaultValue.isEmpty())
        {
            out.print(" " + defaultValue);
        }
        out.println(">");
    }

    private static String attributeUseToString(short attributeUse)
    {
        switch (attributeUse)
        {
            case USE_NORMAL:
                return "";
            case USE_IMPLIED:
                return "#IMPLIED";
            case USE_FIXED:
                return "#FIXED";
            case USE_REQUIRED:
                return "#REQUIRED";
            default:
                throw new IllegalArgumentException("Invalid attribute use");
        }
    }

    @Override
    public void endDTD()
    {
        out.println("End DTD");
        out.close();
    }
}
