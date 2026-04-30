package tungnn.tutor.java.architecture.pattern.t2_layered.presentation;

import tungnn.tutor.java.architecture.pattern.t2_layered.business.Customer;

public class CustomerDelegate {
    private Customer customerService = new Customer();

    public void handleRegistration(String name) {
        customerService.register(name);
    }
}