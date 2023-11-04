package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataBase.MemoryDBConnection;
import entities.Customer;
import entities.Orders;

public class InMemoryOrdersDAO extends OrdersDAO {
    private MemoryDBConnection databaseRef;

    public InMemoryOrdersDAO(MemoryDBConnection databaseRef) {
        super();
        this.databaseRef = databaseRef;
    }

    @Override
    public List<Orders> getOrdersByCustomerId(int customerId) throws SQLException {
        List<Orders> orders = new ArrayList<>();
        Iterator<Orders> iterator = databaseRef.getOrderList().iterator();

        while (iterator.hasNext()) {
            Orders buffer = iterator.next();
            if (buffer.getCustomerId() == customerId) {
                orders.add(buffer);
            }
        }
        return orders;
    }

    @Override
    public Orders getOrderByNumber(int orderNumber) throws SQLException {
        Orders order = null;
        Iterator<Orders> iterator = databaseRef.getOrderList().iterator();

        while (iterator.hasNext()) {
            Orders buffer = iterator.next();
            if (buffer.getNumber() == orderNumber) {
                order = buffer;
            }
        }
        return order;
    }

    @Override
    public void addOrder(Orders order) throws SQLException {
        databaseRef.getOrderList().add(order);
    }

    @Override
    public void updateOrder(Orders order) throws SQLException {
        ArrayList<Orders> orders = databaseRef.getOrderList();

        for (int index = 0; index < orders.size(); index++) {
            if (orders.get(index).getNumber() == order.getNumber()) {
                orders.set(index, order);
                break;
            }
        }
    }

    @Override
    public void deleteOrderByNumber(int orderNumber) throws SQLException {
        ArrayList<Orders> orders = databaseRef.getOrderList();

        for (int index = 0; index < orders.size(); index++) {
            if (orders.get(index).getNumber() == orderNumber) {
                orders.remove(index);
                break;
            }
        }
    }

    @Override
    public void deleteAllOrders() throws SQLException {
        databaseRef.getOrderList().clear();
    }

    public List<Orders> getOrdersByCustomerName(Customer customer) throws SQLException {
        List<Orders> orders = new ArrayList<>();
        Iterator<Orders> iterator = databaseRef.getOrderList().iterator();

        while (iterator.hasNext()) {
            Orders buffer = iterator.next();
            if (buffer.getCustomerId() == customer.getId()) {
                orders.add(buffer);
            }
        }
        return orders;
    }
}
