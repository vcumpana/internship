package com.endava.service_system.utils;

import com.endava.service_system.model.dto.ServiceToUserDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.Font.BOLD;
import static com.itextpdf.text.Font.FontFamily.TIMES_ROMAN;

@Component
public class PDFMaking {

    public static ByteArrayOutputStream  makePDFOfServices(List<ServiceToUserDto> services) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document document = new Document();
        Font cellFont = new Font(TIMES_ROMAN, 12);
        Font cellBoldFont = new Font(TIMES_ROMAN, 12, BOLD);
        PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 3, 1});
        table.setWidthPercentage(90f);
        insertCell(table, "Category", cellBoldFont);
        insertCell(table, "Company", cellBoldFont);
        insertCell(table, "Title", cellBoldFont);
        insertCell(table, "Description", cellBoldFont);
        insertCell(table, "Price", cellBoldFont);
        table.setHeaderRows(1);
        for (ServiceToUserDto service : services) {
            insertCell(table, service.getCategory(), cellFont);
            insertCell(table, service.getCompanyName(), cellFont);
            insertCell(table, service.getTitle(), cellFont);
            insertCell(table, service.getDescription(), cellFont);
            insertCell(table, service.getPrice().toString(), cellFont);
        }

        try {
            PdfWriter.getInstance(document, stream);
            document.open();
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }

    private static void insertCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(ALIGN_LEFT);
        table.addCell(cell);
    }
}
