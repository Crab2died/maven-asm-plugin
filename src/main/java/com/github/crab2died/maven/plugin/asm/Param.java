package com.github.crab2died.maven.plugin.asm;

import com.github.crab2died.maven.plugin.util.MethodUtils;

public class Param {

    // packageNam.TargetClassName#targetMethodName
    // packageNam.TargetClassName#targetMethodName(packageNam.ParmaClassName, packageNam.ParmaClassName)packageNam.ReturnClassName
    private String target;

    // packageName.ReplacerClassName#replacerMethodName
    // packageName.ReplacerClassName#replacerMethodName(packageNam.ParmaClassName, packageNam.ParmaClassName)packageNam.ReturnClassName
    private String replacer;

    private MethodView targetMethod;

    private MethodView replacerMethod;

    public Param() {
    }

    public Param(String target, String replacer) {
        this.target = target;
        this.replacer = replacer;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getReplacer() {
        return replacer;
    }

    public void setReplacer(String replacer) {
        this.replacer = replacer;
    }

    public MethodView targetMethod() {
        if (null != targetMethod) return targetMethod;
        targetMethod = MethodUtils.methodView(target);
        return targetMethod;
    }

    public MethodView replacerMethod() {
        if (null != replacerMethod) return replacerMethod;
        replacerMethod = MethodUtils.methodView(replacer);
        return replacerMethod;
    }

}
