package de.jandankert.prefs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.jandankert.prefs.unix.PrefsXMLCreator;
import de.jandankert.prefs.win32.RegistryImport;

/**
 * Generator for Java-Preferences. Creates from *.properties-files a windows-REG-file and
 * Unix-prefs.xml-files.
 * 
 * @author Jan Dankert
 */
public class Prop2Prefs
{

    /**
     * Define all Property-Sinks.
     */
    private static PrefSink[] prefSinks = new PrefSink[]
    {new PrefsXMLCreator(), new RegistryImport()};

    /**
     * Main program.
     * 
     * @param args No args needed for this application
     */
    public static void main(String[] args)
    {
        try
        {
            // Prepare all sinks
            for (PrefSink sink : prefSinks)
                sink.open();

            // Recursively traverse through all subdirectories
            investigateDirectory(new ArrayList<String>());

            // Close all sinks
            for (PrefSink sink : prefSinks)
                sink.close();
        }
        catch (IOException e)
        {
            // Shit happenz
            e.printStackTrace(System.err);

            System.exit(-4); // Inform the caller about the mess.
        }

        // all OK :)
    }

    /**
     * Investigate a directory. This methode will be recursivly called for all subdirectories.
     * 
     * @param path Path
     * @throws IOException
     */
    private static void investigateDirectory(List<String> path) throws IOException
    {

        System.out.println("Investigate directory: " + path.toString());
        File file = new File("./" + Utils.join(path, "/"));
        File[] files = file.listFiles(new PropertyFileFilter());
        for (File propFile : files)
        {
            parsePropertyFile(propFile, path);
        }

        File[] dirs = file.listFiles(new DirectoryFilter());
        for (File subDir : dirs)
        {
            ArrayList<String> newsubDir = new ArrayList<String>(path);
            newsubDir.add(subDir.getName());
            investigateDirectory(newsubDir);
        }
    }

    /**
     * @param propFile
     * @param path
     * @throws IOException
     */
    private static void parsePropertyFile(File propFile, List<String> path) throws IOException
    {
        System.out.println("Investigate property-file: " + propFile.getName());

        Properties props = new Properties();
        props.load(new FileInputStream(propFile));

        // Call all sinks with the loaded properties.
        for (PrefSink sink : prefSinks)
            sink.sinkProperties(props, path);
    }

    private static class DirectoryFilter implements FileFilter
    {

        public boolean accept(File pathname)
        {
            return pathname.isDirectory();
        }
    }

    private static class PropertyFileFilter implements FileFilter
    {

        public boolean accept(File pathname)
        {
            return pathname.getName().endsWith(".properties");
        }
    }

}
