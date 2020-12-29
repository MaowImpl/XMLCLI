package maow.xmlcli.command.instruction.types;

import maow.xmlcli.command.instruction.AbstractInstruction;
import maow.xmlcli.util.ConvertUtils;
import org.dom4j.Document;
import org.dom4j.Element;

public class ElementInstruction extends AbstractInstruction {
    public ElementInstruction(String value, String text) {
        super(value, text);
    }

    @Override
    public String getType() {
        return "element";
    }

    @Override
    public void handle(Document document, Element active) {
        if (active == null) {
            final Element element = document.addElement(value);
            document.setRootElement(element);
            setActiveElement(element);
        } else {
            final Element element = active.addElement(value);
            if (text != null && !text.equals("")) {
                final boolean setActive = ConvertUtils.convert(Boolean.class, text);
                if (setActive) {
                    setActiveElement(element);
                }
            }
        }
    }
}
