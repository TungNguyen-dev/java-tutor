package tungnn.tutor.java.architecture_pattern.t2_layered.persistence;

import tungnn.tutor.java.architecture_pattern.t2_layered.database.JdbcAgent;

public class CustomerDAO {
    private JdbcAgent dbAgent = new JdbcAgent();

    public void save(String name) {
        dbAgent.executeQuery("INSERT INTO customers VALUES ('" + name + "')");
        System.out.println("[Persistence] Đã lưu thông tin khách hàng vào DB.");
    }
}