package edu.bsu.cs222.finalproject.backend;

import com.lowagie.text.DocumentException;
import edu.bsu.cs222.finalproject.Main;
import edu.bsu.cs222.finalproject.database.Repair;
import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ToPDF;

import java.awt.*;
import java.io.*;
import java.net.URL;

public class Print {
    public static Boolean printRepair(Repair repair) {

        try {
            File ticketHtml = new File ("ticket.html");

            FileWriter  ticketWriter = new FileWriter(ticketHtml);
            ticketWriter.write(generateRepairDoc(repair));
            ticketWriter.close();

            File ticketPdf = new File ("ticket.pdf");

            ToPDF.createPDF(ticketHtml.getPath(), ticketPdf.getPath());

            Desktop.getDesktop().print(ticketPdf);

            return true;
        } catch (IOException e) {
            System.err.println("ticket writer fault");
            e.printStackTrace();
        } catch (DocumentException e) {
            System.err.println("error in PDF creation");
            e.printStackTrace();
        }
        return false;
    }
    private static String generateRepairDoc(Repair repair) {
        Main main = Main.getInstance();

        StringBuilder output = new StringBuilder();

        //adding Strings together is usually a bad idea because Java uses copy-on-write with Strings, but the compiler will be able to figure out the string literals
        output.append(""
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
        );
        {//css file
            InputStream cssFile = main.getClass().getResourceAsStream("/print/repairTicket.css");
            StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(cssFile, writer, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.append(writer);//add the css file to the html doc using an inline <style> block
        }
        output.append(""
                + "</style>"
                + "</head>"
                + "<body>"
        );
        {//header
            URL logoA = main.getClass().getResource("/print/logoA.png");
            URL logoB = main.getClass().getResource("/print/logoB.png");
            output.append(""
                    + "<div id='header'>"
                    + "<div id='headerImages'>"
                    + "<img src='" + logoA + "' />"
                    + "<img src='" + logoB + "' />"
                    + "</div>"
                    + "<div id='headerText'>"
                    + "105 S. Forest Ave.<br />"
                    + "Brazil, IN<br />"
                    + "47834<br />"
                    + "812-448-2877<br />"
                    + "</div>"
                    + "</div>"
            );
        }
        {//customerInfo

        }
        {//description

        }
        {//itemList

        }
        {//footer

        }
        output.append(""
                + "</body>"
                + "</html>"
        );

        return output.toString();
    }


}