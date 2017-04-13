import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class JSONHandler extends Thread {

	private instPOJO pojo;
	private SortHandler sorter;
	private String filename;
	
	JSONHandler (String filename, SortHandler sorter){
		this.pojo = new instPOJO();
		this.sorter = sorter;
		this.filename = filename;
	}
	
	public void run() {
		
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(filename));

            JSONObject jsonObject = (JSONObject) obj;
            
            pojo.setSchoolName((String) jsonObject.get("name"));
            
            long a = (long) jsonObject.get("begin_date");
            pojo.setBegin_date((int) a);

            JSONArray teachers = (JSONArray) jsonObject.get("teachers");
            Iterator<JSONObject> iterator1 = teachers.iterator();
            while (iterator1.hasNext()) {
            	JSONObject newObj = (JSONObject) iterator1.next();
            	pojo.addTeacher();
            }
            
            JSONArray students = (JSONArray) jsonObject.get("students");
            Iterator<JSONObject> iterator2 = students.iterator();
            while (iterator2.hasNext()) {
            	JSONObject newObj = (JSONObject) iterator2.next();
            	pojo.addStudent();
            }
            
            sorter.prepareForSorting(pojo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}