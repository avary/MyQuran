
import java.io.IOException;
import java.util.logging.Level;
import play.Logger;
import org.apache.lucene.store.FSDirectory;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import search.Autocompleter;
import search.SearchModule;

@OnApplicationStart
public class SearchReindexJob extends Job {

    public void doJob() throws Exception {
        if (Boolean.parseBoolean(Play.configuration.getProperty("play.search.reindex", "false"))
                || Play.configuration.getProperty("play.search.reindex", "false").trim().equals("enabled")) {
            try {
                SearchModule.init();
                SearchModule.reindex();

                /*
                try {
                    Autocompleter autocomplete = new Autocompleter(SearchModule.DATA_PATH+"/autocomplete");
                    autocomplete.reIndex(FSDirectory.getDirectory(SearchModule.DATA_PATH+"/models.Ayat", null),
                            "content");
                    Logger.info("Rebuild index autocomplete finish");
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(SearchModule.class.getName()).log(Level.SEVERE, null, ex);
                }

*/
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
