package ru.marat.abstraction;

import java.util.function.Function;

public class FactoryBasedServiceDescriptor extends ServiceDescriptor {
    private final Function<Session, ?> factory;

    public FactoryBasedServiceDescriptor(Class<?> abstraction,
                                         Function<Session, ?> factory,
                                         Lifetime lifetime) {
        super(abstraction, lifetime);
        this.factory = factory;
    }

    public Function<Session, ?> getFactoryMethod() {
        return factory;
    }
}

