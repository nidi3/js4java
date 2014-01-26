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
import stni.js4java.java.*;
import stni.js4java.jsdoc.DefaultJsDocParser;
import stni.js4java.jsdoc.JsDoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
     * Comma separated list of patterns to define which js files to process.
     */
    @Parameter(property = "includes", defaultValue = "**/*.js")
    private String includes = "**/*.js";

    /**
     * The base directory from where 'includes' is searched.
     */
    @Parameter(property = "includeBasedir", defaultValue = "src/main/js")
    private String includeBasedir = "src/main/js";

    /**
     * Comma separated list of java packages that should be known.
     */
    @Parameter(property = "importPackages")
    private String importPackages;

    /**
     * If jsr-303 validators should automatically be generated.
     * If true, all functions named 'isValidXxx' with one parameter and boolean return type are treated as validator functions.
     */
    @Parameter(property = "generateValidators", defaultValue = "true")
    private boolean generateValidators = true;

    private File outputDir;
    private File includeBase;
    private DefaultJsDocParser parser=new DefaultJsDocParser();
    private List<InterfaceCreator> creators;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            initInputAndOutput();
            initCreators();

            for (File file : findInputs()) {
                createOutput(file);
            }
        } catch (IOException | DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Problem executing plugin", e);
        }
    }

    private void initInputAndOutput() {
        includeBase = new File(project.getBasedir(), includeBasedir);
        final String output = "target/generated-sources/js4java";
        outputDir = new File(project.getBasedir(), output);
        outputDir.mkdirs();
        project.addCompileSourceRoot(output);

        final Resource js = new Resource();
        js.setDirectory(includeBasedir);
        project.addResource(js);
    }

    private void initCreators() throws DependencyResolutionRequiredException, MojoExecutionException {
        final TypeResolver typeResolver = importPackages == null
                ? new DefaultTypeResolver()
                : new ClasspathTypeResolver(Arrays.asList(importPackages.split(",")), extendClasspathWithCompile());

        creators = new ArrayList<>();
        creators.add(new DefaultInterfaceCreator(typeResolver));
        if (generateValidators) {
            creators.add(new Jsr303InterfaceCreator(typeResolver));
        }
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
            for (InterfaceCreator creator : creators) {
                for (InterfaceDescriptor descriptor : creator.createInterfaces(jsDocs, name, relative.replace('/', '.'))) {
                    descriptor.write(filebase, "utf-8");
                }
            }
        }
    }

    private String relative(File baseDir, File file) {
        String res = "";
        while (file.getParentFile() != null && !file.getParentFile().equals(baseDir)) {
            file = file.getParentFile();
            res = file.getName() + "/" + res;
        }
        return res.substring(0, res.length() - 1);
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
