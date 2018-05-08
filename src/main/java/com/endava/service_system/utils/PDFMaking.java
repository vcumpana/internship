package com.endava.service_system.utils;

import com.endava.service_system.dto.ServiceToUserDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.Font.FontFamily.TIMES_ROMAN;

public class PDFMaking {
    public static void makePDFOfServices(List<ServiceToUserDto> services) {
        System.out.println("Creating PDF");
        Document document = new Document();
        PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 3, 1});
        table.setWidthPercentage(90f);
        insertCell(table, "Category");
        insertCell(table, "Company");
        insertCell(table, "Title");
        insertCell(table, "Description");
        insertCell(table, "Price");
        table.setHeaderRows(1);
        for (ServiceToUserDto service : services) {
            insertCell(table, service.getCategory());
            insertCell(table, service.getCompanyName());
            insertCell(table, service.getTitle());
            insertCell(table, service.getDescription());
            insertCell(table, service.getPrice().toString());
        }

        try {
            document.open();
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(TIMES_ROMAN, 12)));
        cell.setHorizontalAlignment(ALIGN_LEFT);
        table.addCell(cell);
    }
}
