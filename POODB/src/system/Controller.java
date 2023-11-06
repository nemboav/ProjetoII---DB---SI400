package system;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dao.CustomerDAO;
import dao.DBCustomerDAO;
import dao.DBOrdersDAO;
import dao.InMemoryCustomerDAO;
import dao.InMemoryOrdersDAO;
import dao.OrdersDAO;
import dataBase.MariaDBConnection;
import dataBase.MemoryDBConnection;
import entities.Customer;
import entities.Orders;

public class Controller {
    private CustomerDAO customerDAO = null;
    private OrdersDAO ordersDAO = null;
    private MariaDBConnection myDBConnection = null;
    private MemoryDBConnection memoryDBConnection = null;
    private DataBaseType selectedDataBase = DataBaseType.INVALID;
    private Scanner sc;

    public Controller(DataBaseType selectedDataBase) {
        super();
        this.selectedDataBase = selectedDataBase;
        this.sc = new Scanner(System.in);
    }

    private void openConnection() {
        switch (selectedDataBase) {
            case MEMORY:
                memoryDBConnection = new MemoryDBConnection();
                this.customerDAO = new InMemoryCustomerDAO(memoryDBConnection);
                this.ordersDAO = new InMemoryOrdersDAO(memoryDBConnection);
                break;
            case MARIADB:
                myDBConnection = new MariaDBConnection();
                this.customerDAO = new DBCustomerDAO(myDBConnection.getConnection());
                this.ordersDAO = new DBOrdersDAO(myDBConnection.getConnection());
                break;
            default:
                System.out.println("Database selection not supported.");
                throw new InvalidParameterException("Selector is unspecified: " + selectedDataBase);
        }
    }

    private void closeConnection() {
        if (myDBConnection != null) {
            myDBConnection.close();
        }
        if (memoryDBConnection != null) {
            memoryDBConnection.close();
        }
    }

    void showMainMenu() {
        openConnection();
        System.out.println("Bem-vindo ao Sistema de Controle de Clientes!");

        boolean exit = false;

        while (!exit) {
            System.out.println("Menu: ");
            System.out.println("1. Clientes");
            System.out.println("2. Pedidos");
            System.out.println("3. Relatórios");
            System.out.println("4. Informações");
            System.out.println("5. Sair");
            System.out.println("Escolha uma das opções: ");
           
            String input = sc.nextLine();
            if (input.isEmpty() || !input.matches("\\d+")) {
                System.out.println("Entrada inválida. Por favor, insira um número válido.");
                continue;
            }
            
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    showMenuClients();
                    break;
                case 2:
                    showMenuOrders();
                    break;
                case 3:
                    showMenuReports();
                    break;
                case 4:
                    showMenuInfo();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Opção inválida. Digite novamente.");
            }
        }
        closeConnection();
    }

    private void showMenuClients() {
        boolean first = false;
        while (!first) {
            System.out.println("1. Inserir clientes: ");
            System.out.println("2. Consultar cliente pelo identificador: ");
            System.out.println("3. Consultar cliente pelo nome: ");
            System.out.println("4. Apagar cliente pelo identificador: ");
            System.out.println("5. Voltar");

            System.out.println("Escolha uma das opções: ");
            
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Id do cliente: ");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.println("Nome do cliente: ");
                    String name = sc.nextLine();
                    System.out.println("Cidade do cliente: ");
                    String city = sc.nextLine();
                    System.out.println("Estado do cliente: ");
                    String state = sc.nextLine();

                    try {
                        Customer customer = new Customer(id, name, city, state);
                        customerDAO.addCustomer(customer);
                        System.out.println("Cliente adicionado com sucesso!");
                    } catch (SQLException e) {
                        System.out.println("Erro ao inserir cliente: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Digite o identificador do cliente: ");
                    int customerId = Integer.parseInt(sc.nextLine());

                    try {
                        Customer customer = customerDAO.getCustomerById(customerId);
                        if (customer != null) {
                            System.out.println("Cliente encontrado: " + customer);
                        } else {
                            System.out.println("Cliente não encontrado.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao consultar cliente: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Digite o nome do cliente: ");
                    String clientName = sc.nextLine();

                    try {
                        List<Customer> customers = customerDAO.getAllCustomersOrderedByName();
                        boolean found = false;

                        for (Customer c : customers) {
                            if (c.getName().equals(clientName)) {
                                System.out.println("Cliente encontrado: " + c);
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("Nenhum cliente encontrado com esse nome.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao consultar cliente pelo nome: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Digite o identificador do cliente para apagar: ");
                    int idCliente = Integer.parseInt(sc.nextLine());

                    try {
                        customerDAO.deleteCustomerById(idCliente);
                        System.out.println("Cliente removido com sucesso!");
                    } catch (SQLException e) {
                        System.out.println("Erro ao apagar cliente: " + e.getMessage());
                    }
                    break;
                case 5:
                    first = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void showMenuOrders() {
        boolean second = false;

        while (!second) {
            System.out.println("1. Inserir pedido para um cliente");
            System.out.println("2. Consultar pedido pelo número");
            System.out.println("3. Apagar pedido pelo número");
            System.out.println("4. Voltar");

            System.out.println("Escolha uma das opções: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Número do pedido do cliente: ");
                    int number = Integer.parseInt(sc.nextLine());
                    System.out.println("Descrição do pedido: ");
                    System.out.println("Identificador do cliente: ");
                    int customerId = Integer.parseInt(sc.nextLine());
                    System.out.println("Descrição do pedido: ");
                    String description = sc.nextLine();
                    System.out.println("Preço do pedido: ");
                    BigDecimal price = new BigDecimal(sc.nextLine());

                    try {
                        Orders orders = new Orders(number, customerId, description, price);
                        ordersDAO.addOrder(orders);
                        System.out.println("Pedido inserido com sucesso!");
                    } catch (SQLException e) {
                        System.out.println("Erro ao inserir pedido: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Número do pedido: ");
                    int orderNumber1 = Integer.parseInt(sc.nextLine());

                    try {
                        Orders order = ordersDAO.getOrderByNumber(orderNumber1);
                        if (order != null) {
                            System.out.println("Pedido encontrado: " + order);
                        } else {
                            System.out.println("Pedido não encontrado.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao consultar pedido: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Número do pedido para apagar: ");
                    int orderNumber2 = Integer.parseInt(sc.nextLine());

                    try {
                        ordersDAO.deleteOrderByNumber(orderNumber2);
                        System.out.println("Pedido removido com sucesso!");
                    } catch (SQLException e) {
                        System.out.println("Erro ao apagar pedido: " + e.getMessage());
                    }
                    break;
                case 4:
                    second = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void showMenuReports() {

        boolean third = false;

        while (!third) {
            System.out.println("1. Clientes ordenados por identificador");
            System.out.println("2. Clientes ordenados por nome");
            System.out.println("3. Pedidos ordenados por número");
            System.out.println("4. Pedidos dos clientes ordenados por nome");
            System.out.println("5. Voltar");

            System.out.println("Escolha uma das opções: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Id do cliente: ");
                    int customerId = sc.nextInt();
                    try {
                        Customer customer = customerDAO.getCustomerById(customerId);
                        if (customer != null) {
                            System.out.println("Cliente encontrado: " + customer);
                        } else {
                            System.out.println("Cliente não encontrado.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao buscar cliente: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        List<Customer> customersByName = customerDAO.getAllCustomersOrderedByName();
                        if (customersByName != null) {
                            customersByName.forEach(System.out::println);
                        } else {
                            System.out.println("Não há clientes.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao buscar clientes: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Número do pedido do cliente: ");
                    int orderNumber = Integer.parseInt(sc.nextLine());

                    try {
                        Orders order = ordersDAO.getOrderByNumber(orderNumber);
                        if (order != null) {
                            System.out.println("Pedido encontrado: " + order);
                        } else {
                            System.out.println("Pedido não encontrado.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao buscar pedidos: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        List<Customer> customersByName = customerDAO.getAllCustomersOrderedByName();

                        for (Customer customer : customersByName) {
                            System.out.println("Cliente: " + customer.getName());

                            List<Orders> ordersForCustomer = ordersDAO.getOrdersByCustomerId(customer.getId());
                            if (ordersForCustomer != null && !ordersForCustomer.isEmpty()) {
                                for (Orders order : ordersForCustomer) {
                                    System.out.println("Pedido: " + order.getDescription() + " - Preço: " + order.getPrice());
                                }
                            } else {
                                System.out.println("Nenhum pedido encontrado para este cliente.");
                            }
                            System.out.println("-----");
                        }
                    } catch (SQLException e) {
                        System.out.println("Erro ao buscar clientes ou pedidos: " + e.getMessage());
                    }
                    break;

                case 5:
                    third = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void showMenuInfo() {
        boolean fourth = false;

        while (!fourth) {
            System.out.println("Menu Informações: ");
            System.out.println("1. Ajuda");
            System.out.println("2. Sobre");
            System.out.println("3. Voltar");

            System.out.println("Escolha uma das opções: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Bem-vindo ao sistema de controle de clientes e pedidos!");
                    System.out.println("Este sistema oferece funcionalidades para gerenciar clientes, pedidos e relatórios.");
                    break;
                case 2:
                    System.out.println("Sistema de Controle de Clientes e Pedidos");
                    System.out.println("Versão 2.0");
                    System.out.println("Desenvolvido por Natalia Emboava - 2023");

                    break;
                case 3:
                    fourth = true;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
