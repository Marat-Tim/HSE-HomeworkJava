package ru.marat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Статический класс, отвечающий за сериализацию объектов в json объект.
 * Для настройки сериализации можно использовать несколько аннотаций:
 *
 * <br>1. IgnoreNull - не добавляет значение поля в json, если оно null;
 *
 * <br>2. JsonElement(String) - меняет имя поля при сериализации на заданное;
 *
 * <br>3. JsonSerializable - помечает, что класс можно сериализовать
 * (не помечайте этой аннотацией классы
 * у которых нет возможности применить setAccessible(true) к одному из полей).
 */
public final class Serializator {

    /**
     * Скрываем конструктор.
     */
    private Serializator() {
    }

    /**
     * Сериализует объект в json файлы.
     *
     * @param o Объект, который нужно сериализовать.
     * @return Json объект.
     */
    public static JSONObject serialize(Object o) {
        if (o.getClass().getAnnotation(JsonSerializable.class) == null) {
            throw new IllegalArgumentException("Класс объекта не помечен аннотацией @JsonSerializable");
        }
        JSONObject json = new JSONObject();
        for (Field field : o.getClass().getDeclaredFields()) {
            addFieldToJson(o, field, json);
        }
        return json;
    }

    /**
     * Добавляет значение данного поля у данного объекта в json(учитывая все аннотации).
     */
    private static void addFieldToJson(Object o, Field field, JSONObject json) {
        Object value = getValueOfField(o, field);
        // Проверка на аннотацию @IgnoreNull.
        if (value == null && field.getAnnotation(IgnoreNull.class) != null) {
            return;
        }
        // Если у объекта есть ссылка на другой объект, который можно сериализовать, то сериализует его.
        if (value != null && field.getType().getAnnotation(JsonSerializable.class) != null) {
            value = Serializator.serialize(value);
        }
        // Обработка коллекций.
        if (value instanceof Collection<?> collection) {
            JSONArray array = new JSONArray();
            for (Object el : collection) {
                if (el.getClass().getAnnotation(JsonSerializable.class) != null) {
                    array.put(Serializator.serialize(el));
                } else {
                    array.put(el);
                }
            }
            value = array;
        }
        if (value == null) {
            value = "null";
        }
        String name = getName(field);
        json.put(name, value);
    }

    /**
     * Возвращает какое имя должно быть у поля при сериализации.
     */
    private static String getName(Field field) {
        JsonElement annotation = field.getAnnotation(JsonElement.class);
        if (annotation != null && !annotation.value().isEmpty()) {
            return annotation.value();
        }
        return field.getName();
    }

    /**
     * Возвращает значение поля у данного объекта.
     * Если нельзя применить field.setAccessible(true), то выбрасывает RuntimeException.
     */
    private static Object getValueOfField(Object o, Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(o);
            field.setAccessible(false);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Не работает field.setAccessible(true)");
        }
    }
}
