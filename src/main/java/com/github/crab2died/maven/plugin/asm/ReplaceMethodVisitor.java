package com.github.crab2died.maven.plugin.asm;

import com.github.crab2died.maven.plugin.util.LogColor;
import org.apache.maven.plugin.logging.Log;
import com.github.crab2died.maven.plugin.util.StringUtils;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class ReplaceMethodVisitor extends MethodVisitor {

    private final String parentClass;

    private final String parentMethod;

    private final List<Param> params;

    private final Log log;

    public ReplaceMethodVisitor(int api, MethodVisitor methodVisitor, String parentClass,
                                String parentMethod, List<Param> params, Log log) {
        super(api, methodVisitor);
        this.parentClass = parentClass;
        this.parentMethod = parentMethod;
        this.params = params;
        this.log = log;
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
        for (Param param : params) {
            MethodView targetMethod = param.targetMethod();
            MethodView replaceMethod = param.replacerMethod();

            // exclude self class
            if (targetMethod.getClassName().equals(parentClass) || replaceMethod.getClassName().equals(parentClass)) continue;
            if (targetMethod.compare(owner, name, descriptor)) {
                log.info(LogColor.blue(parentClass + "#" + parentMethod) + " => " +
                        LogColor.cyan(targetMethod) + " -> " + LogColor.green(replaceMethod));
                newOwner = replaceMethod.getClassName();
                newName = replaceMethod.getMethodName();
                if (!StringUtils.isBlank(replaceMethod.getDescriptor())) {
                    newDescriptor = replaceMethod.getDescriptor();
                }
                break;
            }
        }
        super.visitMethodInsn(opcode, newOwner, newName, newDescriptor, isInterface);
    }

}