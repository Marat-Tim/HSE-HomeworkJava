package ru.marat.abstraction;

import java.util.function.Function;

public interface ServiceContainerBuilder {
    void add(ServiceDescriptor serviceDescriptor);

    ServiceContainer build();

    default <T1, T2 extends T1> ServiceContainerBuilder addSingleton(Class<T1> abstraction,
                                                                     Class<T2> implementation) {
        add(new TypeBasedServiceDescriptor(abstraction, implementation, Lifetime.SINGLETON));
        return this;
    }

    default <T1, T2 extends T1> ServiceContainerBuilder addSessioned(Class<T1> abstraction,
                                                                     Class<T2> implementation) {
        add(new TypeBasedServiceDescriptor(abstraction, implementation, Lifetime.SESSION));
        return this;
    }

    default <T1, T2 extends T1> ServiceContainerBuilder addPrototype(Class<T1> abstraction,
                                                                     Class<T2> implementation) {
        add(new TypeBasedServiceDescriptor(abstraction, implementation, Lifetime.PROTOTYPE));
        return this;
    }

    default <T1, T2 extends T1> ServiceContainerBuilder addSingleton(Class<T1> abstraction,
                                                                     Function<Session, T2> factory) {
        add(new FactoryBasedServiceDescriptor(abstraction, factory, Lifetime.SINGLETON));
        return this;
    }

    default <T1, T2 extends T1> ServiceContainerBuilder addSessioned(Class<T1> abstraction,
                                                                     Function<Session, T2> factory) {
        add(new FactoryBasedServiceDescriptor(abstraction, factory, Lifetime.SESSION));
        return this;
    }

    default <T1, T2 extends T1> ServiceContainerBuilder addPrototype(Class<T1> abstraction,
                                                                     Function<Session, T2> factory) {
        add(new FactoryBasedServiceDescriptor(abstraction, factory, Lifetime.PROTOTYPE));
        return this;
    }

    default <T1, T2 extends T1> ServiceContainerBuilder addSingleton(Class<T1> abstraction, T2 instance) {
        add(new InstanceBasedServiceDescriptor(abstraction, instance, Lifetime.SINGLETON));
        return this;
    }
}
