package com.simplecrud.handling;

import java.util.List;
import java.sql.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * class SimpleCrud
 * <p>
 * This class is a handler for demonstrating simple CRUD(CREATE, READ, UPDATE,
 * DELETE) functionality.
 */
public class SimpleCrud {

	private static SessionFactory sessionFactory;
	private static Session session;

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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "0000");
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}

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
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement("");
			StringBuilder sql = new StringBuilder();

			// Create/Connect DB//
			sql.append("CREATE DATABASE IF NOT EXISTS CRUD_SAMPLE;");
			stmt.executeUpdate(sql.toString());
			sql.setLength(0);

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud_sample", "root", "0000");
			stmt = conn.prepareStatement("");

			sql.append("use CRUD_SAMPLE;");
			stmt.executeUpdate(sql.toString());
			sql.setLength(0);

			// Create/Connect Table//
			sql.append("CREATE TABLE IF NOT EXISTS users (" + "id INT(64) NOT NULL AUTO_INCREMENT,"
					+ "name VARCHAR(45) NOT NULL," + "password VARCHAR(45) NOT NULL,"
					+ "email VARCHAR(45) NOT NULL,"
					+ "PRIMARY KEY (id)) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8");

			stmt.executeUpdate(sql.toString());
			sql.setLength(0);

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sessionFactory = new Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();
		}
	}

	/**
	 * insert(UserPOJO currentUser) : void()
	 * <p>
	 * This method inserts the current user in the table CRUD - CREATE
	 * 
	 * @param currentUser
	 *            The user to insert to the DB
	 * 
	 * @return Integer representing the success/failure state of the query
	 */
	public static int insert(UserPOJO currentUser) {
		if (sessionFactory == null) {
			bootStrapDatabase();
		} else {
			session = sessionFactory.openSession();
		}
		Transaction tx = null;
		int rowsInserted = 0;
		try {
			tx = session.beginTransaction();
			session.save(currentUser);
			tx.commit();
			rowsInserted = 1;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return rowsInserted;
	}

	/**
	 * update(UserPOJO currentUser) : void()
	 * <p>
	 * This method updates the mail and pass of the current user in the table
	 * CRUD - UPDATE
	 * 
	 * @param currentUser
	 *            The user to update in the DB
	 * 
	 * @return Integer representing the success/failure state of the query
	 */
	public static int update(UserPOJO currentUser) {
		if (sessionFactory == null) {
			bootStrapDatabase();
		} else {
			session = sessionFactory.openSession();
		}

		int rowsUpdated = 0;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			UserPOJO a = session.get(UserPOJO.class, currentUser.getId());

			a.setName(currentUser.getName());
			a.setPassword(currentUser.getPassword());
			a.setEmail(currentUser.getEmail());

			session.update(a);

			tx.commit();
			rowsUpdated = 1;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return rowsUpdated;
	}

	/**
	 * delete(UserPOJO currentUser)
	 * <p>
	 * This method deletes the current user from the table CRUD - DELETE
	 * 
	 * @param currentUser
	 *            The user to update in the DB
	 * 
	 * @return Integer representing the success/failure state of the query
	 */
	public static int delete(UserPOJO currentUser) {
		if (sessionFactory == null) {
			bootStrapDatabase();
		} else {
			session = sessionFactory.openSession();
		}

		int rowsDeleted = 0;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			UserPOJO a = session.get(UserPOJO.class, currentUser.getId());
			session.delete(a);
			tx.commit();
			rowsDeleted = 1;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
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
	 * @return UserPOJO The selected user
	 */
	public static UserPOJO selectUserById(int id) {
		if (sessionFactory == null) {
			bootStrapDatabase();
		} else {
			session = sessionFactory.openSession();
		}

		UserPOJO u = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			u = session.get(UserPOJO.class, id);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return u;
	}

	/**
	 * getAllRecords()
	 * <p>
	 * This method fetches all the users from the DB CRUD - READ
	 * 
	 * @return List<UserPOJO> The selected users
	 */
	public static List<UserPOJO> getAllRecords() {
		if (sessionFactory == null) {
			bootStrapDatabase();
		} else {
			session = sessionFactory.openSession();
		}
		List<UserPOJO> list = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			list = session.createQuery("FROM UserPOJO").list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return list;
	}
}
