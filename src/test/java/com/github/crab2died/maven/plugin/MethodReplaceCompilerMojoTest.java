package com.github.crab2died.maven.plugin;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class MethodReplaceCompilerMojoTest extends AbstractMojoTestCase {

    private File testPom = new File(getBasedir(), "target/test-classes/plugin.config.xml");

    public void testCompilerBasic() throws Exception {
        MethodReplaceCompilerMojo mojo = (MethodReplaceCompilerMojo) lookupMojo("compile", testPom);
        mojo.execute();
    }

}
