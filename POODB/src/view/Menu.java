package menus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import DAO.CustomerDAO;
import DAO.OrdersDAO;
import DAO.DBCustomerDAO;
import DAO.DBOrdersDAO;
import entities.Customer;
import entities.Orders;
import menus.Controller;
import dataBase.*;
import menus.*;

public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private final CustomerDAO CustomerDAO;
    private final OrdersDAO OrdersDAO;
    private final Connection MariaDBConnection;
    private final Controller controller;
	 
    public Menu() {
        this.MariaDBConnection = new Connection(); 
        this.CustomerDAO = new DBCustomerDAO(MariaDBConnection);
        this.OrdersDAO = new DBOrdersDAO(MariaDBConnection);
        this.controller = new Controller(DataBaseType.MARIADB);
    }
    
    public Menu(Controller controller) {
        this.controller = controller;
        this.MariaDBConnection = new Connection();
        this.CustomerDAO = new DBCustomerDAO(MariaDBConnection);
        this.OrdersDAO = new DBOrdersDAO(MariaDBConnection);
    }
	 
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.showMainMenu();
    }
    
    private void showMainMenu() {
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
            int choice = sc.nextInt();

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
            	break;
            default:
            	System.out.println("Opcáo inválida. Digite novamente.");
            
            }
		}
	}
		
	private void showMenuClients() {
		boolean first = false;			
			while(!first) {
				System.out.println("1. Inserir clientes: ");
				System.out.println("2. Consultar cliente pelo identificador: ");
				System.out.println("3. Consultar cliente pelo nome: ");
				System.out.println("4. Apagar cliente pelo identificador: ");
				System.out.println("5. Voltar");
				
				System.out.println("Escolha uma das opções: ");
				int choice = Integer.parseInt(sc.nextLine());
				
				switch(choice) {
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
		                     CustomerDAO.addCustomer(customer);
		                     System.out.println("Cliente adicionado com sucesso!");
						} catch (SQLException e) {
							System.out.println("Erro ao inserir cliente: " + e.getMessage());
						}
						break;
					case 2:
		                System.out.println("Digite o identificador do cliente: ");
		                int customerId = Integer.parseInt(sc.nextLine());

		                try {
		                    Customer customer = CustomerDAO.getCustomerById(customerId);
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
		                    List<Customer> customers = CustomerDAO.getAllCustomersOrderedByName();
		                    if (customers != null && !customers.isEmpty()) {
		                        for (Customer c : customers) {
		                            System.out.println("Cliente encontrado: " + c);
		                        }
		                    } else {
		                        System.out.println("Nenhum cliente encontrado com esse nome.");
		                    }
		                } catch (SQLException e) {
		                    System.out.println("Erro ao consultar cliente pelo nome: " + e.getMessage());
		                }
						break;
					case 4:
						 // Apagar cliente pelo identificador
		                System.out.println("Digite o identificador do cliente para apagar: ");
		                int idCliente = Integer.parseInt(sc.nextLine());

		                try {
		                    CustomerDAO.deleteCustomerById(idCliente);
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
		                    OrdersDAO.addOrder(orders);
		                    System.out.println("Pedido inserido com sucesso!");
		                } catch (SQLException e) {
		                    System.out.println("Erro ao inserir pedido: " + e.getMessage());
		                }
		                break;
		            case 2:
		            	
		                System.out.println("Número do pedido: ");
		                int orderNumber1 = Integer.parseInt(sc.nextLine());

		                try {
		                    Orders order = OrdersDAO.getOrderByNumber(orderNumber1);
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
		                    OrdersDAO.deleteOrderByNumber(orderNumber2);
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
			                Customer customer = CustomerDAO.getCustomerById(customerId); 
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
		                    List<Customer> customersByName = CustomerDAO.getAllCustomersOrderedByName();
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
		                    Orders order = OrdersDAO.getOrderByNumber(orderNumber);
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
		                    List<Customer> customersByName = CustomerDAO.getAllCustomersOrderedByName(); 

		                    for (Customer customer : customersByName) {
		                        System.out.println("Cliente: " + customer.getName()); 

		                        List<Orders> ordersForCustomer = OrdersDAO.getOrdersByCustomerId(customer.getId()); 
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
			                System.out.println("Sistema de Controle de Clientes e Pedidos v1.0");
			                System.out.println("Desenvolvido por [Diego Rapacci, Marx Maciel, Natalia Emboava, Ulisses, Victor Bergamasco] - 2023"); 
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