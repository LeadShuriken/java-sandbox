package server;

import java.util.*;
import stud.*;

class Students {
	private ArrayList<Student> st;

	public Students() {
		st = new ArrayList<Student>(10);
	}

	public synchronized void addS(Student p) {
		st.add(p);
	}

	public synchronized void rmvS(Student p) {
		st.remove(p);
	}

	public synchronized int nSt() {
		return st.size();
	}

	public synchronized void printSt() {
		Iterator<Student> itr = st.iterator();
		System.out.println("---------------------------");
		while (itr.hasNext()) {
			Student p = itr.next();
			System.out.println("" + p);
		}
		System.out.println(nSt() + " students total\n");
	}

	public synchronized String getStudents() {
		String a = "| ";
		Iterator<Student> itr = st.iterator();
		while (itr.hasNext()) {
			Student p = itr.next();
			a += p.toString() + " | ";
		}
		return a;
	}
}