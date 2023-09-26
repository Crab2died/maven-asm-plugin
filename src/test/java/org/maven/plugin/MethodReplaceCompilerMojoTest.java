package org.maven.plugin;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

public class MethodReplaceCompilerMojoTest extends AbstractMojoTestCase {

    public void testCompilerBasic() throws Exception {
    }

    private MethodReplaceCompilerMojo getCompilerMojo(String pomXml) throws Exception {
        File testPom = new File(getBasedir(), pomXml);
        MethodReplaceCompilerMojo mojo = (MethodReplaceCompilerMojo) lookupMojo("compile", testPom);

        return mojo;
    }

}
