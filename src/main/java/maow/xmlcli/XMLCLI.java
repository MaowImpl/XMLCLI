package maow.xmlcli;

import maow.mayan.lang.StringUtils;
import maow.xmlcli.command.Command;
import maow.xmlcli.command.instruction.Instruction;
import maow.xmlcli.command.instruction.InstructionFactory;
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
        final Path configPath = Paths.get("commands.xml");
        final Optional<Document> optional = IOUtils.readXmlDocument(configPath);
        if (optional.isPresent()) {
            final Document config = optional.get();
            final List<Command> commands = getCommands(config);
            commands.forEach(command -> {
                System.out.println("Loaded command: " + command.getName());
                COMMANDS.put(command.getName(), command);
            });
        }
    }

    private static List<Command> getCommands(Document config) {
        return getChildren(config.getRootElement(), "commands", "command")
                .stream()
                .map(XMLCLI::getCommand)
                .collect(Collectors.toList());
    }

    private static Command getCommand(Element element) {
        final String name = getAttribute(element, "name");
        final int args = getAttribute(Integer.class, element, "args");
        final List<Instruction> instructions = getInstructions(element);
        return new Command(name, args, instructions);
    }

    private static List<Instruction> getInstructions(Element element) {
        return getChildren(element, "command", "instruction")
                .stream()
                .map(XMLCLI::getInstruction)
                .collect(Collectors.toList());
    }

    private static Instruction getInstruction(Element element) {
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
            if (args.size() >= command.getArgs()) {
                executeCommand(command, args);
                return;
            }
            System.out.println("Incorrect number of args, requested " + command.getArgs() + " for command \"" + command.getName() + "\"");
            return;
        }
        System.out.println("Invalid command: " + name);
    }

    private static List<String> getArgs(String input) {
        final List<String> args = StringUtils.separateArgs(input);
        args.remove(0);
        return args;
    }

    private static void executeCommand(Command command, List<String> args) {
        command.getInstructions().forEach(instruction -> instruction.execute(DOCUMENT, ACTIVE_ELEMENT, args));
        IOUtils.writeXmlDocument(DOCUMENT, XML_PATH);
    }

    public static void setActiveElement(Element activeElement) {
        ACTIVE_ELEMENT = activeElement;
    }
}