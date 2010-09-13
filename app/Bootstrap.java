
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Download;
import models.User;
import models.forum.Forum;
import models.forum.ForumCategory;
import play.db.jpa.FileAttachment;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali
 */
@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Check if the database is empty
        if (User.count() == 0) {
            Fixtures.load("initial-data.yml");
        }

        if(ForumCategory.count() == 0){
            ForumCategory fc = new ForumCategory();
            fc.categoryOrder = 0;
            fc.name = "Propositions des utilisateurs";
            fc.save();

            Forum f = new Forum();
            f.forumOrder = 0;
            f.isVisible = true;
            f.name = "Propositions de traduction";
            f.nbPost = 0;
            f.nbTopic = 0;
            f.state = 0;
            f.save();
            

            Forum f2 = new Forum();
            f2.forumOrder = 1;
            f2.isVisible = true;
            f2.name = "Propositions de commentaires";
            f2.nbPost = 0;
            f2.nbTopic = 0;
            f2.state = 0;
            f2.save();
            

            Forum f3 = new Forum();
            f3.forumOrder = 2;
            f3.isVisible = true;
            f3.name = "Propositions de chapitres";
            f3.nbPost = 0;
            f3.nbTopic = 0;
            f3.state = 0;
            f3.save();

            fc.forums = new ArrayList<Forum>();
            fc.forums.add(f);
            fc.forums.add(f2);
            fc.forums.add(f3);

            fc.save();
        }

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

