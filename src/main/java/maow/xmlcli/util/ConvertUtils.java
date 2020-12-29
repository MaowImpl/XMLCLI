package maow.xmlcli.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ConvertUtils {
    private static final Map<Class<?>, Converter<?>> CONVERTERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T convert(Class<T> clazz, String s) {
        return (T) CONVERTERS.get(clazz).convert(s);
    }

    private static <T> void newConverter(Class<T> clazz, Function<String, T> function) {
        final Converter<T> converter = new Converter<>(function);
        CONVERTERS.put(clazz, converter);
    }

    static {
        newConverter(Integer.class, Integer::parseInt);
        newConverter(Boolean.class, Boolean::parseBoolean);
    }

    private static class Converter<T> {
        private final Function<String, T> function;

        public Converter(Function<String, T> function) {
            this.function = function;
        }

        public T convert(String s) {
            return function.apply(s);
        }
    }
}
