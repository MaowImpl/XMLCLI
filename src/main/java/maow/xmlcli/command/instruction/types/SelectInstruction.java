package maow.xmlcli.command.instruction.types;

import maow.xmlcli.command.instruction.AbstractInstruction;
import org.dom4j.Document;
import org.dom4j.Element;

public class SelectInstruction extends AbstractInstruction {
    public SelectInstruction(String value, String text) {
        super(value, text);
    }

    @Override
    public String getType() {
        return "select";
    }

    @Override
    public void handle(Document document, Element active) {
        switch(value) {
            case "$PARENT": {
                if (active != null && active != document.getRootElement()) {
                    setActiveElement(active.getParent());
                }
                return;
            }
            case "$ROOT": {
                final Element root = document.getRootElement();
                if (root != null) {
                    setActiveElement(root);
                }
                return;
            }
            case "$CHILD": {
                if (active != null) {
                    if (active.elements().size() > 0) {
                        setActiveElement(active.elements().get(0));
                    }
                }
                return;
            }
            default: {
                if (active != null) {
                    if (text == null || text.equals("")) {
                        final Element element = active.element(value);
                        if (element != null) {
                            setActiveElement(element);
                        }
                    } else {
                        final int index = Integer.parseInt(text);
                        final Element element = active.elements(value).get(index);
                        if (element != null) {
                            setActiveElement(element);
                        }
                    }
                }
            }
        }
    }
}