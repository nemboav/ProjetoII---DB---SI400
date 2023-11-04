package menus;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import DAO.CustomerDAO;
import DAO.OrdersDAO;
import DAO.DBCustomerDAO;
import DAO.InMemoryCustomerDAO;
import DAO.DBOrdersDAO;
import DAO.InMemoryOrdersDAO;
import entities.Customer;
import entities.Orders;
import dataBase.MemoryDBConnection;
import dataBase.MariaDBConnection;

public class Controller
   {
   private CustomerDAO customerDAO        = null;
   private OrdersDAO    ordersDAO          = null;
   private MariaDBConnection   myDBConnection     = null;
   private MemoryDBConnection  memoryDBConnection = null;
   private DataBaseType        selectedDataBase   = DataBaseType.INVALID;

   public Controller(DataBaseType selectedDataBase) {
       super();
       this.selectedDataBase = selectedDataBase;
   }

   private void openConnection() {
       switch (selectedDataBase) {
           case MEMORY: {
               memoryDBConnection = new MemoryDBConnection();
               this.customerDAO = new InMemoryCustomerDAO(memoryDBConnection);
               this.ordersDAO = new InMemoryOrdersDAO(memoryDBConnection);
               break;
           }
           case MARIADB: {
               myDBConnection = new MariaDBConnection();
               this.customerDAO = new DBCustomerDAO(myDBConnection.getConnection());
               this.ordersDAO = new DBOrdersDAO(myDBConnection.getConnection());
               break;
           }
           default: {
               System.out.println("Database selection not supported.");
               throw new InvalidParameterException("Selector is unspecified: " + selectedDataBase);
           }
       }
   }
   
   private void closeConnection()
      {
      if (myDBConnection != null)
         {
         myDBConnection.close();
         }
      if (memoryDBConnection != null)
         {
         memoryDBConnection.close();
         }
      }

   public void start()
      {
	   openConnection();
	   insertData();
	   requestData();
	   updateData();
	   deleteData();
	   deleteAllData();
	   requestData();
	   closeConnection();
      }

   private void insertData()
      {
      System.out.println("Create 4 random customers");
      try
         {
         for (int i = 1; i <= 4; i++)
            {
            Customer customer = new Customer();
            customer.setId(i);
            customer.setName("Customer " + i);
            customer.setCity("City " + i);
            customer.setState("State " + i);
            customerDAO.addCustomer(customer);

           
            for (int j = 1; j <= 2; j++)
               {
               Orders order = new Orders();
               order.setNumber((i - 1) * 2 + j); 
               order.setCustomerId(i);
               order.setDescription("Order " + j + " for Customer " + i);
               order.setPrice(new BigDecimal(new Random().nextDouble() * 100.0)); 
               ordersDAO.addOrder(order);
               }
            }
         }
      catch (SQLException e)
         {
        
         e.printStackTrace();
         }

      System.out.println("Random customers and orders created successfully!");
      }

   private void requestData()
      {

      System.out.println("Requesting all customers");
      try
         {
         List<Customer> customers = customerDAO.getAllCustomersOrderedByName();
         for (Customer customer : customers)
            {
            System.out.println(customer.getName());
            }
         }
      catch (SQLException e)
         {
         System.err.println("Error retrieving customers: " + e.getMessage());
         }

      System.out.println("Requesting single customer");
      try
         {
         int customerId = 1;
         Customer customer = customerDAO.getCustomerById(customerId);

         if (customer != null)
            {
            System.out.println("Customer Name: " + customer.getName());
            System.out.println("City: " + customer.getCity());
            System.out.println("State: " + customer.getState());
            }
         else
            {
            System.out.println("Customer not found.");
            }
         }
      catch (SQLException e)
         {
         System.err.println("Error retrieving customer: " + e.getMessage());
         }

      System.out.println("Requesting all orders from a customer");
      try
         {
         int customerId = 1; // Replace with the desired customer
                             // ID
         List<Orders> customerOrders = ordersDAO.getOrdersByCustomerId(customerId);

         for (Orders order : customerOrders)
            {
            System.out.println("Order Number: " + order.getNumber());
            System.out.println("Description: " + order.getDescription());
            System.out.println("Price: " + order.getPrice());
            System.out.println();
            }
         }
      catch (SQLException e)
         {
         System.err.println("Error retrieving customer orders: " + e.getMessage());
         }

      System.out.println("Requesting a single order");
      try
         {
         int orderNumber = 1; // Replace with the desired order number
         Orders order = ordersDAO.getOrderByNumber(orderNumber);

         if (order != null)
            {
            System.out.println("Order Number: " + order.getNumber());
            System.out.println("Customer ID: " + order.getCustomerId());
            System.out.println("Description: " + order.getDescription());
            System.out.println("Price: " + order.getPrice());
            }
         else
            {
            System.out.println("Order not found.");
            }
         }
      catch (SQLException e)
         {
         System.err.println("Error retrieving order: " + e.getMessage());
         }
      }

   private void updateData()
      {
      // Single Customer
      try
         {
         int customerId = 1; // Replace with the desired customer ID
         Customer customer = customerDAO.getCustomerById(customerId);

         if (customer != null)
            {
            System.out.println("Customer Name: " + customer.getName());
            System.out.println("City: " + customer.getCity());
            System.out.println("State: " + customer.getState());

            customer.setCity("Limeira");
            customer.setState("SP");
            customerDAO.updateCustomer(customer);
            }
         else
            {
            System.out.println("Customer not found.");
            }
         }
      catch (SQLException e)
         {
         System.err.println("Error retrieving customer: " + e.getMessage());
         }

      }

   private void deleteData() {
	    try {
	        int orderNumber = ;
	        Orders order = ordersDAO.getOrderByNumber(orderNumber);

	        if (order != null) {
	            System.out.println("Order Number: " + order.getNumber());
	            System.out.println("Customer ID: " + order.getCustomerId());
	            System.out.println("Description: " + order.getDescription());
	            System.out.println("Price: " + order.getPrice());

	            ordersDAO.deleteOrderByNumber(order.getNumber());
	        } else {
	            System.out.println("Order not found.");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error retrieving order: " + e.getMessage());
	    }
	}

   private void deleteAllData() {
	    System.out.println("Deleting all data");

	    try {
	        ordersDAO.deleteAllOrders();
	        customerDAO.deleteAllCustomers();
	    } catch (SQLException e) {
	        System.err.println("Error deleting all data: " + e.getMessage());
	    }
	}
   }

