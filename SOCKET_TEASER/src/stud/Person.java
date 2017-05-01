package stud;

import java.io.*;
import javax.swing.*;

class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	String name = "";
	String id = "";

	public Person() {
		do {
			name = JOptionPane.showInputDialog("Please enter person's name");
		} while ((name == null) || (name.length() == 0));

		do {
			id = JOptionPane.showInputDialog("Please enter person' id");
		} while ((id == null) || (id.length() == 0));

	}

	@Override
	public String toString() {
		return name + " " + id;
	}
}
