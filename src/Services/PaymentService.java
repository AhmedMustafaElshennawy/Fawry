package Services;

import Models.Customer;

class PaymentService {
    public void processPayment(Customer customer, double amount) {
        customer.deductBalance(amount);
    }
}