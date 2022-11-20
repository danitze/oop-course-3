package org.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        //ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        MyClassLoader classLoader = new MyClassLoader();
        String className = "org.example.TestClass";
        Class<?> clazz = classLoader.findClass(className);
        System.out.println("-------Class-------");
        System.out.println(clazz.getName());
        printAnnotations(clazz);
        System.out.print("Interfaces: ");
        for(AnnotatedType type: clazz.getAnnotatedInterfaces()) {
            System.out.print(type.getType().getTypeName() + " ");
        }
        System.out.println();
        System.out.println("Superclass: " + clazz.getAnnotatedSuperclass().getType().getTypeName());
        System.out.println();
        System.out.println("-------Fields-------");
        for(Field field: clazz.getFields()) {
            System.out.println(Modifier.toString(field.getModifiers()) + " " + field.getType() + " " + field.getName());
            printAnnotations(field);
        }
        System.out.println();

        System.out.println("-------Constructors-------");
        for(Constructor<?> constructor: clazz.getConstructors()) {
            System.out.println("* " + constructor.getName());
            printParameters(constructor);
            printAnnotations(constructor);
        }
        System.out.println();

        System.out.println("-------Methods-------");
        for(var method: clazz.getMethods()) {
            System.out.println("* " + method.getName() + " " + method.getReturnType().getName());
            printParameters(method);
            printAnnotations(method);
        }
    }

    private static void printAnnotations(AnnotatedElement element) {
        System.out.print("  Annotations: ");
        for(Annotation annotation: element.getAnnotations()) {
            System.out.print("  " + annotation.annotationType().getName() + " ");
        }
        System.out.println();
    }

    private static void printParameters(Executable executable) {
        System.out.println("  Parameters:");
        for(var parameter: executable.getParameters()) {
            System.out.println("  " + parameter.getType().getName() + " " + parameter.getName());
        }
    }
}