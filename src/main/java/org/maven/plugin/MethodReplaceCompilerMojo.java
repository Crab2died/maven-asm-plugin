package org.maven.plugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.maven.plugin.asm.ReplaceClassVisitor;
import org.maven.plugin.asm.ReplaceParam;
import org.maven.plugin.util.Constants;
import org.maven.plugin.util.Log;
import org.maven.plugin.util.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.maven.plugin.util.Constants.METHOD_REPLACER_PLUGIN;

@Mojo(name = Constants.METHOD_REPLACER_MOJO, defaultPhase = LifecyclePhase.COMPILE)
public class MethodReplaceCompilerMojo extends AbstractMojo {

    @Parameter(name = "output", defaultValue = "${project.build.directory}")
    private File output;

    @Parameter(name = "replaceParams")
    private List<ReplaceParam> replaceParams;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        long start = System.currentTimeMillis();
        Log.info("-----------< ", Log.green(METHOD_REPLACER_PLUGIN), " >-----------");

        Log.info("Parameters: [", output, "] => ", replaceParams);

        // check params
        if (null == output || !output.exists()) {
            return;
        }
        if (null == replaceParams || replaceParams.size() == 0) {
            return;
        }
        List<ReplaceParam> params = new ArrayList<>();
        // clear up params
        for (ReplaceParam replaceParam : replaceParams) {
            String targetClass = replaceParam.getTargetClass();
            String targetMethod = replaceParam.getTargetMethod();
            String targetDescriptor = replaceParam.getTargetDescriptor();
            String replaceClass = replaceParam.getReplaceClass();
            String replaceMethod = replaceParam.getReplaceMethod();
            String replaceDescriptor = replaceParam.getReplaceDescriptor();
            if (StringUtils.isNoneBlank(targetClass, targetMethod, replaceClass, replaceMethod)) {
                targetClass = targetClass.replace(".", "/");
                replaceClass = replaceClass.replace(".", "/");
                params.add(new ReplaceParam(targetClass, targetMethod, targetDescriptor, replaceClass, replaceMethod, replaceDescriptor));
            }
        }
        if (params.size() == 0) {
            return;
        }

        try {
            insertPile(output, params);
        } catch (Exception e) {
            throw new MojoExecutionException(e);
        }
        long cost = System.currentTimeMillis() - start;

        Log.info("-----------< ", Log.green(METHOD_REPLACER_PLUGIN), " [ ", cost, " ms ] >-----------");

    }

    private void insertPile(File file, List<ReplaceParam> params) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File file0 : files) {
                    insertPile(file0, params);
                }
            }
        }
        String fileName = file.getName();
        if (fileName.endsWith(".class")) {
            final byte[] instrumentBytes = doInsertFile(file, params);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(instrumentBytes);
                fos.flush();
            }
        }
    }

    private byte[] doInsertFile(File file, List<ReplaceParam> params) throws IOException {
        ClassReader cr = new ClassReader(new FileInputStream(file));
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cr.accept(new ReplaceClassVisitor(Opcodes.ASM9, cw, file, params), ClassReader.SKIP_DEBUG);
        return cw.toByteArray();
    }

}
