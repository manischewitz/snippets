package dbExamples;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

//Run as java -classpath .:driverJAR dbExamples.TestDB
public class TestDB {

	public static void main(String[] args) throws IOException {
		try {
			test();
		} catch (SQLException sqle) {
			sqle.forEach(t -> t.printStackTrace());
			
		}

	}
	
	private static void test() throws SQLException, IOException {
		try(Connection connection = getConnection();
				Statement statement = connection.createStatement()) {
			statement.executeUpdate("CREATE TABLE hello (value CHAR(64));");
			statement.executeUpdate("INSERT INTO hello VALUES ('word!');");
			
			try (ResultSet result = statement.executeQuery("SELECT * FROM hello;")) {
				if (result.next()) {
					Logger.getGlobal().info(result.getString(1));
				}
			}
			statement.executeUpdate("DROP TABLE hello;");
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
