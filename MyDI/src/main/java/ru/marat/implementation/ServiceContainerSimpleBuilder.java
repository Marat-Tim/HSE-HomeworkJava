package ru.marat.implementation;

import ru.marat.abstraction.ServiceContainer;
import ru.marat.abstraction.ServiceContainerBuilder;
import ru.marat.abstraction.ServiceDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ServiceContainerSimpleBuilder implements ServiceContainerBuilder {
    private final List<ServiceDescriptor> descriptors = new ArrayList<>();

    @Override
    public void add(ServiceDescriptor serviceDescriptor) {
        descriptors.add(serviceDescriptor);
    }

    @Override
    public ServiceContainer build() {
        return new SimpleServiceContainer(descriptors);
    }
}
