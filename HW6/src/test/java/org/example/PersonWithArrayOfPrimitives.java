package org.example;

@JsonSerializable
public class PersonWithArrayOfPrimitives {
    String name;
    double age;
    String[] hobby;

    PersonWithArrayOfPrimitives(String name, double age, String[] hobby) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }
}
