package stud;

import javax.swing.*;

public class Student extends Person {
	private static final long serialVersionUID = 1L;
	int marks[];

	public Student() {
		marks = new int[5];
		for (int i = 0; i < marks.length; i++) {
			do {
				String s = JOptionPane.showInputDialog("Please enter marks " + (i + 1) + " [0 - 6]");
				if (s == null)
					s = "0"; // to terminate input with cancel
				try {
					marks[i] = Integer.parseInt(s);
				} catch (Exception e) {
					marks[i] = 7;
				}
			} while ((marks[i] < 0) || (marks[i] > 6));
			if (marks[i] == 0)
				break;
		}
	}

	@Override
	public String toString() {
		String s = super.toString() + " marks: ";
		for (int i = 0; i < marks.length; i++) {
			if (marks[i] == 0)
				break;
			s += marks[i] + " ";
		}
		return s;
	}

	public String getName() {
		return this.name;
	}
}