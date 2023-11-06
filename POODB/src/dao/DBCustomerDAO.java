package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.Customer;

public class DBCustomerDAO extends CustomerDAO {
    private Connection connection;

    public DBCustomerDAO(Connection connection) {
        super();
        this.connection = connection;
    }
    
    public Connection getConnection() {
        return connection;
    }

    @Override
    public List<Customer> getAllCustomersOrderedByName() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer ORDER BY name";

        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                try (PreparedStatement preparedStatement = getConnection().prepareStatement(query);
                     ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Customer customer = new Customer();
                        customer.setId(resultSet.getInt("id"));
                        customer.setName(resultSet.getString("name"));
                        customer.setCity(resultSet.getString("city"));
                        customer.setState(resultSet.getString("state"));
                        customers.add(customer);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Trate a exceção apropriadamente, pode usar um logger ou relançar a exceção
        }

        return customers;
    }


    @Override
    public Customer getCustomerById(int customerId) throws SQLException {
        String query = "SELECT * FROM Customer WHERE id = ?";
        Customer customer = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setCity(resultSet.getString("city"));
                customer.setState(resultSet.getString("state"));
            }
        }

        return customer;
    }

    @Override
    public void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO Customer (name, city, state) VALUES (?, ?, ?)";

	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, customer.getName());
	            preparedStatement.setString(2, customer.getCity());
	            preparedStatement.setString(3, customer.getState());
	
	            preparedStatement.executeUpdate();
	        }
        }
    

    @Override
    public void updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE Customer SET name = ?, city = ?, state = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getCity());
            preparedStatement.setString(3, customer.getState());
            preparedStatement.setInt(4, customer.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteCustomerById(int customerId) throws SQLException {
        String query = "DELETE FROM Customer WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteAllCustomers() throws SQLException {
        String query = "DELETE FROM Customer";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Customer> getAllCustomersOrderedById() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customer ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setCity(resultSet.getString("city"));
                customer.setState(resultSet.getString("state"));
                customers.add(customer);
            }
        }

        return customers;
    }

    @Override
    public Customer getCustomerByName(String name) throws SQLException {
        String query = "SELECT * FROM Customer WHERE name = ?";
        Customer foundCustomer = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                foundCustomer = new Customer();
                foundCustomer.setId(resultSet.getInt("id"));
                foundCustomer.setName(resultSet.getString("name"));
                foundCustomer.setCity(resultSet.getString("city"));
                foundCustomer.setState(resultSet.getString("state"));
            }
        }

        return foundCustomer;
    }
}
