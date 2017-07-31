package common;

import java.rmi.*;

public class ShowBindings {
	public static void main(String args[]) {
		try {
			String[] b = Naming.list("");
			for (int a = 0; a < b.length; a++) {
				System.out.println(b[a]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
