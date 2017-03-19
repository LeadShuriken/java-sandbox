import java.sql.*;

/**
 * class SimpleCrud
 * <p>
 * This class is a handler for demonstrating simple 
 * CRUD(CREATE, READ, UPDATE, DELETE) functionality. 
 */
public class SimpleCrud {

	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private String DB_URL = "jdbc:mysql://localhost:3306/";

	private final String DB_USER;
	private final String DB_PASS;
	private final String DB_NAME;

	private Connection conn;
	private PreparedStatement stmt;
	
	private UserPOJO currentUser;

	SimpleCrud(String username, String password, String dbName) {
		DB_USER = username;
		DB_PASS = password;
		DB_NAME = dbName;
		currentUser = new UserPOJO();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleCrud a = new SimpleCrud("username", "password", "crud_sample");
		a.bootStrapDatabase();

		try {
			a.insert(); // aka CREATE;
			a.select(); // aka READ;
			a.update(); // aka UPDATE;
			a.delete(); // aka DELETE;
//			a.dropDatabase(); 
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			a.disconnect();
		}
	}
	
	/**
	 * bootStrapDatabase() : void()
	 * <p>
	 * This method creates a database named 'crud_sample' and 
	 * a table named 'users' in the case that they do not exist 
	 *
	 * @return      void
	 */
	private void bootStrapDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			stmt = conn.prepareStatement("");
			
			ResultSet resultSet = conn.getMetaData().getCatalogs();

			Boolean databaseFound = false;
			Boolean tableFound = false;

			while (resultSet.next()) {
				String databaseName = resultSet.getString(1);
				if (databaseName.equals(DB_NAME)) {
					databaseFound = true;
					System.out.println("Database '" + DB_NAME + "' found...");
					DB_URL = "jdbc:mysql://localhost:3306/crud_sample";
					conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
					stmt = conn.prepareStatement("");
					ResultSet tables = conn.getMetaData().getTables(null, null, "users", null);
					if (tables.next()) {
						tableFound = true;
						System.out.println("Table 'users' found...");
					}
					break;
				}
			}
			resultSet.close();
			
			StringBuilder sql = new StringBuilder();

			if (!databaseFound) {
				sql.append("CREATE DATABASE " + DB_NAME + ";");
				stmt.executeUpdate(sql.toString());
				sql.setLength(0);
				System.out.println("Database '" + DB_NAME + "' create...");
				DB_URL = "jdbc:mysql://localhost:3306/crud_sample";
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				stmt = conn.prepareStatement("");
			}
			
			sql.append("use " + DB_NAME + ";");
			stmt.executeUpdate(sql.toString());
			sql.setLength(0);
			
			if (!tableFound) {
				sql.append("CREATE TABLE users (" 
						+ "user_id INT(64) NOT NULL AUTO_INCREMENT,"
						+ "username VARCHAR(45) NOT NULL," 
						+ "password VARCHAR(45) NOT NULL,"
						+ "email VARCHAR(45) NOT NULL,"
						+ "PRIMARY KEY (user_id))");

				stmt.executeUpdate(sql.toString());
				sql.setLength(0);
				
				System.out.println("Table 'users' create...");
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * insert() : void() throws SQLException
	 * <p>
	 * This method inserts the current user in the table 
	 * CRUD - CREATE
	 *
	 * @return      void
	 */
	private void insert() throws SQLException {
		String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";

		stmt = conn.prepareStatement(sql);
		stmt.setString(1, currentUser.getName());
		stmt.setString(2, currentUser.getPass());
		stmt.setString(3, currentUser.getMail());

		int rowsInserted = stmt.executeUpdate();
		if (rowsInserted > 0) {
			System.out.println("User named '" + currentUser.getName() + "' was created...");
		}
	}
	
	/**
	 * select() : void() throws SQLException
	 * <p>
	 * This method reads the current user/users from the table 
	 * displaying the full entry
	 * CRUD - READ
	 *
	 * @return      void
	 */
	private void select() throws SQLException {
		String sql = "SELECT * FROM Users";

		stmt = conn.prepareStatement("");
		ResultSet result = stmt.executeQuery(sql);

		int count = 0;

		while (result.next()) {
			String name = result.getString(2);
			String pass = result.getString(3);
			String email = result.getString("email");

			String output = "User #%d: %s pass: %s mail: %s was found...";
			System.out.println(String.format(output, ++count, name, pass, email));
		}
	}
	
	/**
	 * update() : void() throws SQLException
	 * <p>
	 * This method updates the mail and pass of the current user in the table
	 * CRUD - UPDATE
	 * @return      void
	 */
	private void update() throws SQLException {
		String sql = "UPDATE Users SET password=?, email=? WHERE username=?";
		currentUser.setMail(currentUser.getName() + "@gmail.com");
		currentUser.setPass("sssshhhh");

		stmt = conn.prepareStatement(sql);
		stmt.setString(1, currentUser.getPass());
		stmt.setString(2, currentUser.getMail());
		stmt.setString(3, currentUser.getName());

		int rowsUpdated = stmt.executeUpdate();
		if (rowsUpdated > 0) {
			System.out.println("User named '" + currentUser.getName() + "' was updated...");
		}
	}
	
	/**
	 * delete() : void() throws SQLException
	 * <p>
	 * This method deletes the current user from the table
	 * CRUD - DELETE
	 * @return      void
	 */
	private void delete() throws SQLException {
		String sql = "DELETE FROM Users WHERE username=?";

		stmt = conn.prepareStatement(sql);
		stmt.setString(1, currentUser.getName());

		int rowsDeleted = stmt.executeUpdate();
		if (rowsDeleted > 0) {
			System.out.println("User named '" + currentUser.getName() + "' was deleted...");
		}
	}
	
	/**
	 * dropDatabase() : void() throws SQLException
	 * <p>
	 * This method drops the created database
	 * CRUD - DELETE
	 * @return      void
	 */
	private void dropDatabase() throws SQLException {
		String sql =  "DROP DATABASE " + DB_NAME + ";";

		stmt = conn.prepareStatement(sql);
		int dataBaseDel = stmt.executeUpdate();
		if (dataBaseDel > 0) {
			System.out.println(DB_NAME + " was dropped...");
		}
	}
	
	
	/**
	 * disconnect() : void()
	 * <p>
	 * This method safely disconnects from the database
	 * @return      void
	 */
	private void disconnect() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se2) {
			// nothing we can do
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
}
