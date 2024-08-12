import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseConnection {
  private static final String URL;

  // returning Connection
  // without connection pooling
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL);

  }

}
