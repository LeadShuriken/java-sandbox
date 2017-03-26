package crudHandling;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class SimpleCrud
 * <p>
 * This class is a handler for demonstrating simple CRUD(CREATE, READ, UPDATE,
 * DELETE) functionality.
 */
public class SimpleCrud {

	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://localhost:3306/";

	private static String DB_USER = "username";
	private static String DB_PASS = "password";
	private static String DB_NAME = "crud_sample";

	private static Connection conn = null;
	private static PreparedStatement stmt = null;

	/**
	 * bootStrapDatabase() : void()
	 * <p>
	 * This method creates a database named 'crud_sample' and a table named
	 * 'users' in the case that they do not exist
	 *
	 * @return void
	 */
	public static void bootStrapDatabase() {
		try {
			// Connect to the database //
			Class.forName("com.mysql.jdbc.Driver");
			conn = getConnection();
			stmt = conn.prepareStatement("");
			StringBuilder sql = new StringBuilder();

			// Create/Connect DB//
			sql.append("CREATE DATABASE IF NOT EXISTS " + DB_NAME + ";");
			stmt.executeUpdate(sql.toString());
			sql.setLength(0);
			System.out.println("Database '" + DB_NAME + "' created/found...");

			DB_URL = "jdbc:mysql://localhost:3306/crud_sample";
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			stmt = conn.prepareStatement("");

			sql.append("use " + DB_NAME + ";");
			stmt.executeUpdate(sql.toString());
			sql.setLength(0);

			// Create/Connect Table//
			sql.append("CREATE TABLE IF NOT EXISTS users (" +
					"id INT(64) NOT NULL AUTO_INCREMENT," +
					"username VARCHAR(45) NOT NULL," +
					"password VARCHAR(45) NOT NULL," +
					"email VARCHAR(45) NOT NULL," +
					"PRIMARY KEY (id))");

			stmt.executeUpdate(sql.toString());
			sql.setLength(0);

			System.out.println("Table 'users' create/found...");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * getConnection()
	 * <p>
	 * This method creates a connection to the database
	 *
	 * @return Connection The connection established
	 */
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}

	/**
	 * insert(UserBEAN currentUser) : void()
	 * <p>
	 * This method inserts the current user in the table CRUD - CREATE
	 * 
	 * @param currentUser
	 *            The user to insert to the DB
	 * 
	 * @return Integer representing the success/failure state of the query
	 */
	public static int insert(UserBEAN currentUser) {
		int rowsInserted = 0;
		try {
			if (conn == null) {
				bootStrapDatabase();
			}

			String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, currentUser.getName());
			stmt.setString(2, currentUser.getPassword());
			stmt.setString(3, currentUser.getEmail());

			rowsInserted = stmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("User named '" + currentUser.getName() + "' was created...");
			}
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return rowsInserted;
	}

	/**
	 * update(UserBEAN currentUser) : void()
	 * <p>
	 * This method updates the mail and pass of the current user in the table
	 * CRUD - UPDATE
	 * 
	 * @param currentUser
	 *            The user to update in the DB
	 * 
	 * @return Integer representing the success/failure state of the query
	 */
	public static int update(UserBEAN currentUser) {
		int rowsUpdated = 0;
		try {
			if (conn == null) {
				bootStrapDatabase();
			}

			String sql = "UPDATE Users SET username=?, password=?, email=? WHERE id=?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, currentUser.getName());
			stmt.setString(2, currentUser.getPassword());
			stmt.setString(3, currentUser.getEmail());
			stmt.setInt(4, currentUser.getId());

			rowsUpdated = stmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("User named '" + currentUser.getName() + "' was updated...");
			}
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return rowsUpdated;
	}

	/**
	 * delete(UserBEAN currentUser)
	 * <p>
	 * This method deletes the current user from the table CRUD - DELETE
	 * 
	 * @param currentUser
	 *            The user to update in the DB
	 * 
	 * @return Integer representing the success/failure state of the query
	 */
	public static int delete(UserBEAN currentUser) {
		int rowsDeleted = 0;
		try {
			if (conn == null) {
				bootStrapDatabase();
			}

			String sql = "DELETE FROM Users WHERE id=?";

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, currentUser.getId());

			rowsDeleted = stmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("User with id :'" + currentUser.getId() + "' was deleted...");
			}
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}

		return rowsDeleted;
	}

	/**
	 * selectUserById(Integer id)
	 * <p>
	 * This method reads the current user from the DB CRUD - READ
	 * 
	 * @param id
	 *            The id of the user to fetch from the database
	 * 
	 * @return UserBEAN The selected user
	 */
	public static UserBEAN selectUserById(int id) {
		UserBEAN u = null;
		try {
			if (conn == null) {
				bootStrapDatabase();
			}

			String sql = "SELECT * FROM Users WHERE id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				int newId = result.getInt("id");
				String name = result.getString("username");
				String pass = result.getString("password");
				String email = result.getString("email");

				u = new UserBEAN();
				u.setId(newId);
				u.setName(name);
				u.setPassword(pass);
				u.setEmail(email);

				String output = "User #%d: %s pass: %s mail: %s was found...";
				System.out.println(String.format(output, newId, name, pass, email));
			}
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return u;
	}

	/**
	 * getAllRecords()
	 * <p>
	 * This method fetches all the users from the DB CRUD - READ
	 * 
	 * @return List<UserBEAN> The selected users
	 */
	public static List<UserBEAN> getAllRecords() throws SQLException {
		List<UserBEAN> list = new ArrayList<UserBEAN>();
		try {
			if (conn == null) {
				bootStrapDatabase();
			}

			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users");
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				UserBEAN u = new UserBEAN();
				u.setId(result.getInt("id"));
				u.setName(result.getString("username"));
				u.setPassword(result.getString("password"));
				u.setEmail(result.getString("email"));
				list.add(u);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	/**
	 * dropDatabase() : void() throws SQLException
	 * <p>
	 * This method drops the created database CRUD - DELETE
	 * 
	 * @return void
	 */
	public static int dropDatabase() throws SQLException {
		String sql = "DROP DATABASE " + DB_NAME + ";";

		stmt = conn.prepareStatement(sql);
		int dataBaseDel = stmt.executeUpdate();
		if (dataBaseDel > 0) {
			System.out.println(DB_NAME + " was dropped...");
		}

		return dataBaseDel;
	}

	/**
	 * disconnect() : void()
	 * <p>
	 * This method safely disconnects from the database
	 * 
	 * @return void
	 */
	public static void disconnect() {
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
