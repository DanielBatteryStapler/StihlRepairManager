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
            File ticketHtml = File.createTempFile("ticket", ".html");

            FileWriter  ticketWriter = new FileWriter(ticketHtml);
            ticketWriter.write(generateRepairDoc(repair));
            ticketWriter.close();

            File ticketPdf = File.createTempFile("ticket", ".pdf");

            ToPDF.createPDF(ticketHtml.getPath(), ticketPdf.getPath());

            Desktop.getDesktop().print(ticketPdf);

            Boolean first = ticketHtml.delete();
            Boolean second = ticketPdf.delete();
            return first && second;//return true(success on print) if it is able to delete the two temporary files it created
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