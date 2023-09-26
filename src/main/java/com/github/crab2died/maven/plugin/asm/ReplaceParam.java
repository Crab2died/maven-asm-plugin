package com.github.crab2died.maven.plugin.asm;


public class ReplaceParam {


    private String targetClass;

    private String targetMethod;

    private String targetDescriptor;

    private String replaceClass;

    private String replaceMethod;

    private String replaceDescriptor;

    public ReplaceParam() {
    }

    public ReplaceParam(String targetClass, String targetMethod, String targetDescriptor,
                        String replaceClass, String replaceMethod, String replaceDescriptor) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.targetDescriptor = targetDescriptor;
        this.replaceClass = replaceClass;
        this.replaceMethod = replaceMethod;
        this.replaceDescriptor = replaceDescriptor;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getTargetDescriptor() {
        return targetDescriptor;
    }

    public void setTargetDescriptor(String targetDescriptor) {
        this.targetDescriptor = targetDescriptor;
    }

    public String getReplaceClass() {
        return replaceClass;
    }

    public void setReplaceClass(String replaceClass) {
        this.replaceClass = replaceClass;
    }

    public String getReplaceMethod() {
        return replaceMethod;
    }

    public void setReplaceMethod(String replaceMethod) {
        this.replaceMethod = replaceMethod;
    }

    public String getReplaceDescriptor() {
        return replaceDescriptor;
    }

    public void setReplaceDescriptor(String replaceDescriptor) {
        this.replaceDescriptor = replaceDescriptor;
    }
}
