package org.maven.plugin.asm;

public class MethodView {

    private String className;

    private String methodName;

    private String descriptor;

    public MethodView() {
    }

    public MethodView(String className, String methodName, String descriptor) {
        this.className = className;
        this.methodName = methodName;
        this.descriptor = descriptor;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String method() {
        return className + "#" + methodName + (null == descriptor ? "" : descriptor);
    }

    public boolean compare(String className, String methodName, String descriptor) {
        if (null != this.descriptor && !"".equals(this.descriptor)) {
            return method().equals(className + "#" + methodName + descriptor);
        } else {
            return method().equals(className + "#" + methodName);
        }
    }

    @Override
    public String toString() {
        return method();
    }

}