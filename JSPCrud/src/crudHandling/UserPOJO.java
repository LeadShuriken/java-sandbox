package crudHandling;

/**
 * class UserPOJO
 * <p>
 * A Simple POJO handling current user data. 
 */
public class UserPOJO {
	private int id;
	private String name;
	private String password;
	private String email;

	public UserPOJO() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
}
