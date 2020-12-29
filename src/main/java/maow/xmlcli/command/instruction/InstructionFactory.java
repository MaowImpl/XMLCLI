package maow.xmlcli.command.instruction;

import maow.xmlcli.command.instruction.types.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public final class InstructionFactory {
    private static final Map<String, BiFunction<String, String, Instruction>> INSTRUCTIONS = new HashMap<>();

    public static Optional<Instruction> create(String type, String value, String text) {
        return Optional.of(INSTRUCTIONS.get(type).apply(value, text));
    }

    public static Instruction empty() {
        return new EmptyInstruction();
    }

    public static void addInstruction(String type, BiFunction<String, String, Instruction> biFunction) {
        INSTRUCTIONS.put(type, biFunction);
    }

    static {
        addInstruction("element", ElementInstruction::new);
        addInstruction("attribute", AttributeInstruction::new);
        addInstruction("text", TextInstruction::new);
        addInstruction("select", SelectInstruction::new);
        addInstruction("print", PrintInstruction::new);
    }
}
