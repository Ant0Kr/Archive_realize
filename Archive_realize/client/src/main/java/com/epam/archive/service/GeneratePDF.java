package com.epam.archive.service;

import com.epam.archive.client.controllers.ClientMainController;
import com.epam.archive.models.Person;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Antoha12018 on 30.04.2017.
 */
public class GeneratePDF {

    private static Font TIME_ROMAN_BIG = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private static Font TIME_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);

    public static void createPDF(String way) throws FileNotFoundException, DocumentException, SQLException, ClassNotFoundException {

        File file = new File(way);
        if (file.exists())
            file.delete();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(way));
        document.open();
        addMetaDataToPDF(document);
        addTitlePageToPDF(document);
        createTableOnPDF(document);
        document.close();
    }

    private static void addMetaDataToPDF(Document document) {
        document.addTitle("Table in PDF");
        document.addSubject("PDF doc");
        document.addAuthor("Antoha Karachun");
        document.addCreator("antoha12018");
    }

    private static void addTitlePageToPDF(Document document) throws DocumentException {

        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(" "));
        paragraph.add(new Paragraph("Archive", TIME_ROMAN_BIG));

        paragraph.add(new Paragraph(" "));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        paragraph.add(new Paragraph("PDF created date " + simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));
        document.add(paragraph);

    }

    private static void createTableOnPDF(Document document)
            throws DocumentException, ClassNotFoundException, SQLException {

        Paragraph paragraph = new Paragraph();
        paragraph.add(new Paragraph(" "));
        paragraph.add(new Paragraph(" "));
        document.add(paragraph);
        PdfPTable table;
        table = new PdfPTable(7);

        PdfPCell c1 = new PdfPCell(new Phrase("Surname", TIME_ROMAN_SMALL));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Name", TIME_ROMAN_SMALL));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Fathername", TIME_ROMAN_SMALL));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Phone", TIME_ROMAN_SMALL));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("EMail", TIME_ROMAN_SMALL));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Name of Job", TIME_ROMAN_SMALL));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Job Experience", TIME_ROMAN_SMALL));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);
        LinkedList<Person> item;
        synchronized (ClientMainController.getArchiveList()) {
            item = (LinkedList<Person>) ClientMainController.getArchiveList();
        }

        int size = item.size();
        for (int i = 0; i < size; i++) {

            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            Person person = item.get(i);
            table.addCell(person.getSurname());
            table.addCell(person.getName());
            table.addCell(person.getFathername());
            table.addCell(person.getPhone());
            table.addCell(person.getEMail());
            table.addCell(person.getNameJob());
            table.addCell(person.getExperienceJob());

        }

        document.add(table);
    }
}
