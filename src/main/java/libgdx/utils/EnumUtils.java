package libgdx.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EnumUtils {

    public static <T extends Enum> T getRandomValue(T[] values) {
        List<T> elements = new ArrayList<>();
        for (T val : values) {
            elements.add(val);
        }
        Collections.shuffle(elements);
        return elements.get(0);
    }

    public static <T extends Enum> T[] getValues(Class<T> enumClass) {
        return enumClass.getEnumConstants();
    }

    public static <T extends Enum> T getEnumValue(Class<T> enumClass, String name) {
        for (T item : getValues(enumClass)) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
