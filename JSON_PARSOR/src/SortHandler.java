import java.io.FileWriter;
import java.util.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SortHandler {

	boolean sortByDate;
	static int assetsNumber;
	private ArrayList<instPOJO> pojos = new ArrayList<instPOJO>();

	SortHandler(int assetsNumber, String type) {
		sortByDate = type.equals("date") ? true : false;
		this.assetsNumber = assetsNumber;
	}

	public synchronized void prepareForSorting(instPOJO pojo) {
		pojos.add(pojo);
		assetsNumber--;
		if (assetsNumber == 0) {
			if (sortByDate) {
				Collections.sort(pojos, (p1, p2) -> p1.getBegin_date() - p2.getBegin_date());
			} else {
				Collections.sort(pojos, (p1, p2) -> p1.getTeachersNumber() - p2.getTeachersNumber());
			}
			writeOutput();
		}
	}

	private void writeOutput() {
		try (FileWriter file = new FileWriter("dist/Output/output.json")) {
			int n = pojos.size();
			JSONArray stack = new JSONArray();
			for (int i = 0; i < n; i++) {
				JSONObject instituion = new JSONObject();
				instPOJO a = pojos.get(i);
				System.out.println(a.toString());
				instituion.put("name", a.getSchoolName());
				instituion.put("studentsNumber", a.getStudentsNumber());
				instituion.put("teachersNumber", a.getTeachersNumber());
				instituion.put("beginDate", a.getBegin_date());
				stack.add(instituion);
			}
			
			ObjectMapper mapper = new ObjectMapper();
			file.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stack));
			file.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
