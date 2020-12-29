package maow.xmlcli.command;

import maow.xmlcli.XMLCLI;
import maow.xmlcli.command.instruction.AbstractInstruction;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Collections;

public final class HelpCommand extends Command {
    public HelpCommand() {
        super("help", 0, Collections.singletonList(new HelpInstruction()), new Description("help || help <command name>", "Views list of commands, OR usage and description about a single command."), null);
    }

    private static class HelpInstruction extends AbstractInstruction {
        public HelpInstruction() {
            super("$ARGS[1]", "");
        }

        @Override
        public String getType() {
            return null;
        }

        @Override
        public void handle(Document document, Element active) {
            if (!value.equals("$ARGS[1]")) {
                final Command command = XMLCLI.getCommand(value);
                if (command != null) {
                    final String parentID = command.getParentID();
                    final Description description = command.getDescription();

                    System.out.println("Name: " + command.getName());
                    System.out.println("Minimum Args: " + command.getArgs());
                    System.out.println("Extends: " + ((parentID == null) ? "Nothing" : parentID));
                    System.out.println("Usage: " + description.getUsage());
                    System.out.println("Description:\n" + description.getText());
                    return;
                }
                System.out.println("Invalid command: " + value);
                return;
            }
            XMLCLI.getCommands().stream().map(Command::getName).forEach(System.out::println);
        }
    }
}
