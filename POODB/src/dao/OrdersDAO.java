package dao;

import java.sql.SQLException;
import java.util.List;

import entities.Customer;
import entities.Orders;

public abstract class OrdersDAO {

    abstract public List<Orders> getOrdersByCustomerId(int customerId) throws SQLException;

    abstract public List<Orders> getOrdersByCustomerName(Customer customer) throws SQLException;

    abstract public Orders getOrderByNumber(int orderNumber) throws SQLException;

    abstract public void addOrder(Orders order) throws SQLException;

    abstract public void updateOrder(Orders order) throws SQLException;

    abstract public void deleteOrderByNumber(int orderNumber) throws SQLException;

    abstract public void deleteAllOrders() throws SQLException;
}
