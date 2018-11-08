package edu.bsu.cs222.finalproject.backend;

import com.lowagie.text.DocumentException;
import edu.bsu.cs222.finalproject.database.Repair;
import org.xhtmlrenderer.pdf.ToPDF;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        String output = "<!DOCTYPE html>";
        output += "<html><body>";
        output += "<h1>Stihl Repair</h1>";
        output += "Serial Number: " + repair.itemId;
        output += "</body></html>";
        return output;
    }


}