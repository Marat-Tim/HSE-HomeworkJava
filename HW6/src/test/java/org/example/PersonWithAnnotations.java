package org.example;

@JsonSerializable
public class PersonWithAnnotations {
    @JsonElement("NAME")
    String name;
    @JsonElement()
    Double age;
    @IgnoreNull
    SimplePerson friend;
    @JsonElement("AVAVAVAVAVAVAVA")
    String work;

    PersonWithAnnotations(String name, Double age, SimplePerson friend, String work) {
        this.name = name;
        this.age = age;
        this.friend = friend;
        this.work = work;
    }
}
