package de.jandankert.prefs;

import java.util.Collection;
import java.util.Iterator;

public final class Utils
{

    public static <T> String join(final Collection<T> objs, final String delimiter)
    {
        if (objs == null || objs.isEmpty())
            return "";
        Iterator<T> iter = objs.iterator();
        StringBuffer buffer = new StringBuffer(iter.next().toString());
        while (iter.hasNext())
            buffer.append(delimiter).append(iter.next().toString());
        return buffer.toString();
    }

}
