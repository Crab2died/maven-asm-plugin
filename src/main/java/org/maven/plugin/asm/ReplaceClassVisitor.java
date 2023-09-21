package org.maven.plugin.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.util.List;

public class ReplaceClassVisitor extends ClassVisitor {

    private final File targetFile;

    private final List<ReplaceParam> params;

    public ReplaceClassVisitor(int api, ClassVisitor classVisitor, File targetFile, List<ReplaceParam> params) {
        super(api, classVisitor);
        this.targetFile = targetFile;
        this.params = params;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        final MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new ReplaceMethodVisitor(super.api, methodVisitor, targetFile, name, params);
    }

}