package maow.xmlcli;

import maow.mayan.lang.StringUtils;
import maow.xmlcli.command.Command;
import maow.xmlcli.command.Description;
import maow.xmlcli.command.HelpCommand;
import maow.xmlcli.command.instruction.Instruction;
import maow.xmlcli.command.instruction.InstructionFactory;
import maow.xmlcli.util.ConvertUtils;
import maow.xmlcli.util.IOUtils;
import org.dom4j.*;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static maow.xmlcli.util.XmlUtils.*;

public final class XMLCLI {
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    private static Path XML_PATH = null;
    private static Document DOCUMENT = null;
    private static Element ACTIVE_ELEMENT = null;

    public static void main(String[] args) {
        if (args.length > 0) {
            final String path = args[0];
            if (path.endsWith(".xml")) {
                XML_PATH = Paths.get(path);
                DOCUMENT = IOUtils.readOrCreateXmlDocument(XML_PATH);
                ACTIVE_ELEMENT = DOCUMENT.getRootElement();

                initCommands();
                initTerminal();
                return;
            }
            System.err.println("Specified file does not end with .xml");
            return;
        }
        System.err.println("File not specified! - Valid args: XMLCLI \"file.xml\"");
    }

    private static void initCommands() {
        final Optional<Document> optional = IOUtils.readXmlDocument(Paths.get("commands.xml"));
        if (optional.isPresent()) {
            final Document config = optional.get();
            final List<Command> commands = getCommands(config);
            COMMANDS.put("help", new HelpCommand());
            commands
                    .stream()
                    .filter(command -> !command.getName().equals("help"))
                    .forEach(command -> COMMANDS.put(command.getName(), command));
        }
    }

    public static List<Command> getCommands(Document config) {
        return getChildren(config.getRootElement(), "commands", "command")
                .stream()
                .map(XMLCLI::getCommand)
                .collect(Collectors.toList());
    }

    public static Command getCommand(Element element) {
        final String name = getAttribute(element, "name");
        final String argsString = getAttribute(element, "args");
        int args = 0;
        if (argsString != null) {
            args = ConvertUtils.convert(Integer.class, argsString);
        }
        final List<Instruction> instructions = getInstructions(element);
        final Description description = getDescription(element);
        final String parentID = getAttribute(element, "extends");
        return new Command(name, args, instructions, description, parentID);
    }

    public static Description getDescription(Element element) {
        final Element description = element.element("Description");
        if (description != null) {
            final String usage = getAttribute(description, "usage");
            final String text = getText(description, true);
            return new Description(usage, text);
        }
        return Description.empty();
    }

    public static List<Instruction> getInstructions(Element element) {
        return getChildren(element, "command", "instruction")
                .stream()
                .map(XMLCLI::getInstruction)
                .collect(Collectors.toList());
    }

    public static Instruction getInstruction(Element element) {
        final String type = getAttribute(element, "type");
        final String value = getAttribute(element, "value");
        final String text = getText(element, false);
        return InstructionFactory.create(type, value, text).orElseGet(InstructionFactory::empty);
    }

    private static void initTerminal() {
        try {
            final Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .dumb(true)
                    .build();

            final LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            awaitCommand(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void awaitCommand(LineReader reader) {
        System.out.println("-- XMLCLI --");
        while (true) {
            try {
                final String input = reader.readLine("> ");
                if (!input.equals("")) {
                    readCommand(input);
                }
            } catch (UserInterruptException | EndOfFileException ignored) {
                return;
            }
        }
    }

    private static void readCommand(String input) {
        final String name = input.split("\\s+")[0];
        final List<String> args = getArgs(input);
        final Command command = COMMANDS.get(name);
        if (command != null) {
            executeCommand(command, args);
            return;
        }
        System.out.println("Invalid command: " + name);
    }

    public static List<String> getArgs(String input) {
        final List<String> args = StringUtils.separateArgs(input);
        args.remove(0);
        return args;
    }

    public static void executeCommand(Command command, List<String> args) {
        if (args.size() >= command.getArgs()) {
            final Command parent = COMMANDS.get(command.getParentID());
            if (parent != null) {
                executeCommand(parent, args);
            }
            command.getInstructions().forEach(instruction -> instruction.execute(DOCUMENT, ACTIVE_ELEMENT, args));
            IOUtils.writeXmlDocument(DOCUMENT, XML_PATH);
            return;
        }
        System.out.println("Incorrect number of args, requested " + command.getArgs() + " for command \"" + command.getName() + "\"");
    }

    public static void setActiveElement(Element activeElement) {
        ACTIVE_ELEMENT = activeElement;
    }

    public static Command getCommand(String name) {
        return COMMANDS.get(name);
    }

    public static Collection<Command> getCommands() {
        return COMMANDS.values();
    }
}