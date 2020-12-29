package maow.xmlcli.command.instruction;

import org.dom4j.Document;
import org.dom4j.Element;

public class EmptyInstruction extends AbstractInstruction {
    EmptyInstruction() {
        super("", "");
    }

    @Override
    public String getType() {
        return "empty";
    }

    @Override
    public void handle(Document document, Element active) {}
}
