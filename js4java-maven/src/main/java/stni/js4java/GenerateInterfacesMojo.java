package stni.js4java;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.util.FileUtils;
import stni.js4java.java.ClasspathTypeResolver;
import stni.js4java.java.DefaultInterfaceCreator;
import stni.js4java.java.DefaultTypeResolver;
import stni.js4java.java.TypeResolver;
import stni.js4java.jsdoc.DefaultJsDocParser;
import stni.js4java.jsdoc.JsDoc;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateInterfacesMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true, required = true)
    private MavenProject project;

    /**
     */
    @Parameter(property = "includes", defaultValue = "**/*.js")
    private String includes = "**/*.js";

    /**
     */
    @Parameter(property = "includeBasedir", defaultValue = "src/main/js")
    private String includeBasedir = "src/main/js";

    /**
     */
    @Parameter(property = "importPackages")
    private String importPackages;

    private File outputDir;
    private File includeBase;
    private DefaultJsDocParser parser;
    private DefaultInterfaceCreator creator;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            handleOutputDirectories();
            parser = new DefaultJsDocParser();
            final TypeResolver typeResolver = importPackages == null
                    ? new DefaultTypeResolver()
                    : new ClasspathTypeResolver(Arrays.asList(importPackages.split(",")), extendClasspathWithCompile());
            creator = new DefaultInterfaceCreator(typeResolver);

            for (File file : findInputs()) {
                createOutput(file);
            }
        } catch (IOException | DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Problem executing plugin", e);
        }
    }

    private void handleOutputDirectories() {
        includeBase = new File(project.getBasedir(), includeBasedir);
        final String output = "target/generated-sources/js4java";
        outputDir = new File(project.getBasedir(), output);
        outputDir.mkdirs();
        project.addCompileSourceRoot(output);

        final Resource js = new Resource();
        js.setDirectory(includeBasedir);
        project.addResource(js);
    }

    private List<File> findInputs() throws IOException {
        @SuppressWarnings("unchecked")
        final List<File> files = FileUtils.getFiles(includeBase, includes, null);
        return files;
    }

    private void createOutput(File file) throws IOException {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new InputStreamReader(new FileInputStream(file), "utf-8"));
        if (!jsDocs.isEmpty()) {
            String name = file.getName().substring(0, file.getName().length() - 3);
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            final String relative = relative(includeBase, file);
            File filebase = new File(outputDir, relative);
            filebase.mkdirs();
            final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(filebase, name + ".java")), "utf-8");
            creator.createInterface(jsDocs, relative.replace('/', '.') + name, out);
        }
    }

    private String relative(File baseDir, File file) {
        String res = "";
        while (file.getParentFile() != null && !file.getParentFile().equals(baseDir)) {
            file = file.getParentFile();
            res = file.getName() + "/" + res;
        }
        return res;
    }

    protected ClassLoader extendPluginClasspath(List<String> elements) throws MojoExecutionException {
        ClassWorld world = new ClassWorld();
        try {
            ClassRealm realm = world.newRealm("maven", Thread.currentThread().getContextClassLoader());
            for (String element : elements) {
                File elementFile = new File(element);
                getLog().debug("*** Adding element to plugin classpath " + elementFile.getPath());
                realm.addConstituent(elementFile.toURI().toURL());
            }
            Thread.currentThread().setContextClassLoader(realm.getClassLoader());
            return realm.getClassLoader();
        } catch (Exception ex) {
            throw new MojoExecutionException(ex.toString(), ex);
        }
    }

    protected List<String> compileClasspath() throws DependencyResolutionRequiredException {
        @SuppressWarnings("unchecked")
        final List<String> compileClasspathElements = project.getCompileClasspathElements();
        return compileClasspathElements;
    }

    protected ClassLoader extendClasspathWithCompile() throws DependencyResolutionRequiredException, MojoExecutionException {
        return extendPluginClasspath(compileClasspath());
    }
}
