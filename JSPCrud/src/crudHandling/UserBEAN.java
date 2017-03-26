package crudHandling;

/**
 * class UserBEAN
 * <p>
 * A Simple BEAN handling current user data. 
 */
public class UserBEAN {
	private int id;
	private String name;
	private String password;
	private String email;

	public UserBEAN() {
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
