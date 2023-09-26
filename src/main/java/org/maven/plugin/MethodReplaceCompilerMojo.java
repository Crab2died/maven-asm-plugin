package org.maven.plugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.maven.plugin.asm.Param;
import org.maven.plugin.asm.ReplaceClassVisitor;
import org.maven.plugin.util.Constants;
import org.maven.plugin.util.LogColor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.maven.plugin.util.Constants.CLASS_SUFFIX;
import static org.maven.plugin.util.Constants.METHOD_REPLACER_PLUGIN;

@Mojo(name = Constants.METHOD_REPLACER_MOJO, defaultPhase = LifecyclePhase.COMPILE)
public class MethodReplaceCompilerMojo extends AbstractMojo {

    private static final String[] DEFAULT_INCLUDES = new String[]{"**/**.class"};

    @Parameter(name = "output", defaultValue = "${project.build.directory}", required = true)
    private File output;

    @Parameter(name = "includes")
    private String[] includes;

    @Parameter(name = "excludes")
    private String[] excludes;

    @Parameter(name = "params")
    private List<Param> params;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        long start = System.currentTimeMillis();
        getLog().info("---------------< " + LogColor.green(METHOD_REPLACER_PLUGIN) + " >---------------");

        // check params
        if (null == output || !output.exists()) {
            return;
        }
        if (null == params || params.size() == 0) {
            return;
        }

        FileSetManager fileSetManager = new FileSetManager();
        FileSet jarContentFileSet = new FileSet();
        jarContentFileSet.setDirectory(output.getAbsolutePath());
        jarContentFileSet.setIncludes(Arrays.asList(getIncludes()));
        jarContentFileSet.setExcludes(Arrays.asList(getExcludes()));
        String[] includedFiles = fileSetManager.getIncludedFiles(jarContentFileSet);
        if (null == includedFiles || includedFiles.length == 0) return;
        for (String includeFile : includedFiles) {
            try {
                String path = output + File.separator + includeFile;
                File file = new File(path);
                String fileName = file.getName();
                // only class
                if (fileName.endsWith(CLASS_SUFFIX)) {
                    final byte[] instrumentBytes = doInsertFile(file, params);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(instrumentBytes);
                        fos.flush();
                    }
                }
            } catch (Exception e) {
                throw new MojoExecutionException(e);
            }
        }

        long cost = System.currentTimeMillis() - start;
        getLog().info("---------------< " + LogColor.green(METHOD_REPLACER_PLUGIN) + " [ " + cost + " ms ] >---------------");

    }

    private byte[] doInsertFile(File file, List<Param> params) throws IOException {
        ClassReader cr = new ClassReader(new FileInputStream(file));
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cr.accept(new ReplaceClassVisitor(Opcodes.ASM9, cw, params, getLog()), ClassReader.SKIP_DEBUG);
        return cw.toByteArray();
    }

    private String[] getIncludes() {
        if (includes != null && includes.length > 0) {
            return includes;
        }
        return DEFAULT_INCLUDES;
    }

    private String[] getExcludes() {
        if (excludes != null && excludes.length > 0) {
            return excludes;
        }
        return new String[0];
    }

    public File getOutput() {
        return output;
    }

    public List<Param> getParams() {
        return params;
    }

}
