package edu.bsu.cs222.finalproject.backend;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import javax.print.*;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.awt.Desktop;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.lowagie.text.DocumentException;
import edu.bsu.cs222.finalproject.database.*;
import edu.bsu.cs222.finalproject.database.Repair;
import org.xhtmlrenderer.pdf.*;
import sun.security.krb5.internal.Ticket;

public class Print {

    public static void printRepair(Repair repair)
    {

        File ticket = new File("ticket.html");


        try {
            FileWriter  ticketWriter = new FileWriter(ticket.getName());
            ticketWriter.write(generateRepairDoc(repair));
            ticketWriter.close();
            String htmlLocation = ticket.toURI().toString();
            ToPDF.createPDF(ticket.getName(), "ticket.pdf");
            Path outpath = Paths.get("ticket.pdf");
            File outfile = outpath.toFile();
            Desktop.getDesktop().print(outfile);

        } catch (IOException e) {
            System.err.println("ticket writer fault");
            e.printStackTrace();
        } catch (DocumentException e) {
            System.err.println("error in PDF creation");
            e.printStackTrace();
        }


    }
    private static String generateRepairDoc(Repair repair) {
        String output = "<!DOCTYPE html>";
        output += "<html><body>";
        output += "<h1>Stihl Repair</h1>";
        output += "Serial Number: " + repair.itemId;
        output += "</body></html>";
        return output;
    }



}