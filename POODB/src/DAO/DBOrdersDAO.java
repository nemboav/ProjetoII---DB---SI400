package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Customer;
import entities.Orders;

public class DBOrdersDAO extends OrdersDAO {
    private Connection connection;

    public DBOrdersDAO(Connection connection) {
        super();
        this.connection = connection;
    }

    @Override
    public List<Orders> getOrdersByCustomerId(int customerId) throws SQLException {
        List<Orders> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE customerId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Orders order = new Orders();
                order.setNumber(resultSet.getInt("number"));
                order.setCustomerId(resultSet.getInt("customerId"));
                order.setDescription(resultSet.getString("description"));
                order.setPrice(resultSet.getBigDecimal("price"));
                orders.add(order);
            }
        }

        return orders;
    }

    @Override
    public List<Orders> getOrdersByCustomerName(Customer customer) throws SQLException {
        List<Orders> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Orders order = new Orders();
                customer.setName(resultSet.getString("name"));
                order.setNumber(resultSet.getInt("number"));
                order.setCustomerId(resultSet.getInt("customerId"));
                order.setDescription(resultSet.getString("description"));
                order.setPrice(resultSet.getBigDecimal("price"));
                orders.add(order);
            }
        }

        return orders;
    }

    @Override
    public Orders getOrderByNumber(int orderNumber) throws SQLException {
        String query = "SELECT * FROM Orders WHERE number = ?";
        Orders order = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                order = new Orders();
                order.setNumber(resultSet.getInt("number"));
                order.setCustomerId(resultSet.getInt("customerId"));
                order.setDescription(resultSet.getString("description"));
                order.setPrice(resultSet.getBigDecimal("price"));
            }
        }

        return order;
    }

    @Override
    public void addOrder(Orders order) throws SQLException {
        String query = "INSERT INTO Orders (number, customerId, description, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, order.getNumber());
            preparedStatement.setInt(2, order.getCustomerId());
            preparedStatement.setString(3, order.getDescription());
            preparedStatement.setBigDecimal(4, order.getPrice());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateOrder(Orders order) throws SQLException {
        String query = "UPDATE Orders SET customerId = ?, description = ?, price = ? WHERE number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, order.getCustomerId());
            preparedStatement.setString(2, order.getDescription());
            preparedStatement.setBigDecimal(3, order.getPrice());
            preparedStatement.setInt(4, order.getNumber());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteOrderByNumber(int orderNumber) throws SQLException {
        String query = "DELETE FROM Orders WHERE number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderNumber);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteAllOrders() throws SQLException {
        String query = "DELETE FROM Orders";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }
}
