import java.io.File;
import java.util.ArrayList;

public class SortRunner {
	static File folder = new File("dist/Institutions");

	public static ArrayList<String> crunchJSONFilder(final File folder) {
		ArrayList<String> pathString = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				crunchJSONFilder(fileEntry);
			} else {

				pathString.add(fileEntry.getPath());
			}
		}
		return pathString;
	}

	public static void main(String[] args) {

		ArrayList<String> pathString = SortRunner.crunchJSONFilder(folder);
		int n = pathString.size();
		SortHandler sorter = new SortHandler(n, "date");
		for (int i = 0; i < n; i++) {
			(new JSONHandler(pathString.get(i), sorter)).start();
		}
	}
}
