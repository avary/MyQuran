package search;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ISOLatin1AccentFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter.Side;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Search term auto-completer, works for single terms (so use on the last term
 * of the query).
 * <p>
 * Returns more popular terms first.
 *
 * @author Mat Mannion, M.Mannion@warwick.ac.uk
 */
public final class Autocompleter {

    private static final String GRAMMED_WORDS_FIELD = "words";
    private static final String SOURCE_WORD_FIELD = "sourceWord";
    private static final String COUNT_FIELD = "count";
    public final static String[] FRENCH_STOP_WORDS = {
        "a", "afin", "ai", "ainsi", "après", "attendu", "au", "aujourd", "auquel", "aussi",
        "autre", "autres", "aux", "auxquelles", "auxquels", "avait", "avant", "avec", "avoir",
        "c", "car", "ce", "ceci", "cela", "celle", "celles", "celui", "cependant", "certain",
        "certaine", "certaines", "certains", "ces", "cet", "cette", "ceux", "chez", "ci",
        "combien", "comme", "comment", "concernant", "contre", "d", "dans", "de", "debout",
        "dedans", "dehors", "delà", "depuis", "derrière", "des", "désormais", "desquelles",
        "desquels", "dessous", "dessus", "devant", "devers", "devra", "divers", "diverse",
        "diverses", "doit", "donc", "dont", "du", "duquel", "durant", "dès", "elle", "elles",
        "en", "entre", "environ", "est", "et", "etc", "etre", "eu", "eux", "excepté", "hormis",
        "hors", "hélas", "hui", "il", "ils", "j", "je", "jusqu", "jusque", "l", "la", "laquelle",
        "le", "lequel", "les", "lesquelles", "lesquels", "leur", "leurs", "lorsque", "lui", "là",
        "ma", "mais", "malgré", "me", "merci", "mes", "mien", "mienne", "miennes", "miens", "moi",
        "moins", "mon", "moyennant", "même", "mêmes", "n", "ne", "ni", "non", "nos", "notre",
        "nous", "néanmoins", "nôtre", "nôtres", "on", "ont", "ou", "outre", "où", "par", "parmi",
        "partant", "pas", "passé", "pendant", "plein", "plus", "plusieurs", "pour", "pourquoi",
        "proche", "près", "puisque", "qu", "quand", "que", "quel", "quelle", "quelles", "quels",
        "qui", "quoi", "quoique", "revoici", "revoilà", "s", "sa", "sans", "sauf", "se", "selon",
        "seront", "ses", "si", "sien", "sienne", "siennes", "siens", "sinon", "soi", "soit",
        "son", "sont", "sous", "suivant", "sur", "ta", "te", "tes", "tien", "tienne", "tiennes",
        "tiens", "toi", "ton", "tous", "tout", "toute", "toutes", "tu", "un", "une", "va", "vers",
        "voici", "voilà", "vos", "votre", "vous", "vu", "vôtre", "vôtres", "y", "à", "ça", "ès",
        "été", "être", "ô"
    };
    
    private final Directory autoCompleteDirectory;
    private IndexReader autoCompleteReader;
    private IndexSearcher autoCompleteSearcher;

    public Autocompleter(String autoCompleteDir) throws IOException {
        this.autoCompleteDirectory = FSDirectory.getDirectory(autoCompleteDir);

        //reOpenReader();
    }

    public List<String> suggestTermsFor(String term) throws IOException {
        // get the top 5 terms for query
        Query query = new TermQuery(new Term(GRAMMED_WORDS_FIELD, term));
        Sort sort = new Sort(COUNT_FIELD, true);

        TopDocs docs = autoCompleteSearcher.search(query, null, 5,sort);
        List<String> suggestions = new ArrayList<String>();
        for (ScoreDoc doc : docs.scoreDocs) {
            suggestions.add(autoCompleteReader.document(doc.doc).get(
                    SOURCE_WORD_FIELD));
        }

        return suggestions;
    }

    @SuppressWarnings("unchecked")
    public void reIndex(Directory sourceDirectory, String fieldToAutocomplete)
            throws CorruptIndexException, IOException {
        // build a dictionary (from the spell package)
        IndexReader sourceReader = IndexReader.open(sourceDirectory);

        LuceneDictionary dict = new LuceneDictionary(sourceReader,
                fieldToAutocomplete);

        // code from
        // org.apache.lucene.search.spell.SpellChecker.indexDictionary(
        // Dictionary)
        //IndexReader.unlock(autoCompleteDirectory);

        // use a custom analyzer so we can do EdgeNGramFiltering
        IndexWriter writer = new IndexWriter(autoCompleteDirectory,
                new Analyzer() {

                    public TokenStream tokenStream(String fieldName,
                            Reader reader) {
                        TokenStream result = new StandardTokenizer(reader);

                        result = new StandardFilter(result);
                        result = new LowerCaseFilter(result);
                        result = new ISOLatin1AccentFilter(result);
                        result = new StopFilter(result,
                                FRENCH_STOP_WORDS);
                        result = new EdgeNGramTokenFilter(
                                result, Side.FRONT, 1, 20);

                        return result;
                    }
                }, true);

        writer.setMergeFactor(300);
        writer.setMaxBufferedDocs(150);

        // go through every word, storing the original word (incl. n-grams)
        // and the number of times it occurs
        Map<String, Integer> wordsMap = new HashMap<String, Integer>();

        Iterator<String> iter = (Iterator<String>) dict.getWordsIterator();
        while (iter.hasNext()) {
            String word = iter.next();
            System.out.println("WORD : "+word);
            int len = word.length();
            if (len < 3) {
                continue; // too short we bail but "too long" is fine...
            }

            if (wordsMap.containsKey(word)) {
                throw new IllegalStateException(
                        "This should never happen in Lucene 2.3.2");
                // wordsMap.put(word, wordsMap.get(word) + 1);
            } else {
                // use the number of documents this word appears in
                wordsMap.put(word, sourceReader.docFreq(new Term(
                        fieldToAutocomplete, word)));
            }
        }

        for (String word : wordsMap.keySet()) {
            // ok index the word
            Document doc = new Document();
            doc.add(new Field(SOURCE_WORD_FIELD, word, Field.Store.YES,
                    Field.Index.UN_TOKENIZED)); // orig term
            doc.add(new Field(GRAMMED_WORDS_FIELD, word, Field.Store.YES,
                    Field.Index.TOKENIZED)); // grammed
            doc.add(new Field(COUNT_FIELD,
                    Integer.toString(wordsMap.get(word)), Field.Store.NO,
                    Field.Index.UN_TOKENIZED)); // count

            writer.addDocument(doc);
        }

        sourceReader.close();

        // close writer
        writer.optimize();
        writer.close();

        // re-open our reader
        reOpenReader();
    }

    private void reOpenReader() throws CorruptIndexException, IOException {
        if (autoCompleteReader == null) {
            autoCompleteReader = IndexReader.open(autoCompleteDirectory);
        } else {
            autoCompleteReader.reopen();
        }

        autoCompleteSearcher = new IndexSearcher(autoCompleteReader);
    }

    public static void main(String[] args) throws Exception {
        Autocompleter autocomplete = new Autocompleter("C:\\tmp\\index\\autocomplete");

        // run this to re-index from the current index, shouldn't need to do
        // this very often
         autocomplete.reIndex(FSDirectory.getDirectory("C:\\tmp\\index\\models.Ayat", null),
         "content");

        String term = "sa";
        autocomplete.reOpenReader();
        
        System.out.println(autocomplete.suggestTermsFor(term));
        // prints [steve, steven, stevens, stevenson, stevenage]
    }
}
