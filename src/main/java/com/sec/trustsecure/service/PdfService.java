package com.sec.trustsecure.service;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generatePdf(String content) {

        try {
            System.out.println("PDF CONTENT: " + content);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            com.itextpdf.kernel.pdf.PdfDocument pdf =
                    new com.itextpdf.kernel.pdf.PdfDocument(writer);

            Document document = new Document(pdf);

            document.add(new Paragraph("Firewall Risk Report\n\n"));

            // ✅ Prevent null crash
            document.add(new Paragraph(
                    content != null ? content : "No data available"
            ));

            document.close(); // VERY IMPORTANT

            byte[] result = out.toByteArray();

            System.out.println("PDF SIZE: " + result.length);

            return result;

        } catch (Exception e) {
            e.printStackTrace();

            // ❌ NEVER return null
            return new byte[0];
        }
    }
}