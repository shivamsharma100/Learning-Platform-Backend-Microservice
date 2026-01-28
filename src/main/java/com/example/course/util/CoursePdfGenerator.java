package com.example.course.util;

import com.example.course.entities.Course;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CoursePdfGenerator {

    private CoursePdfGenerator(){

    }

    public static byte[] generateCoursePdf(List<Course> courses) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Title
        Paragraph title = new Paragraph("Course Catalog")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold()
                .setMarginBottom(20);

        document.add(title);

        // Table
        Table table = new Table(5);
        table.setWidth(100);

        addHeader(table, "ID");
        addHeader(table, "Title");
        addHeader(table, "Desc");
        addHeader(table, "Level");
        addHeader(table, "Duration");

        for (Course course : courses) {
            table.addCell(String.valueOf(course.getId()));
            table.addCell(course.getTitle());
            table.addCell(course.getDescription());
            table.addCell(course.getLevel());
            table.addCell(course.getDuration() + " hrs");
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    private static void addHeader(Table table, String header) {
        table.addHeaderCell(
                new Cell()
                        .add(new Paragraph(header).setBold())
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
        );
    }
}
