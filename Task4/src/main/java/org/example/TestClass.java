package org.example;

import jdk.jfr.Enabled;

@Enabled
public class TestClass implements TestInterface {
    @Deprecated
    public int a;
    public int b;

    public TestClass() {
    }


    public TestClass(int a, int b) {
        this.a = a;
        this.b = b;
    }
}
