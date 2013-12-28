package stni.js4java;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
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
 * @requiresDependencyResolution compile
 * @phase generate-resources
 * @goal generate
 */
public class GenerateInterfacesMojo extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter expression="${includes}"
     * @required
     */
    private String includes;

    /**
     * @parameter expression="${package}"
     * @required
     */
    private String packge;

    /**
     * @parameter expression="${importPackages}"
     * @required
     */
    private String importPackages;

    private File outputDir;
    private DefaultJsDocParser parser;
    private DefaultInterfaceCreator creator;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            outputDir = handleOutputDirectories();
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

    private void createOutput(File file) throws IOException {
        final List<JsDoc> jsDocs = parser.parseJsDoc(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String name = file.getName().substring(0, file.getName().length() - 3);
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(outputDir, name + ".java")), "utf-8");
        creator.createInterface(jsDocs, packge.replace('.', '/') + "/" + name, out);
    }

    private List<File> findInputs() throws IOException {
        final String includes = prepend(project.getBasedir().getName() + "/", this.includes);
        final File basedir = project.getBasedir().getParentFile();
        getLog().info("Input from " + basedir + " " + includes);
        @SuppressWarnings("unchecked")
        final List files = FileUtils.getFiles(basedir, includes, null);
        return files;
    }

    private File handleOutputDirectories() {
        final String output = "target/generated-sources/js4java";
        final File dest = new File(project.getBasedir(), output + "/" + packge.replace('.', '/'));
        dest.mkdirs();
        project.addCompileSourceRoot(output);
        return dest;
    }

    private String prepend(String prefix, String includes) {
        final String[] split = includes.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].startsWith("../") ? split[i].substring(3) : (prefix + split[i]);
        }
        return StringUtils.join(split, ",");
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
