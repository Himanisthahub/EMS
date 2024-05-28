import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagementSystem {
    private final List<Employee> employees;
    private final List<User> users;
    private final Scanner scanner;
    private User loggedInUser;
    private int employeeIdCounter;

    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
        users = new ArrayList<>();
        scanner = new Scanner(System.in);
        employeeIdCounter = 1;  // Initialize the employee ID counter

        // Adding a default admin user
        users.add(new User("rudrathapa", "rudra123", "admin"));
    }

    // Entry point
    public static void main(String[] args) {
        EmployeeManagementSystem system = new EmployeeManagementSystem();
        system.run();
    }

    public void run() {
        while (true) {
            if (loggedInUser == null) {
                login();
            } else {
                showMenu();
            }
        }
    }

    private void login() {
        System.out.println("Login:");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (user != null) {
            loggedInUser = user;
            System.out.println("Login successful. Welcome " + user.getUsername());
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void showMenu() {
        System.out.println("\nEmployee Management System");
        System.out.println("1. Add Employee");
        System.out.println("2. Delete Employee");
        System.out.println("3. Update Employee");
        System.out.println("4. Search Employee");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> addEmployee();
            case 2 -> deleteEmployee();
            case 3 -> updateEmployee();
            case 4 -> searchEmployee();
            case 5 -> logout();
            default -> System.out.println("Invalid choice. Please enter a number between 1 and 5.");
        }
    }

    private void logout() {
        loggedInUser = null;
        System.out.println("Logged out successfully.");
    }

    private void addEmployee() {
        if (!loggedInUser.getRole().equals("admin")) {
            System.out.println("Access denied. Only admins can add employees.");
            return;
        }

        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter employee position: ");
        String position = scanner.nextLine();
        System.out.print("Enter employee salary: ");
        double salary = getDoubleInput();

        Employee employee = new Employee(name, String.valueOf(employeeIdCounter++), position, salary);  // Auto increment ID
        employees.add(employee);
        System.out.println("Employee added successfully with ID: " + employee.getId());
    }

    private void deleteEmployee() {
        if (!loggedInUser.getRole().equals("admin")) {
            System.out.println("Access denied. Only admins can delete employees.");
            return;
        }

        System.out.print("Enter employee ID to delete: ");
        String id = scanner.nextLine();

        Employee employee = employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            employees.remove(employee);
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    private void updateEmployee() {
        if (!loggedInUser.getRole().equals("admin")) {
            System.out.println("Access denied. Only admins can update employees.");
            return;
        }

        System.out.print("Enter employee ID to update: ");
        String id = scanner.nextLine();

        Employee employee = employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (employee != null) {
            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.isBlank()) {
                employee.setName(name);
            }

            System.out.print("Enter new position (leave blank to keep current): ");
            String position = scanner.nextLine();
            if (!position.isBlank()) {
                employee.setPosition(position);
            }

            System.out.print("Enter new salary (leave blank to keep current): ");
            String salaryStr = scanner.nextLine();
            if (!salaryStr.isBlank()) {
                double salary = Double.parseDouble(salaryStr);
                employee.setSalary(salary);
            }

            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    private void searchEmployee() {
        System.out.println("Search Employee:");
        System.out.println("1. By Name");
        System.out.println("2. By ID");
        System.out.println("3. By Position");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> {
                System.out.print("Enter name: ");
                String name = scanner.nextLine();
                employees.stream()
                        .filter(e -> e.getName().equalsIgnoreCase(name))
                        .forEach(System.out::println);
            }
            case 2 -> {
                System.out.print("Enter ID: ");
                String id = scanner.nextLine();
                employees.stream()
                        .filter(e -> e.getId().equals(id))
                        .forEach(System.out::println);
            }
            case 3 -> {
                System.out.print("Enter position: ");
                String position = scanner.nextLine();
                employees.stream()
                        .filter(e -> e.getPosition().equalsIgnoreCase(position))
                        .forEach(System.out::println);
            }
            default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}

class Employee {
    private String name;
    private final String id;
    private String position;
    private double salary;

    public Employee(String name, String id, String position, double salary) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }
}

class User {
    private final String username;
    private final String password;
    private final String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
