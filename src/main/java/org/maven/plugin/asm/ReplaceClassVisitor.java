package org.maven.plugin.asm;


import org.apache.maven.plugin.logging.Log;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class ReplaceClassVisitor extends ClassVisitor {

    private String className;

    private final List<Param> params;

    private final Log log;

    public ReplaceClassVisitor(int api, ClassVisitor classVisitor, List<Param> params, Log log) {
        super(api, classVisitor);
        this.params = params;
        this.log = log;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        final MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new ReplaceMethodVisitor(super.api, methodVisitor, className, name, params, log);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

}