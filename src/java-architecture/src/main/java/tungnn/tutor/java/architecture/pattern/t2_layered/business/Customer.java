package tungnn.tutor.java.architecture.pattern.t2_layered.business;

import tungnn.tutor.java.architecture.pattern.t2_layered.persistence.CustomerDAO;

public class Customer {
    private CustomerDAO customerDAO = new CustomerDAO();

    public void register(String name) {
        System.out.println("[Business] Đang xử lý đăng ký cho: " + name);
        // Có thể thêm logic kiểm tra dữ liệu ở đây
        customerDAO.save(name);
    }
}