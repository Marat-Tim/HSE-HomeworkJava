package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;

class SerializatorTest {

    @Test
    @DisplayName("Простой тест с SimplePerson")
    void serialize_Simple_Test_With_SimplePerson() {
        SimplePerson person =
                new SimplePerson("Marat Tim", 19.5);

        JSONObject expected = new JSONObject()
                .put("name", "Marat Tim")
                .put("age", 19.5);

        JSONObject actual = Serializator.serialize(person);

        assertJsonEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест класса с массивом простых объектов")
    void serialize_Test_Class_With_Array_Of_Primitives() {
        PersonWithArrayOfPrimitives person =
                new PersonWithArrayOfPrimitives(
                        "Marat Tim",
                        19.5,
                        new String[]{"table games", "programming"});

        JSONObject expected = new JSONObject()
                .put("name", "Marat Tim")
                .put("age", 19.5)
                .put("hobby", new String[]{"table games", "programming"});

        JSONObject actual = Serializator.serialize(person);

        assertJsonEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест класса, хранящего другой сериализуемый класс")
    void serialize_Class_Storing_Another_Serializable_Class() {
        PersonWithSerializableFriend person =
                new PersonWithSerializableFriend(
                        "Marat Tim",
                        19.5,
                        new SimplePerson("Goliaf", 18.7)
                );

        JSONObject expected = new JSONObject()
                .put("name", "Marat Tim")
                .put("age", 19.5)
                .put("mainFriend",
                        new JSONObject().put("name", "Goliaf").put("age", 18.7));

        JSONObject actual = Serializator.serialize(person);

        assertJsonEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест класса с коллекцией")
    void serialize_Test_Of_Class_With_Collection() {
        PersonWithCollectionOfFriends person =
                new PersonWithCollectionOfFriends(
                        "Marat Tim",
                        19.5,
                        List.of(
                                new SimplePerson("Goliaf", 18.7),
                                new SimplePerson("Avna", 20)
                        )
                );

        JSONObject expected = new JSONObject()
                .put("name", "Marat Tim")
                .put("age", 19.5)
                .put("friends",
                        new JSONArray()
                                .put(new JSONObject().put("name", "Goliaf").put("age", 18.7))
                                .put(new JSONObject().put("name", "Avna").put("age", 20.0))
                );

        JSONObject actual = Serializator.serialize(person);

        assertJsonEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест аннотаций")
    void serialize_Test_Of_Annotations() {
        PersonWithAnnotations person = new PersonWithAnnotations(
                "Marat Tim",
                19.5,
                null,
                "programmer"
        );

        JSONObject expected = new JSONObject()
                .put("NAME", "Marat Tim")
                .put("age", 19.5)
                .put("AVAVAVAVAVAVAVA", "programmer");

        JSONObject actual = Serializator.serialize(person);

        assertJsonEquals(expected, actual);
    }

}