package dao;

import java.sql.SQLException;
import java.util.List;
import entities.Customer;

public abstract class CustomerDAO {
    abstract public List<Customer> getAllCustomersOrderedByName() throws SQLException;

    abstract public List<Customer> getAllCustomersOrderedById() throws SQLException;

    abstract public Customer getCustomerById(int customerId) throws SQLException;

    abstract public void addCustomer(Customer customer) throws SQLException;

    abstract public void updateCustomer(Customer customer) throws SQLException;

    abstract public void deleteCustomerById(int customerId) throws SQLException;

    abstract public void deleteAllCustomers() throws SQLException;

    abstract public Customer getCustomerByName(String name) throws SQLException;
}