# Maven ASM Plugin
`maven plugin for the asm code compile`

![version](https://img.shields.io/badge/version-1.0.0-green.svg)
[![GitHub license](https://img.shields.io/github/license/Crab2died/maven-asm-plugin.svg)](https://github.com/Crab2died/maven-asm-plugin/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)](https://search.maven.org/search?q=a:maven-asm-plugin)
[![javadoc](https://javadoc.io/badge2/com.github.crab2died/maven-asm-plugin/1.0.0/javadoc.svg)](https://javadoc.io/doc/com.github.crab2died/maven-asm-plugin/1.0.0)

## Quick Use
### Maven Plugin Configuration in pom.xml
```xml
<build>
    <plugins>
        ...
        <plugin>
            <groupId>com.github.crab2died</groupId>
            <artifactId>maven-asm-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <includes>**/**.class</includes>
                <excludes>**/exclude/**.class</excludes>
                <output>${project.build.directory}</output>
                <params>
                    <param>
                        <target>packageNam.TargetClassName#targetMethodName</target>
                        <replacer>packageName.ReplacerClassName#replacerMethodName</replacer>
                    </param>
                    <param>
                        <target>packageNam.TargetClassName#targetMethodName(packageNam.ParmaClassName, packageNam.ParmaClassName)packageNam.ReturnClassName</target>
                        <replacer>packageName.ReplacerClassName#replacerMethodName(packageNam.ParmaClassName, packageNam.ParmaClassName)packageNam.ReturnClassName</replacer>
                    </param>
                </params>
                <asmApiVersion>ASM9</asmApiVersion>
                <asmClassWriterFlags>COMPUTE_MAXS | COMPUTE_FRAMES</asmClassWriterFlags>
                <asmParsingOptions>SKIP_CODE | SKIP_DEBUG</asmParsingOptions>
            </configuration>
            <executions>
                <execution>
                    <id>asm-method-replacer</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>asm-method-replacer</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        ...
    </plugins>
</build>
```
### Plugin Configuration Parameters
- includes: include files, default `**/**.class`
- excludes: exclude files, default empty
- output: output directory, default `${project.build.directory}`
- params: method replace configuration settings
  - target: target method description, refer: Method Description
  - replacer: replace method description, refer: Method Description
- asmApiVersion: ASM API Version, `ASM4, ASM5, ASM6, ASM7, ASM8, ASM9`, default `ASM9`
- asmClassWriterFlags: ASM class writer flags, `COMPUTE_MAXS | COMPUTE_FRAMES` or empty, default empty
- asmParsingOptions: ASM class reader parsing options, `SKIP_CODE | SKIP_DEBUG | SKIP_FRAMES | EXPAND_FRAMES` or empty, default empty

### Method Description
- method not with arguments and return: `packageNam.ClassName#methodName`
- method with arguments and return: `packageNam.ClassName#methodName(packageNam.ParmaClassName, packageNam.ParmaClassName)packageNam.ReturnClassName`
