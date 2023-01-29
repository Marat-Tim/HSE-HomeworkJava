package org.example;

import java.util.Collection;

@JsonSerializable
public class PersonWithCollectionOfFriends {
    String name;
    Double age;
    Collection<SimplePerson> friends;

    PersonWithCollectionOfFriends(String name, Double age, Collection<SimplePerson> friends) {
        this.name = name;
        this.age = age;
        this.friends = friends;
    }
}
