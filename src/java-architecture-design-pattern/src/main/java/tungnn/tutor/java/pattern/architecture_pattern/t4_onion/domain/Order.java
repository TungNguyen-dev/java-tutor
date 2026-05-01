package tungnn.tutor.java.pattern.architecture_pattern.t4_onion.domain;

public class Order {
    private String id;
    private double amount;

    public Order(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public void validate() {
        if (amount <= 0) throw new RuntimeException("Số tiền không hợp lệ");
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
}
