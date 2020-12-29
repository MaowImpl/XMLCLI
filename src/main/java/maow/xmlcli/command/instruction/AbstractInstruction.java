package maow.xmlcli.command.instruction;

import maow.xmlcli.XMLCLI;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public abstract class AbstractInstruction implements Instruction {
    private final String initialValue;
    private final String initialText;

    protected String value;
    protected String text;

    public AbstractInstruction(String value, String text) {
        this.initialValue = value;
        this.initialText = text;
    }

    @Override
    public String getValue() {
        return initialValue;
    }

    @Override
    public String getText() {
        return initialText;
    }

    @Override
    public void execute(Document document, Element active, List<String> args) {
        value = expandArgs(initialValue, args);
        text = expandArgs(initialText, args);

        handle(document, active);

        value = initialValue;
        text = initialText;
    }

    private String expandArgs(String text, List<String> args) {
        for (int i = 0; i < args.size(); i++) {
            final String arg = "\\$ARGS\\[" + (i + 1) + "]";
            text = text.replaceAll(arg, args.get(i));
        }
        return text;
    }

    protected static void setActiveElement(Element element) {
        XMLCLI.setActiveElement(element);
    }
}