package ru.marat.abstraction;

public class TypeBasedServiceDescriptor extends ServiceDescriptor {
    private final Class<?> implementation;

    public TypeBasedServiceDescriptor(Class<?> abstraction, Class<?> implementation, Lifetime lifetime) {
        super(abstraction, lifetime);
        this.implementation = implementation;
    }

    public Class<?> getImplementation() {
        return implementation;
    }
}
