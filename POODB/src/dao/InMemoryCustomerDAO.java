package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataBase.MemoryDBConnection;
import entities.Customer;

public class InMemoryCustomerDAO extends CustomerDAO {
    private MemoryDBConnection databaseRef;

    public InMemoryCustomerDAO(MemoryDBConnection databaseRef) {
        super();
        this.databaseRef = databaseRef;
    }

    @Override
    public List<Customer> getAllCustomersOrderedByName() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        customers.addAll(databaseRef.getCustomerList());
        return customers;
    }

    @Override
    public Customer getCustomerById(int customerId) throws SQLException {
        Customer customer = null;
        Iterator<Customer> iterator = databaseRef.getCustomerList().iterator();

        while (iterator.hasNext()) {
            Customer buffer = iterator.next();
            if (buffer.getId() == customerId) {
                customer = buffer;
            }
        }
        return customer;
    }

    @Override
    public void addCustomer(Customer customer) throws SQLException {
        databaseRef.getCustomerList().add(customer);
    }

    @Override
    public void updateCustomer(Customer customer) throws SQLException {
        List<Customer> customers = databaseRef.getCustomerList();

        for (int index = 0; index < customers.size(); index++) {
            if (customers.get(index).getId() == customer.getId()) {
                customers.set(index, customer);
                break;
            }
        }
    }

    @Override
    public void deleteCustomerById(int customerId) throws SQLException {
        List<Customer> customers = databaseRef.getCustomerList();

        for (int index = 0; index < customers.size(); index++) {
            if (customers.get(index).getId() == customerId) {
                customers.remove(index);
                break;
            }
        }
    }

    @Override
    public void deleteAllCustomers() throws SQLException {
        databaseRef.getCustomerList().clear();
    }

    @Override
    public Customer getCustomerByName(String name) throws SQLException {
        Customer foundCustomer = null;
        Iterator<Customer> iterator = databaseRef.getCustomerList().iterator();

        while (iterator.hasNext()) {
            Customer buffer = iterator.next();
            if (buffer.getName().equals(name)) {
                foundCustomer = buffer;
                break;
            }
        }
        return foundCustomer;
    }

    @Override
    public List<Customer> getAllCustomersOrderedById() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        customers.addAll(databaseRef.getCustomerList());
        return customers;
    }
}
