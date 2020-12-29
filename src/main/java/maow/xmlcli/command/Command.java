package maow.xmlcli.command;

import maow.xmlcli.command.instruction.Instruction;

import java.util.List;

public class Command {
    private final String name;
    private final int args;
    private final List<Instruction> instructions;

    public Command(String name, int args, List<Instruction> instructions) {
        this.name = name;
        this.args = args;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public int getArgs() {
        return args;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
