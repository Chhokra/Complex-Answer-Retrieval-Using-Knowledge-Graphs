import org.json.simple.*; 
import org.json.simple.parser.*;
import java.nio.file.*;
import java.io.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.Set;
public class Indexing {
	
	
	public static void main(String[] args) {
		String[] array = new String[8];
		array[0]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_00";
		array[1]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_01";
		array[2]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_02";
		array[3]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_03";
		array[4]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_04";
		array[5]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_05";
		array[6]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_06";
		array[7]  = "/Users/rohanchhokra/eclipse-workspace/BTP_Final/src/wiki_07";
		
		
		
		try {
			
			// Opening the index code 
			Directory dir = FSDirectory.open(Paths.get("/Users/rohanchhokra/eclipse-workspace/BTP_Final/index"));
			Analyzer analyzer = new CustomAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter indexWriter = new IndexWriter(dir, iwc);
            
            //Traversing the dataset to make documents of each JSON Object and make a document out of that which will finally be added to the index
			for(int i = 0;i<array.length;i++) {
				File file = new File(array[i]);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String st;
				JSONParser parser  = new JSONParser();
				while((st = br.readLine())!=null) {
					JSONObject object = (JSONObject) parser.parse(st);
					Document document = new Document();
					System.out.println("Adding "+object);
					for(String field : (Set<String>) object.keySet()) {
						if(field.equals("title")) {
							document.add(new TextField(field,(String)object.get(field),Field.Store.YES));
						}
						if(field.equals("id")) {
							document.add(new StringField(field,(String)object.get(field),Field.Store.YES));
						}
						if(field.equals("text")) {
							String text =  (String) object.get("text");
							String pattern = "<a href[^>]+>|</a>|\\n|\\\"";
							text = text.replaceAll(pattern," ");
							document.add(new TextField(field,text,Field.Store.YES));
						}
					}
					
					indexWriter.addDocument(document);
					
				
				}
			}
			indexWriter.commit();
			indexWriter.close();
			System.out.println("Done");
			
			
            
		}
		catch(Exception e) {
			System.out.println(e.getClass());
		}
		
		
		
		 
	}
}
