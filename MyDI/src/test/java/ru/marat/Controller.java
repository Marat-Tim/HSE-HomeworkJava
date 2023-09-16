package ru.marat;

public class Controller {
    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    public void run() {
        System.out.println("Method working");
    }
}
