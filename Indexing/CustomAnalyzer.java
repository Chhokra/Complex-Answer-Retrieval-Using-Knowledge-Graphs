import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;


import java.io.File;
import java.io.IOException;
import java.util.*;


public class CustomAnalyzer extends Analyzer {

    public static CharArraySet getFileContent(String stopwordlist) {
        File file = new File(stopwordlist);
//        System.out.println(file.getAbsolutePath());
        CharArraySet stopset = null;
        List<String> list = new ArrayList<String>();
        try {
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                list.add(input.nextLine());
            }

        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
        Set<String> ss = new HashSet<String>(list);
        stopset = CharArraySet.copy(ss);

        return stopset;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        StandardTokenizer src = new StandardTokenizer();
        TokenStream result = new LowerCaseFilter(src);
        CharArraySet stopset = getFileContent("/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/stopwords.txt");
        result = new StopFilter(result, stopset);
        result = new PorterStemFilter(result);
        return new TokenStreamComponents(src, result);

    }
}
