package com.wangrj.java_lib.test;

import org.fusesource.jansi.Ansi;

public class JavaLibTestClass {

    public static void main(String[] args) {
        Ansi.Color[] colors = Ansi.Color.values();
        for (Ansi.Color color : colors) {
            System.out.println(Ansi.ansi().eraseScreen().fg(color).a(color.name()).reset());
        }
    }

}
