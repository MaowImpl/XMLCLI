package maow.xmlcli.command.instruction.types;

import maow.xmlcli.command.instruction.AbstractInstruction;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Arrays;

public class TextInstruction extends AbstractInstruction {
    public TextInstruction(String value, String text) {
        super(value, text);
    }

    @Override
    public String getType() {
        return "text";
    }

    @Override
    public void handle(Document document, Element active) {
        if (active != null) {
            if (active.elements().size() == 0) {
                switch (value) {
                    case "set": {
                        active.setText(text);
                        break;
                    }
                    case "append": {
                        final String originalText = active.getText();
                        active.setText(originalText + text);
                        break;
                    }
                    case "remove": {
                        final Integer[] values = getRemoveValues(text);
                        if (values.length >= 2) {
                            final String text = active.getText();
                            active.setText(text.substring(0, values[0]) + text.substring(values[0] + values[1]));
                        }
                        break;
                    }
                    case "clear": {
                        active.setText("");
                        break;
                    }
                    case "insert": {
                        final int index = getInsertIndex(text);
                        final String insert = getInsertString(text);

                        final String original = active.getText();

                        active.setText(original.substring(0, index + 1) + insert + original.substring(index + 1));
                        break;
                    }
                    case "comma": {
                        String originalText = active.getText();
                        if (originalText.startsWith(",")) {
                            originalText = originalText.substring(1);
                        }
                        active.setText(originalText + text);
                        break;
                    }
                }
            }
        }
    }

    private Integer[] getRemoveValues(String text) {
        return Arrays.stream(text.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
    }

    private int getInsertIndex(String text) {
        final String[] split = text.split(",");
        if (split.length == 2) {
            return Integer.parseInt(split[0]);
        }
        return 0;
    }

    private String getInsertString(String text) {
        final String[] split = text.split(",");
        switch(split.length) {
            case 1:
                return split[0];
            case 2:
                return split[1];
            default:
                return "";
        }
    }
}
