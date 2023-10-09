package com.github.crab2died.maven.plugin;


import com.github.crab2died.maven.plugin.asm.Param;
import com.github.crab2died.maven.plugin.asm.ReplaceClassVisitor;
import com.github.crab2died.maven.plugin.util.Constants;
import com.github.crab2died.maven.plugin.util.LogColor;
import com.github.crab2died.maven.plugin.util.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.github.crab2died.maven.plugin.util.Constants.CLASS_SUFFIX;
import static com.github.crab2died.maven.plugin.util.Constants.METHOD_REPLACER_PLUGIN;

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

    @Parameter(name = "asmApiVersion", defaultValue = "ASM9")
    private String asmApiVersion;

    @Parameter(name = "asmClassWriterFlags")
    private String asmClassWriterFlags;

    @Parameter(name = "asmParsingOptions")
    private String asmParsingOptions;

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
        FileSet fileSet = new FileSet();
        fileSet.setDirectory(output.getAbsolutePath());
        fileSet.setIncludes(Arrays.asList(getIncludes()));
        fileSet.setExcludes(Arrays.asList(getExcludes()));
        String[] includedFiles = fileSetManager.getIncludedFiles(fileSet);
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
        ClassWriter cw = new ClassWriter(getAsmClassWriterFlags());
        cr.accept(new ReplaceClassVisitor(getAsmApiVersion(), cw, params, getLog()), getAsmParsingOptions());
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

    public int getAsmApiVersion() {
        asmApiVersion = asmApiVersion.trim().toUpperCase();
        switch (asmApiVersion) {
            case "ASM4":
                return Opcodes.ASM4;
            case "ASM5":
                return Opcodes.ASM5;
            case "ASM6":
                return Opcodes.ASM6;
            case "ASM7":
                return Opcodes.ASM7;
            case "ASM8":
                return Opcodes.ASM8;
            default:
                return Opcodes.ASM9;
        }
    }

    public int getAsmClassWriterFlags() {
        int classWriterFlag = 0;
        if (StringUtils.isBlank(asmClassWriterFlags)) return classWriterFlag;
        String[] flags = asmClassWriterFlags.split("\\|");
        for (String flag : flags) {
            classWriterFlag = classWriterFlag | getFlag(flag);
        }
        return classWriterFlag;
    }

    public int getAsmParsingOptions() {
        int parsingOptions = 0;
        if (StringUtils.isBlank(asmParsingOptions)) return parsingOptions;
        String[] options = asmParsingOptions.split("\\|");
        for (String option : options) {
            parsingOptions = parsingOptions | getParsingOption(option);
        }
        return parsingOptions;
    }

    public int getFlag(String flag) {
        flag = flag.trim().toUpperCase();
        switch (flag) {
            case "COMPUTE_MAXS":
                return ClassWriter.COMPUTE_MAXS;
            case "COMPUTE_FRAMES":
                return ClassWriter.COMPUTE_FRAMES;
            default:
                return 0;
        }
    }

    public int getParsingOption(String option) {
        option = option.trim().toUpperCase();
        switch (option) {
            case "SKIP_CODE":
                return ClassReader.SKIP_CODE;
            case "SKIP_DEBUG":
                return ClassReader.SKIP_DEBUG;
            case "SKIP_FRAMES ":
                return ClassReader.SKIP_FRAMES;
            case "EXPAND_FRAMES":
                return ClassReader.EXPAND_FRAMES;
            default:
                return 0;
        }
    }

}
