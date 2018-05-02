package dbExamples;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class PreparedStatements {
	
	private static final String allQuery = "SELECT Books.Price, Books.Title FROM Books";
	private static final String authorPublisherQuery = "SELECT Books.Price, Books.Title FROM Books, BooksAuthors, Authors, Publishers WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN AND Books.Publisher_Id = Publishers.Publisher_Id AND Authors.Name = ?  AND Publishers.Name = ?";
	private static final String authorQuery = "SELECT Books.Price, Books.Title FROM Books, BooksAuthors, Authors WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN AND Authors.Name = ?";
	private static final String publisherQuery = "SELECT Books.Price, Books.Title FROM Books, Publishers WHERE Books.Publisher_Id = Publishers.Publisher_Id AND Publishers.Name = ?";
	private static final String priceUpdate = "UPDATE Books SET Price = Price + ?  WHERE Books.Publisher_Id = (SELECT Publisher_Id FROM Publishers WHERE Name = ?)";
	
	private static Scanner in;
	private static List<String> authors = new ArrayList<>();
	private static List<String> publishers = new ArrayList<>();
	
	public static void main(String[] args) {
		try(Connection connection = getConnection()) {
			in = new Scanner(System.in);
			authors.add("Any");
			publishers.add("Any");
			
			try (Statement s = connection.createStatement()) {
				try (ResultSet rs = s.executeQuery("SELECT name FROM Authors;")) {
					while (rs.next()) {
						authors.add(rs.getString(1));
					}
				}
				try (ResultSet rs = s.executeQuery("SELECT name FROM Publishers;")) {
					while (rs.next()) {
						publishers.add(rs.getString(1));
					}
				}
			}
			
			boolean done = false;
			while (!done) {
				System.out.print("Q)uery C)hange prices E)xit: ");
				String input = in.next().toUpperCase();
				if (input.equals("Q")) {
					executeQuery(connection);
				} else if (input.equals("C")) {
					changePrices(connection);
				} else {
					done = true;
				}
			}
		} catch (SQLException e) {
			e.forEach((Throwable exception) -> 
			System.out.println(exception.getMessage()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	
	private static void changePrices(Connection connection) throws SQLException {
		String publisher = select("Publishers:", publishers.subList(1, publishers.size()));
		System.out.print("Change by: ");
		double change = in.nextDouble();
		PreparedStatement statement = connection.prepareStatement(priceUpdate);
		statement.setDouble(1, change);
		statement.setString(2, publisher);
		int affected = statement.executeUpdate();
		System.out.print(affected + " was updated.");
		
	}

	private static void executeQuery(Connection connection) throws SQLException {
		String author = select("Authors:", authors);
		String publisher = select("Publishers:", publishers);
		PreparedStatement statement;
		if (!author.equals("Any") && !publisher.equals("Any")) {
			statement = connection.prepareStatement(authorPublisherQuery);
			statement.setString(1, author);
			statement.setString(2, publisher);
		} else if (!author.equals("Any") && publisher.equals("Any")) {
			statement = connection.prepareStatement(authorQuery);
			statement.setString(1, author);
		} else if (author.equals("Any") && !publisher.equals("Any")) {
			statement = connection.prepareStatement(publisherQuery);
			statement.setString(1, publisher);
		} else {
			statement = connection.prepareStatement(allQuery);
		}
		
		try (ResultSet rs = statement.executeQuery()) {
			while (rs.next()) {
				System.out.println(rs.getString(1) + ", " + rs.getString(2));
			}
		}
	}
	
	/**
	* Asks the user to select a string.
	* @param prompt the prompt to display
	* @param options the options from which the user can choose
	* @return the option that the user chose
	*/
	private static String select(String prompt, List<String> options) {
		while (true) {
			System.out.println(prompt);
			for (int i = 0; i < options.size(); i++) {
				System.out.printf("%2d) %s%n", i + 1, options.get(i));
			}
			int selected = in.nextInt();
			if (selected > 0 && selected <= options.size()) {
				return options.get(selected - 1);
			}
		}
	}

	private static Connection getConnection() throws IOException, SQLException {
		Properties props = new Properties();
		try (Reader in = Files.newBufferedReader(Paths.get("db.properties"), StandardCharsets.UTF_8)) {
			props.load(in);
		}
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null) {
			System.setProperty("jdbc.drivers", drivers);
		}
		String url = props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");
		return DriverManager.getConnection(url, username, password);
	}
}
