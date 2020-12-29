package maow.xmlcli.command.instruction;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public interface Instruction {
    String getType();
    String getValue();
    String getText();

    void execute(Document document, Element active, List<String> args);
    void handle(Document document, Element active);
}
