package org.maven.plugin.util;

import org.maven.plugin.asm.MethodView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodUtils {

    // match packageNam.ClassName#MethodName
    private static final String METHOD_REGEX_SIMPLE = "^[a-zA-Z_$][a-zA-Z\\d_$]*(\\.[a-zA-Z_$][a-zA-Z\\d_$]*)*#([a-zA-Z_$][a-zA-Z\\d_$]*)$";

    // match packageNam.ClassName#MethodName(packageNam.ParmaClassName, packageNam.ParmaClassName)packageNam.ReturnClassName
    private static final String METHOD_REGEX_FULL = "^[a-zA-Z_$][a-zA-Z\\d_$]*(\\.[a-zA-Z_$][a-zA-Z\\d_$]*)*#([a-zA-Z_$][a-zA-Z\\d_$]*)" +
            "\\(([a-zA-Z_$][a-zA-Z\\d_$\\[\\]]*(\\.[a-zA-Z_$][a-zA-Z\\d_$\\[\\]]*)*)*" +
            "(,[ a-zA-Z_$][a-zA-Z\\d_$\\[\\]]*(\\.[a-zA-Z_$][a-zA-Z\\d_$\\[\\]]*)*)*\\)" +
            "([a-zA-Z_$][a-zA-Z\\d_$\\[\\]]*(\\.[a-zA-Z_$][a-zA-Z\\d_$\\[\\]]*)*)?$";


    public static MethodView methodView(String method) {
        if (StringUtils.isBlank(method)) return null;
        if (methodCheck(method, METHOD_REGEX_FULL)) {
            String[] arr0 = method.split("#");
            String className = arr0[0].replace(".", "/");
            String[] arr1 = arr0[1].split("\\(");
            String methodName = arr1[0];
            if (arr1.length == 1) arr1 = new String[]{arr1[0], ""};
            String[] arr2 = arr1[1].split("\\)");
            if (arr2.length == 0) arr2 = new String[]{"", ""};
            if (arr2.length == 1) arr2 = new String[]{arr2[0], ""};
            String[] methodParams = arr2[0].split(",");
            String methodReturn = arr2[1];
            StringBuilder descriptor = new StringBuilder();
            descriptor.append("(");
            for (String methodParam : methodParams) {
                descriptor.append(objectDescriptor(methodParam));
            }
            descriptor.append(")").append(objectDescriptor(methodReturn));
            return new MethodView(className, methodName, descriptor.toString());
        }
        if (methodCheck(method, METHOD_REGEX_SIMPLE)) {
            String[] arr0 = method.split("#");
            String className = arr0[0].replace(".", "/");
            String[] arr1 = arr0[1].split("\\(");
            String methodName = arr1[0];
            return new MethodView(className, methodName, null);
        }
        throw new IllegalArgumentException("method definition error");
    }


    public static String objectDescriptor(String obj) {
        StringBuilder classDesc = new StringBuilder();
        int countArray = countString(obj, "[]");
        String[] arr = obj.split("\\[]");
        String clazz = arr[0].trim().replace(".", "/");

        if ("".equals(clazz)) classDesc = new StringBuilder("V");
        if (byte.class.getName().equals(clazz)) classDesc = new StringBuilder("B");
        if (char.class.getName().equals(clazz)) classDesc = new StringBuilder("C");
        if (float.class.getName().equals(clazz)) classDesc = new StringBuilder("F");
        if (int.class.getName().equals(clazz)) classDesc = new StringBuilder("I");
        if (long.class.getName().equals(clazz)) classDesc = new StringBuilder("J");
        if (short.class.getName().equals(clazz)) classDesc = new StringBuilder("S");
        if (boolean.class.getName().equals(clazz)) classDesc = new StringBuilder("Z");
        if (int.class.getName().equals(clazz)) classDesc = new StringBuilder("I");
        if (int.class.getName().equals(clazz)) classDesc = new StringBuilder("I");
        if (classDesc.length() == 0) classDesc = new StringBuilder("L" + clazz + ";");
        if (countArray > 0) {
            for (int i = 0; i < countArray; i++) {
                classDesc = new StringBuilder("[" + classDesc);
            }
        }
        return classDesc.toString();
    }

    public static int countString(String s, String target) {
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = s.indexOf(target, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += target.length();
            }
        }
        return count;
    }

    public static boolean methodCheck(String method, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(method);
        return matcher.matches();
    }

}
