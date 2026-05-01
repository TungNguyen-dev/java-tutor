package tungnn.tutor.java.pattern.architecture_pattern.t2_layered.database;

public class JdbcAgent {
    public void executeQuery(String sql) {
        System.out.println("[Database] Đang thực thi: " + sql);
    }
}