package maow.xmlcli.command.instruction.types;

import maow.xmlcli.XMLCLI;
import maow.xmlcli.command.Command;
import maow.xmlcli.command.instruction.AbstractInstruction;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public class CallInstruction extends AbstractInstruction {
    public CallInstruction(String value, String text) {
        super(value, text);
    }

    @Override
    public String getType() {
        return "call";
    }

    @Override
    public void handle(Document document, Element active) {
        if (value != null && !value.equals("")) {
            final Command toCall = XMLCLI.getCommand(value);
            if (toCall != null) {
                if (text != null && !text.equals("")) {
                    final List<String> args = XMLCLI.getArgs(value + " " + text);
                    XMLCLI.executeCommand(toCall, args);
                }
            }
        }
    }
}
