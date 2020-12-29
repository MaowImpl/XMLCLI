package maow.xmlcli.command;

public class Description {
    private final String usage;
    private final String text;

    public Description(String usage, String text) {
        this.usage = usage;
        this.text = text;
    }

    public String getUsage() {
        return usage;
    }

    public String getText() {
        return text;
    }

    public static Description empty() {
        return new Description(null, "No information for this command.");
    }
}
