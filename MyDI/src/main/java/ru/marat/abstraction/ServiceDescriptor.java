package ru.marat.abstraction;

public abstract class ServiceDescriptor {
    private final Class<?> abstraction;

    private final Lifetime lifetime;

    protected ServiceDescriptor(Class<?> abstraction, Lifetime lifetime) {
        this.abstraction = abstraction;
        this.lifetime = lifetime;
    }


    public Class<?> getAbstraction() {
        return abstraction;
    }

    public Lifetime getLifetime() {
        return lifetime;
    }
}
