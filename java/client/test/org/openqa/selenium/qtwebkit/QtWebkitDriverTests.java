package org.openqa.selenium.qtwebkit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.StandardSeleniumTests;
import org.openqa.selenium.interactions.touch.TouchTests;
import org.openqa.selenium.testing.ReportSupplier;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StandardSeleniumTests.class,
        GraphicsWebSanityTest.class,
        TouchTests.class,
        QtWebDriverSwitchesTest.class,
        QtWebDriverVisualizerTest.class,
        HTML5VideoTagTest.class,
        HTML5AudioTagTest.class,
        VisualizerTest.class
})

public class QtWebkitDriverTests {

    private static final Logger logger = Logger.getLogger(QtWebkitDriverTests.class.getName());

    @BeforeClass
    public static void prepareReport() {
        ;
    }

    @AfterClass
    public static void flushReport() {
        try
        {
            File outFile = new File("build/test_logs/" + QtWebkitDriverTests.class.getName() + "_CommandReport.xml");
            outFile.mkdirs();
            if (outFile.exists())
                outFile.delete();

            if (outFile.createNewFile())
            {
                FileOutputStream outStream = new FileOutputStream(outFile);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(new DOMSource(generateXmlReport()), new StreamResult(outStream));
                outStream.flush();
                outStream.close();
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        catch (TransformerConfigurationException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        catch (TransformerException ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }


    private static Document generateXmlReport()
    {
        try
        {
            Document finalReport = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            String props = "type=\"text/xsl\" href=\"" + "transform.xsl" + "\"";
            ProcessingInstruction instruction = finalReport.createProcessingInstruction("xml-stylesheet", props);
            finalReport.appendChild(instruction);

            Element tableElement = finalReport.createElement("table");
            finalReport.appendChild(tableElement);

            HashMap<String, HashMap<String, Boolean>> commandsMap = ReportSupplier.getCommandMap();
            Iterator it = commandsMap.keySet().iterator();

            // process all commands
            while (it.hasNext())
            {
                String key = (String)it.next();
                String path = QtWebDriverExecutor.getCommandPath(key);
                String method = QtWebDriverExecutor.getCommandMethod(key);

                if (null != path && null != method)
                {
                    Element command = finalReport.createElement("command");
                    command.setAttribute("name", key);
                    command.setAttribute("path", path);
                    command.setAttribute("method", method);

                    // add command tests
                    HashMap<String, Boolean> tests = commandsMap.get(key);

                    Iterator iter = tests.entrySet().iterator();
                    while (iter.hasNext())
                    {
                        Map.Entry pair = (Map.Entry)iter.next();
                        Element test = finalReport.createElement("test");
                        test.setAttribute("name", (String)pair.getKey());
                        String passed = ((Boolean)pair.getValue()).toString();
                        test.setAttribute("passed", passed);

                        command.appendChild(test);
                    }

                    tableElement.appendChild(command);
                }

            } // while (it.hasNext())

            return finalReport;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
