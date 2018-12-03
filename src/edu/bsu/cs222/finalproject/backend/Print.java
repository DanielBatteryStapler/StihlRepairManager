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

        //adding Strings together is usually a bad idea because Java uses copy-on-write with Strings, but the compiler will be able to figure out the string literals and make it efficient
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
                    + "<table>"
                    + "<tr>"
                    + "<td colspan='4'>"
                    + "<div id='headerImages'>"
                    + "<img src='"); output.append(logoA); output.append("' />"
                    + "<img src='"); output.append(logoB); output.append("' />"
                    + "</div>"//headerImages
                    + "<div id='headerText'>"
                    + "105 S. Forest Ave.<br />"
                    + "Brazil, IN<br />"
                    + "47834<br />"
                    + "812-448-2877<br />"
                    + "</div>"//headerText
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td class='align-right'>"
                    + "Date Started:<br />"
                    + "Date Completed:"
                    + "</td>"
                    + "<td>");
                    output.append(repair.dateStarted.toLocaleString()); output.append("<br />");
                    if(repair.dateCompleted == null){
                        output.append("Not Completed");
                    }
                    else{
                        output.append(repair.dateCompleted.toLocaleString());
                    }
                    output.append( ""
                    + "</td>"
                    + "<td class='align-right'>"
                    + "MUST CONTACT CUSTOMER WITH ESTIMATE?"
                    + "</td>"
                    + "<td>"
                    + "TEST"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</div>"//header
            );
        }
        output.append(""
            + "<table id='posTableRoot' class='posTable'>"
            + "<tr class='posTable'>"
            + "<td rowspan='2' class='posTable'>"
        );
        {//itemList
            output.append(""
                    + "<div id='itemList'>"
                    + "<table>"
                    + "<tr>"
                    + "<th style='width:50px;'>QTY</th>"
                    + "<th style='width:250px;'>Name</th>"
                    + "<th style='width:50px;'>Price</th>"
                    + "<th style='width:50px;'>Amount</th>"
                    + "</tr>"
            );
            for(int i = 0; i < 19; i++){//just fill the table with a bunch of empty elements for now
                output.append(""
                    + "<tr>"
                    + "<td></td>"
                    + "<td></td>"
                    + "<td></td>"
                    + "<td></td>"
                    + "</tr>"
                );
            }
            //add the totals on the bottom
            output.append(""
                + "<tr>"
                + "<th></th>"
                + "<th class='align-right'>Total Materials</th>"
                + "<td></td>"
                + "<td></td>"
                + "</tr>"

                + "<tr>"
                + "<th></th>"
                + "<th class='align-right'>Total Labor</th>"
                + "<td></td>"
                + "<td></td>"
                + "</tr>"

                + "<tr>"
                + "<th></th>"
                + "<th class='align-right'>Tax</th>"
                + "<td></td>"
                + "<td></td>"
                + "</tr>"

                + "<tr>"
                + "<th></th>"
                + "<th class='align-right'>Grand Total</th>"
                + "<td></td>"
                + "<td></td>"
                + "</tr>"
            );

            output.append(""
                    + "</table>"
                    + "</div>"//itemList
            );
        }
        output.append(""
                + "</td>"
                + "<td class='posTable'>"
        );
        {//customerInfo
            output.append(""
                    + "<div id='customerInfo'>"
                    + "<b>&nbsp;&nbsp;&nbsp;Customer Information</b>"
                    + "<table>"

                    + "<tr>"
                    + "<th>Name</th>"
                    + "<td style='width:210px;'></td>"
                    + "</tr>"

                    + "<tr>"
                    + "<th>Address</th>"
                    + "<td></td>"
                    + "</tr>"

                    + "<tr>"
                    + "<th>City</th>"
                    + "<td></td>"
                    + "</tr>"

                    + "<tr>"
                    + "<th>State</th>"
                    + "<td></td>"
                    + "</tr>"

                    + "<tr>"
                    + "<th>Phone</th>"
                    + "<td></td>"
                    + "</tr>"

                    + "<tr>"
                    + "<th>Model #</th>"
                    + "<td></td>"
                    + "</tr>"

                    + "</table>"
                    + "</div>"//customerInfo
            );
        }
        output.append(""
                + "</td>"
                + "</tr>"
                + "<tr class='posTable'>"
                + "<td class='posTable'>"
        );
        {//description
            output.append(""
                    + "<div id='description'>"
                    + "Description of Issue or Repairs Needed:"
                    + "<div id='descriptionTextBox'>"
                    + "</div>"//descriptionTextBox
                    + "</div>"//description
            );
        }
        output.append(""
                + "</td>"
                + "</tr>"
                + "</table>"//posTable
        );
        {//footer
            output.append(""
                    + "Repairs Completed:"
                    + "<div id='repairsCompletedTextBox'>"
                    + "</div>"//repairsCompletedTextBox
                    + "<div id='legalBox'>"
                    + "By signing below, I agree to allow Brazil ACE Hardware to complete the work as described in the notes section of this work "
                    + "order. I also agree to pay for all Parts and Labor charges, not covered under warranty, associated with any repairs "
                    + "completed. I agree to pay a minimum $15.00 diagnostic fee if I choose not to have any repairs completed."
                    + "</div>"//legalBox
            );
        }
        output.append(""
                + "</body>"
                + "</html>"
        );

        return output.toString();
    }


}