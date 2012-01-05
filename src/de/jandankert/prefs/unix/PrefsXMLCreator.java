package de.jandankert.prefs.unix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.jandankert.prefs.PrefSink;
import de.jandankert.prefs.Utils;

/**
 * @author Jan Dankert
 */
public class PrefsXMLCreator implements PrefSink
{

	public void open() throws IOException
	{
		// nothing to do...
		// a new file is created on each properties-set.
	}

	public void close() throws IOException
	{
	}

	public void sinkProperties(Properties props, List<String> path) throws IOException
	{
		new File("Unix/" + Utils.join(path, "/")).mkdirs();

		FileOutputStream prefsXMLOutputStream = new FileOutputStream("Unix/" + Utils.join(path, "/") + "/prefs.xml");
		DocumentBuilder builder = null;
		try
		{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e);
		}

		Document newDocument = builder.newDocument();
		Element mapElement = newDocument.createElement("map");
		for (Object key : props.keySet())
		{

			Object value = props.get(key);

			Element entry = newDocument.createElement("entry");
			entry.setAttribute("key", key.toString());
			entry.setAttribute("value", value.toString());
			mapElement.appendChild(entry);
		}

		newDocument.appendChild(mapElement);
		try
		{
			Transformer transformer = SAXTransformerFactory.newInstance().newTransformer();
			transformer.transform(new DOMSource(newDocument), new StreamResult(prefsXMLOutputStream));
			SAXTransformerFactory.newInstance().newTransformer();
		}
		catch (TransformerException e)
		{
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}

	}

}
