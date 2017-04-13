public class instPOJO {
	private String schoolName;
	private int begin_date;
	private int teachersNumber;
	private int studentsNumber;
	
	instPOJO(){}
	
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(int begin_date) {
		this.begin_date = begin_date;
	}

	public int getTeachersNumber() {
		return teachersNumber;
	}

	public void setTeachersNumber(int teachersNumber) {
		this.teachersNumber = teachersNumber;
	}

	public int getStudentsNumber() {
		return studentsNumber;
	}

	public void setStudentsNumber(int studentsNumber) {
		this.studentsNumber = studentsNumber;
	}
	
	public void addStudent(){
		this.studentsNumber++;
	}
	
	public void addTeacher(){
		this.teachersNumber++;
	}
	
	@Override
	public String toString() {
		return "instPOJO [schoolName=" + schoolName + ", begin_date=" + begin_date + ", teachersNumber="
				+ teachersNumber + ", studentsNumber=" + studentsNumber + "]";
	}
}
