
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import models.Ayat;
import models.Sourat;
import play.cache.Cache;
import play.jobs.Job;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author inf04
 */
public class ManzilQuranToPdfJob extends Job<InputStream> {

    private Font link = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLUE);
    private Font normal = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

    @Override
    public void doJob() throws Exception {

        createQuranPart(1, 1, 1, 4, -1);
        createQuranPart(2, 5, 1, 9, -1);
        createQuranPart(3, 10, 1, 15, -1);
        createQuranPart(4, 16, 1, 25, -1);
        createQuranPart(5, 26, 1, 33, -1);
        createQuranPart(6, 34, 1, 46, -1);
        createQuranPart(7, 47, 1, 114, -1);

        createZipFile();
    }

    private void createZipFile() {
        /*************
         * Compression
         *************/
        try {
            int BUFFER = 2048;

            FileOutputStream dest = new FileOutputStream("MyQuran/tmp/quranManzil.zip");
            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            BufferedOutputStream buff = new BufferedOutputStream(checksum);
            ZipOutputStream out = new ZipOutputStream(buff);
            out.setMethod(ZipOutputStream.DEFLATED);
            out.setLevel(Deflater.BEST_COMPRESSION);
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < 7; i++) {
                File file = new File("MyQuran/tmp/coran" + (i+1) + ".pdf");
                FileInputStream fi = new FileInputStream(file);
                BufferedInputStream buffi = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);

                int count;
                while ((count = buffi.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }

                out.closeEntry();
                buffi.close();
                file.delete();
            }

            out.close();
            buff.close();
            checksum.close();
            dest.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createQuranPart(int part, int beginSourat, int beginAyat,
            int endSourat, int endAyat) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream("MyQuran/tmp/coran" + part + ".pdf"));
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            HeaderFooter event = new HeaderFooter();
            writer.setPageEvent(event);

            document.open();
            addMetaData(document);
            addTitlePage(document, part);
            addSummary(document, beginSourat, endSourat);
            addAllSourats(document, beginSourat, beginAyat,
                    endSourat, endAyat);
            document.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManzilQuranToPdfJob.class.getName()).log(Level.SEVERE, null, ex);
        } // iText allows to add metadata to the PDF which can be viewed in your Adobe
        // Reader
        // under File -> Properties
        catch (DocumentException ex) {
            Logger.getLogger(ManzilQuranToPdfJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties

    private void addMetaData(Document document) {
        document.addTitle("Coran");
        document.addSubject("Coran");
        document.addAuthor("coran.al-imane.org");
        document.addCreator("coran.al-imane.org");
    }

    private void addTitlePage(Document document, int part)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 16);
        // Lets write a big header
        Paragraph p = new Paragraph("Le Saint Coran", new com.itextpdf.text.Font(FontFamily.HELVETICA, 45, Font.BOLD));
        
        preface.setAlignment(Paragraph.ALIGN_CENTER);
        preface.add(p);
        addEmptyLine(preface, 1);
        p = new Paragraph("Partie " + part, new Font(FontFamily.HELVETICA, 30, Font.BOLD));
        preface.add(p);

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private void addSummary(Document document, int beginSourat, int endSourat) throws DocumentException {
        Paragraph page = new Paragraph();

        Anchor p = new Anchor("Sommaire", new Font(FontFamily.HELVETICA, 12, Font.BOLD));
        p.setName("summary");

        page.add(p);
        LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, 0);
        page.add(line);
        addEmptyLine(page, 1);

        List<Sourat> sourats = Sourat.find("number >= ? and number <= ? "
                + "order by number", beginSourat, endSourat).fetch();

        for (Sourat s : sourats) {
            Anchor sourat = new Anchor(s.number + ". " + s.title, link);
            sourat.setReference("#" + s.id);
            page.add(sourat);
            page.add(Chunk.NEWLINE);
        }

        document.add(page);
        // Start a new page
        document.newPage();

    }

    private void addAllSourats(Document document, int beginSourat, int beginAyat,
            int endSourat, int endAyat) throws DocumentException {

        List<Sourat> sourats = Sourat.find("number >= ? and number <= ? "
                + "order by number", beginSourat, endSourat).fetch();

        for (Sourat s : sourats) {
            List<Ayat> ayats = null;
            if (Cache.get("sourat_ayat_" + s.number) == null) {
                ayats = Ayat.find("bySourat", s).fetch();
                Cache.set("sourat_ayat_" + s.number, ayats, "24h");
            } else {
                ayats = (List<Ayat>) Cache.get("sourat_ayat_" + s.number);
            }

            Paragraph souratTitle = new Paragraph();
            souratTitle.setAlignment(Paragraph.ALIGN_CENTER);

            Phrase souratName = new Phrase();
            Anchor p = new Anchor("Sourate " + s.number + " : " + s.title.toUpperCase(), new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD));
            p.setName("" + s.id);
            p.setLeading(10);
            souratName.add(p);

            Phrase souratAyatCount = new Phrase();
            Chunk ayatCount = new Chunk(ayats.size() + " versets", normal);
            souratAyatCount.add(ayatCount);

            souratTitle.add(souratName);
            souratTitle.add(Chunk.NEWLINE);
            souratTitle.add(souratAyatCount);

            addEmptyLine(souratTitle, 2);
            document.add(souratTitle);

            if (s.number != 1 && s.number != 9) {
                Paragraph basmallah = new Paragraph("Au nom d'Allah, le Tout "
                        + "Miséricordieux, le Très Miséricordieux.",
                        new Font(FontFamily.TIMES_ROMAN, 10, Font.ITALIC));
                addEmptyLine(basmallah, 2);

                document.add(basmallah);
            }


            for (int i = 0; i < ayats.size(); i++) {
                if (endAyat != -1 && endAyat > i) {
                    break;
                }
                Ayat ayat = ayats.get(i);
                Chunk ayatNumber = new Chunk(ayat.sa + ". ", new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD));
                Chunk ayatContent = new Chunk(ayat.content.trim(), normal);
                Phrase ayatPhrase = new Phrase();
                ayatPhrase.add(ayatNumber);
                ayatPhrase.add(ayatContent);

                Paragraph ayatParagraph = new Paragraph();
                ayatParagraph.setLeading(12);
                ayatParagraph.add(ayatPhrase);
                addEmptyLine(ayatParagraph, 2);

                document.add(ayatParagraph);

            }

            // Start a new page
            document.newPage();
        }

    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    class HeaderFooter extends PdfPageEventHelper {

        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(
                    String.format("Page %d", writer.getPageNumber()), normal),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
            Anchor coran = new Anchor("coran.al-imane.org", link);
            coran.setReference("http://coran.al-imane.org");

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, coran,
                    rect.getRight(), rect.getBottom() - 18, 0);

            Anchor summary = new Anchor("Sommaire", link);
            summary.setReference("#summary");

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, summary,
                    rect.getLeft(), rect.getBottom() - 18, 0);
        }
    }
}
