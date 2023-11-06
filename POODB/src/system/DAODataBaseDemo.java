package system;

public class DAODataBaseDemo {
    public static void main(String[] args) {
        System.out.println("Database demonstration running now...");

        try {
        	(new Controller(DataBaseType.MEMORY)).showMainMenu();
        } catch (Exception e) {
            System.out.println("Exception launched. Program aborted.");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Database demonstration stopping now...");
        System.exit(0);
    }
}
