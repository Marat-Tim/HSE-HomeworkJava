package org.example;

@JsonSerializable
class SimplePerson {
    String name;
    double age;

    SimplePerson(String name, double age) {
        this.name = name;
        this.age = age;
    }
}
