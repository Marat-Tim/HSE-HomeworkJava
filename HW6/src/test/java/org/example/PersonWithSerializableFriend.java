package org.example;

@JsonSerializable
public class PersonWithSerializableFriend {
    String name;
    Double age;
    SimplePerson mainFriend;

    PersonWithSerializableFriend(String name, Double age, SimplePerson mainFriend) {
        this.name = name;
        this.age = age;
        this.mainFriend = mainFriend;
    }
}
