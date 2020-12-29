package maow.xmlcli.util;

import org.dom4j.Element;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class XmlUtils {
    public static boolean isValidElement(Element element, String name) {
        if (name == null || name.equals("")) return true;
        return (element != null && element.getName().equalsIgnoreCase(name));
    }

    public static List<Element> getChildren(Element parent, String requiredParentName, String requiredChildName) {
        if (isValidElement(parent, requiredParentName)) {
            return parent.elements()
                    .stream()
                    .filter(element -> isValidElement(element, requiredChildName))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static List<Element> getChildren(Element parent, String requiredParentName) {
        return getChildren(parent, requiredParentName, null);
    }

    public static List<Element> getChildren(Element parent) {
        return getChildren(parent, null, null);
    }

    public static <T> T getAttribute(Class<T> clazz, Element element, String name) {
        final String attribute = element.attributeValue(name);
        return ConvertUtils.convert(clazz, attribute);
    }

    public static String getAttribute(Element element, String name) {
        return element.attributeValue(name);
    }

    public static String getText(Element element, boolean trim) {
        return trim ? element.getTextTrim() : element.getText();
    }
}
