package de.jandankert.prefs;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Definition of a Properties-"Sink". The implementation will be OS-specific.
 * 
 * @author Jan Dankert
 */
public interface PrefSink
{

    /**
     * Call the sink with properties.
     * @param props
     * @param path
     * @throws IOException
     */
    void sinkProperties(Properties props, List<String> path) throws IOException;

    /**
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * @throws IOException
     */
    void open() throws IOException;
}
