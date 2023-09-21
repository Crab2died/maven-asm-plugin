package org.maven.plugin.asm;

import org.maven.plugin.util.Log;
import org.maven.plugin.util.StringUtils;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.util.List;

public class ReplaceMethodVisitor extends MethodVisitor {

    private final File targetFile;

    private final String targetParentMethod;

    private final List<ReplaceParam> params;

    public ReplaceMethodVisitor(int api, MethodVisitor methodVisitor, File targetFile,
                                String targetParentMethod, List<ReplaceParam> params) {
        super(api, methodVisitor);
        this.targetFile = targetFile;
        this.targetParentMethod = targetParentMethod;
        this.params = params;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (null == params || params.size() == 0) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            return;
        }
        String newOwner = owner;
        String newName = name;
        String newDescriptor = descriptor;
        for (ReplaceParam param : params) {
            String targetClass = param.getTargetClass();
            String targetMethod = param.getTargetMethod();
            String targetDescriptor = StringUtils.isBlank(param.getTargetDescriptor()) ? descriptor : param.getTargetDescriptor();
            String replaceClass = param.getReplaceClass();
            String replaceMethod = param.getReplaceMethod();
            String replaceDescriptor = StringUtils.isBlank(param.getReplaceDescriptor()) ? descriptor : param.getReplaceDescriptor();
            if (targetClass.equals(owner) && targetMethod.equals(name) && targetDescriptor.equals(descriptor)) {
                Log.info("Method replaced: [", Log.blue(targetFile.getPath() + "#" + targetParentMethod), "] => ",
                        Log.cyan(targetClass + "#" + targetMethod + targetDescriptor), " -> ",
                        Log.green(replaceClass + "#" + replaceMethod + replaceDescriptor));
                newOwner = replaceClass;
                newName = replaceMethod;
                newDescriptor = replaceDescriptor;
                break;
            }
        }
        super.visitMethodInsn(opcode, newOwner, newName, newDescriptor, isInterface);
    }

}