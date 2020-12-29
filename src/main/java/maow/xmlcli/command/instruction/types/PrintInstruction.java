package maow.xmlcli.command.instruction.types;

import maow.xmlcli.command.instruction.AbstractInstruction;
import org.dom4j.Document;
import org.dom4j.Element;

public class PrintInstruction extends AbstractInstruction {
    public PrintInstruction(String value, String text) {
        super(value, text);
    }

    @Override
    public String getType() {
        return "print";
    }

    @Override
    public void handle(Document document, Element active) {
        if (value.equals("$ACTIVE")) {
            if (active != null) {
                System.out.println(active.getName());
            }
        } else {
            System.out.println(value);
        }
    }
}
