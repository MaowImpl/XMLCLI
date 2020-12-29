package maow.xmlcli.command.instruction.types;

import maow.xmlcli.command.instruction.AbstractInstruction;
import org.dom4j.*;

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
                selectParent(active, document);
                return;
            }
            case "$ROOT": {
                selectRoot(document);
                return;
            }
            default: {
                if (active != null) {
                    try {
                        selectIndex(active);
                    } catch (NumberFormatException ignored) {
                        selectAttributes(active);
                    }
                }
            }
        }
    }

    private void selectParent(Element active, Document document) {
        if (active != null && active != document.getRootElement()) {
            setActiveElement(active.getParent());
        }
    }

    private void selectRoot(Document document) {
        final Element root = document.getRootElement();
        if (root != null) {
            setActiveElement(root);
        }
    }

    private void selectIndex(Element active) {
        final int index = Integer.parseInt(text);
        final Element element = active.elements(value).get(index);
        if (element != null) {
            setActiveElement(element);
        }
    }

    private void selectAttributes(Element active) {
        full: for (Element element : active.elements(value)) {
            for (String s : text.split(",")) {
                if (!selectAttribute(s, element)) {
                    continue full;
                }
            }
            setActiveElement(element);
            break;
        }
    }

    private boolean selectAttribute(String attribute, Element element) {
        final String[] split = attribute.split(":");
        if (split.length > 0) {
            final String name = split[0];
            if (split.length == 1) {
                return element.attribute(name) != null;
            } else if (split.length == 2) {
                final Attribute attr = element.attribute(name);
                return attr != null && attr.getValue().equals(split[1]);
            }
        }
        return false;
    }
}