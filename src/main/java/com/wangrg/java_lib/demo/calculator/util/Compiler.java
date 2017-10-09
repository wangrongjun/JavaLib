package com.wangrg.java_lib.demo.calculator.util;

import com.wangrg.java_lib.demo.calculator.constant.C;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;


public class Compiler {

    public static int compile(String code) {
        MyFile.saveCode(code);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        int status = compiler.run(null, null, null, "-encoding", "UTF-8", "-d",
                C.binDir, C.javaFilePath);

        return status != 0 ? C.ERROR : C.OK;

    }

    public interface FunInterface {
        public double fun(double n1, double n2);
    }

    public static FunInterface findInstanceByClassName(String className,
                                                       String binDir) throws Exception {

        File file = new File(binDir);

        if (!file.exists()) {

            throw new Exception();
        }

        FunInterface funInterface;

        try {

            URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI()
                    .toURL()});

            funInterface = (FunInterface) loader.loadClass(className)
                    .newInstance();

            loader.close();

        } catch (Exception e) {
            throw e;
        }

        return funInterface;
    }
}
