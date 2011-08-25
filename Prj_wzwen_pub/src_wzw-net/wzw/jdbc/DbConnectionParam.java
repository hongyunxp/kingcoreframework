
package wzw.jdbc;


public interface DbConnectionParam {
 /*String DB_URL = "jdbc:oracle:thin:@10.136.18.211:1521:wzora";
  String DB_USER = "wzwh";
  String DB_PASSWORD = "wzwh2003";
   String DB_URL = "jdbc:oracle:thin:@10.136.18.237:1521:myora";
  String DB_USER = "tj";
  String DB_PASSWORD = "tj";*/
  String DB_URL = "jdbc:oracle:thin:@192.168.32.29:1521:ora9";//"jdbc:oracle:thin:@10.136.18.211:1521:ora9";
  String DB_USER = "sqpt";
  String DB_PASSWORD = "sqpt2004";
  String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
  int MAX_CONN = 46;
  int MIN_CONN = 10;
  String CHECKQUERY = "select 1 from tab";
}