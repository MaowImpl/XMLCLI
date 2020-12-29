package maow.xmlcli.command;

import maow.xmlcli.command.instruction.Instruction;

import java.util.List;

public class Command {
    private final String name;
    private final int args;
    private final List<Instruction> instructions;
    private final Description description;
    private final String parentID;

    public Command(String name, int args, List<Instruction> instructions, Description description, String parentID) {
        this.name = name;
        this.args = args;
        this.instructions = instructions;
        this.description = description;
        this.parentID = parentID;
    }

    public String getName() {
        return name;
    }

    public int getArgs() {
        return args;
    }

    public String getParentID() {
        return parentID;
    }

    public Description getDescription() {
        return description;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
