
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Download;
import play.db.jpa.FileAttachment;
import play.jobs.Job;
import play.jobs.On;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali
 */
@On("0 0 3 * * ?")
public class QuranPDFJob extends Job{

    @Override
    public void doJob() throws Exception {
        AllQuranToPdfJob allQuran = new AllQuranToPdfJob();
        ManzilQuranToPdfJob manzilQuran = new ManzilQuranToPdfJob();
        JuzQuranToPdfJob juzQuran = new JuzQuranToPdfJob();
        try {
            System.out.println("Do all quran job");
            allQuran.doJob();
            File allQuranFile = new File("MyQuran/tmp/coran.pdf");
            Download d = Download.find("byIsActifAndName", true, "coranAll").first();
            if (d != null && d.pdfFile.get() !=null) {
                d.pdfFile.get().delete();
                d.delete();
            }

            d = new Download();
            d.isActif = true;
            d.name = "coranAll";
            d.pdfFile = new FileAttachment();
            d.pdfFile.set(allQuranFile);
            d.pdfFile.filename = "coran.pdf";
            d.save();

            System.out.println("All quran finish");

            System.out.println("Do manzil quran job");
            manzilQuran.doJob();
            File manzilQuranFile = new File("MyQuran/tmp/quranManzil.zip");
            d = Download.find("byIsActifAndName", true, "quranManzil").first();
            if (d != null && d.pdfFile.get() !=null) {
                d.pdfFile.get().delete();
                d.delete();
            }

            d = new Download();
            d.isActif = true;
            d.name = "quranManzil";
            d.pdfFile = new FileAttachment();
            d.pdfFile.set(manzilQuranFile);
            d.pdfFile.filename = "quranManzil.zip";
            d.save();

            System.out.println("Manzil quran finish");

            System.out.println("Do juz quran job");
            juzQuran.doJob();
            File juzQuranFile = new File("MyQuran/tmp/quranJuz.zip");
            d = Download.find("byIsActifAndName", true, "quranJuz").first();
            if (d != null && d.pdfFile.get() !=null) {
                d.pdfFile.get().delete();
                d.delete();
            }

            d = new Download();
            d.isActif = true;
            d.name = "quranJuz";
            d.pdfFile = new FileAttachment();
            d.pdfFile.set(juzQuranFile);
            d.pdfFile.filename = "quranJuz.zip";
            d.save();

            System.out.println("Juz quran finish");

        } catch (Exception ex) {
            Logger.getLogger(Bootstrap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
