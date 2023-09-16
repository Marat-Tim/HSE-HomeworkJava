package ru.marat;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {
    static <T1, T2 extends T1> void Register(Class<T1> t1, Class<T2> t2) {

    }

    public static void main(String[] args) {
        System.out.printf("%s%n", Main.class);
    }
}