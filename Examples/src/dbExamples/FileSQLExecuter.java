package dbExamples;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FileSQLExecuter {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in, "UTF-8")) {
			try (Connection connection = getConnection();
					Statement statement = connection.createStatement()) {
				while (true) {
					Logger.getGlobal().info("Enter file 'name.sql'"
							+ "with sql statements or supply statement"
							+ " or EXIT.");
					String input = in.nextLine();
					if (input == null || input.trim().equalsIgnoreCase("EXIT")) {
						return;
					}
					if (input.endsWith(".sql")) {
						List<String> statements = loadStatements(input);
						Logger.getGlobal().info(executeQuery(statement,
								statements.toArray(
										new String[statements.size()])));
					} else {
						Logger.getGlobal().info(executeQuery(statement, input));
					}
				}
			} 
		} catch (SQLException e) {
			e.forEach((Throwable ex) -> ex.printStackTrace());
		} catch (IOException ioe) {
			Logger.getGlobal().warning("Error when loading file.");
		}
		
	}
	
	private static List<String> loadStatements(String from) throws IOException {
		return Files.lines(Paths.get(from)).collect(Collectors.toList());
	}
	
	private static String executeQuery(final Statement statement, String... query) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < query.length; i++) {
			String s = query[i];
			if (s.endsWith(";")) {
				s = s.substring(0, s.length() - 1);
			}
			try {
				boolean isResult = statement.execute(s);
				if (isResult) {
					try (ResultSet rs = statement.getResultSet()) {
						sb.append(showResultSet(rs));
					}
				} else {
					int updateCount = statement.getUpdateCount();
					sb.append(updateCount);
					sb.append(" rows updated.\n");
				}
			} catch (SQLException e) {
				e.forEach((Throwable ex) -> ex.printStackTrace());
			}
		}
		return sb.toString();
	}
	
	private static String showResultSet(ResultSet set) throws SQLException {
		ResultSetMetaData metaData = set.getMetaData();
		final int columnCount = metaData.getColumnCount();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= columnCount; i++) {
			if (i > 1) {
				sb.append(", ");
			}
			sb.append(metaData.getColumnLabel(i));
		}
		sb.append('\n');
		
		while (set.next()) {
			for (int i = 1; i <= columnCount; i++) {
				if (i > 1) {
					sb.append(", ");
				}
				sb.append(set.getString(i));
			}
			sb.append('\n');
		}
		return sb.append('\n').toString();
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
