package data;

import java.util.ArrayList;

public class DBApplication {
	
	public static void main(String[] args) throws Exception {
		
		String[] documentTitles = new String[] { 
				"CS400 - Monday 9-17.pdf", 
				"CS400 - Monday 9-24.pdf",
				"CS400 - Friday 10-14.pdf",
				"CS400 - Wednesday 10-24.pdf",
				"CS400 - Monday 11-04.pdf",
				"CS400 - Monday 11-14.pdf",
				"CS400 - Monday 11-24.pdf",
				"CS400 - Friday 12-05.pdf",
				"CS411 - Monday 9-17.pdf", 
				"CS411 - Wednesday 9-24.pdf",
				"CS411 - Monday 10-14.pdf",
				"CS411 - Wednesday 10-24.pdf",
				"CS411 - Monday 11-04.pdf",
				"CS411 - Monday 11-14.pdf",
				"CS411 - Monday 11-24.pdf",
				"CS411 - Friday 12-05.pdf",
				"CS420 - Monday 9-17.pdf", 
				"CS420 - Monday 9-24.pdf",
				"CS420 - Friday 10-14.pdf",
				"CS420 - Monday 10-24.pdf",
				"CS420 - Wednesday 11-04.pdf",
				"CS420 - Monday 11-14.pdf",
				"CS420 - Friday 11-24.pdf",
				"CS420 - Monday 12-05.pdf" 
				};
		ArrayList<Document> list = new ArrayList<Document>();
		for (String s : documentTitles) {
			Document doc = new Document(s);
			list.add(doc);
		}
		DBInterface dbInterface = new DBInterface();
		String tableName = "Documents";
		for(Document doc : list) {
			dbInterface.insertDocument(doc.getTitle(), doc.getFile(), tableName);
		}
		System.out.println("Initialization complete");
		
	}

}
