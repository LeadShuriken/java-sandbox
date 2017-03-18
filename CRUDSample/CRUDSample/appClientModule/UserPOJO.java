import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * class UserPOJO
 * <p>
 * A Simple POJO handling current user data. 
 */
public class UserPOJO {
	String[] peoples = {"Bob","Jill","Tom","Brandon","Erica"};
	private SecureRandom random = new SecureRandom();
	private String name;
	private String pass;
	private String eMail;

	public UserPOJO() {
        List<String> names = Arrays.asList(peoples);
        Collections.shuffle(names);
        
		this.name = names.get(0);
		this.pass = new BigInteger(32, random).toString(32);
		this.eMail = this.name + "@mail.bg";
	}

	public String getName() {
		return this.name;
	}
	
	public String getPass() {
		return this.pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void setMail(String mail) {
		this.eMail = mail;
	}
	
	public String getMail() {
		return this.eMail;
	}
}
