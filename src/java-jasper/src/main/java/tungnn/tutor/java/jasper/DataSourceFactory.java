package tungnn.tutor.java.jasper;

import javax.sql.DataSource;
import oracle.jdbc.pool.OracleDataSource;

public class DataSourceFactory {

  private static final String DB_URL = System.getenv("DB_URL");
  private static final String DB_USER = System.getenv("DB_USER");
  private static final String DB_PASS = System.getenv("DB_PASS");

  public static DataSource getOracleDataSource() {
    try {
      OracleDataSource dataSource = new OracleDataSource();
      dataSource.setURL(DB_URL);
      dataSource.setUser(DB_USER);
      dataSource.setPassword(DB_PASS);
      return dataSource;
    } catch (Exception e) {
      System.out.println("Cannot create data source: " + e.getMessage());
      return null;
    }
  }
}
