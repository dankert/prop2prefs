package de.jandankert.prefs.win32;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import de.jandankert.prefs.PrefSink;
import de.jandankert.prefs.Utils;

/**
 * @author Jan Dankert
 */
public class RegistryImport implements PrefSink
{

    private static FileOutputStream regedit;

    public RegistryImport()
    {
    }

    public void open() throws IOException
    {
        new File("Windows").mkdirs();
        regedit = new FileOutputStream(new File("Windows/prefs.reg"));
        regedit.write("REGEDIT4\n\n".getBytes());
    }

    public void close() throws IOException
    {
        regedit.close();
    }

    public void sinkProperties(Properties props, List<String> path) throws IOException
    {
        new File("Unix/" + Utils.join(path, "/")).mkdirs();

        regedit.write(("[HKEY_CURRENT_USER\\Software\\JavaSoft\\prefs\\" + Utils.join(path, "\\") + "]\n").getBytes());
        for (Object key : props.keySet())
        {

            Object value = props.get(key);
            regedit.write(("\"" + key + "\"=\"" + value + "\"\n").getBytes());

        }
        regedit.write("\n".getBytes());

    }
}
