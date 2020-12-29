package maow.xmlcli.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class IOUtils {
    private static final OutputFormat FORMAT = OutputFormat.createPrettyPrint();

    public static Document readOrCreateXmlDocument(Path path) {
        if (Files.exists(path)) {
            final Optional<Document> optional = readXmlDocument(path);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return DocumentHelper.createDocument();
    }

    public static Optional<Document> readXmlDocument(Path path) {
        Document document = null;
        try {
            final SAXReader reader = new SAXReader();
            document = reader.read(path.toFile());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(document);
    }

    public static void writeXmlDocument(Document document, Path path) {
        XMLWriter xw = null;
        try (final FileWriter fw = new FileWriter(path.toFile())) {
            xw = new XMLWriter(fw, FORMAT);
            xw.write(document);
            xw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xw != null) {
                try {
                    xw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}