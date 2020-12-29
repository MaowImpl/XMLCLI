package maow.xmlcli.command.instruction.types;

import maow.xmlcli.command.instruction.AbstractInstruction;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

public class AttributeInstruction extends AbstractInstruction {
    public AttributeInstruction(String value, String text) {
        super(value, text);
    }

    @Override
    public String getType() {
        return "attribute";
    }

    @Override
    public void handle(Document document, Element active) {
        if (active != null) {
            if (value.startsWith("$REMOVE:")) {
                final Attribute attribute = active.attribute(value.substring(8));
                if (attribute != null) {
                    active.remove(attribute);
                }
                return;
            }
            active.addAttribute(value, text);
        }
    }
}
