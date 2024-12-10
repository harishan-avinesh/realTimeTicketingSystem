package cli;

import systemconfig.SystemConfig;
import core.TicketPool;
import core.Vendor;
import core.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SystemConfig config = new SystemConfig();
        TicketPool ticketPool = null;
        List<Thread> vendorThreads = new ArrayList<>();
        List<Thread> customerThreads = new ArrayList<>();

        // Welcome message
        System.out.println("--- Welcome to the Real-Time Ticketing System ---");

        // Configuration process
        while (true) {
            System.out.print("Do you want to load an existing configuration? (yes/no): ");
            String loadChoice = scanner.nextLine().trim().toLowerCase();

            if (loadChoice.equals("yes")) {
                if (!config.loadConfiguration()) {
                    config.configureSystem();
                    break;
                    //System.out.println("No configuration file found. Switching to manual configuration...");
                } else {
                    System.out.println("Configuration loaded successfully!");
                    break;
                }
            } else if (loadChoice.equals("no")) {
                config.configureSystem();
                System.out.print("Would you like to save this configuration? (yes/no): ");
                String saveChoice = scanner.nextLine().trim().toLowerCase();
                if (saveChoice.equals("yes")) {
                    config.saveConfiguration();
                } else {
                    System.out.println("Configuration not saved.");
                }
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        // Initialize TicketPool with configured values
        ticketPool = new TicketPool(config);

        // Start Simulation
        System.out.println("\nConfiguration complete! Press Enter to start the simulation.");
        try {
            System.in.read(); // Wait for the user to press Enter
        } catch (Exception e) {
            System.out.println("Error while waiting for user input.");
        }

        // Ask the user for the number of vendors and customers
        int numVendors = promptValidInput(scanner, "Enter the number of vendors to simulate: ");
        int numCustomers = promptValidInput(scanner, "Enter the number of customers to simulate: ");
        int numTicketsPerBatch = promptValidInput(scanner, "Enter the number of tickets to release per batch by a vendor: ");

        System.out.println("\nSimulation started with " + numVendors + " vendors and " + numCustomers + " customers. Press Enter to stop.");

        // Initialize threads dynamically based on user input
        for (int i = 1; i <= numVendors; i++) {
            Vendor vendor = new Vendor("Vendor" + i, numTicketsPerBatch, config.getTicketReleaseRate(), ticketPool);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        for (int i = 1; i <= numCustomers; i++) {
            Customer customer = new Customer("Customer" + i, config.getCustomerRetrievalRate(), ticketPool);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

        // Monitor the ticket pool for reaching the limit
        while (true) {
            try {
                if (ticketPool.isTicketLimitReached()) {
                    System.out.println("\nTotal ticket limit reached. Stopping simulation...");
                    stopAllThreads(vendorThreads, customerThreads);
                    break;
                }

                System.in.read(); // Wait for the user to press Enter to stop manually
                System.out.println("\nStopping simulation manually...");
                stopAllThreads(vendorThreads, customerThreads);
                break;

            } catch (Exception e) {
                System.out.println("Error while waiting for user input.");
            }
        }

        System.out.println("Simulation stopped. Exiting program...");
/*
        // Stop Simulation
        try {
            System.in.read(); // Wait for the user to press Enter
        } catch (Exception e) {
            System.out.println("Error while waiting for user input.");
        }

        System.out.println("Stopping simulation...");
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }

        vendorThreads.clear();
        customerThreads.clear();
        System.out.println("Simulation stopped. Exiting program..."); */
    }

    // Helper method to validate and prompt for integer input
    private static int promptValidInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be a positive integer. Try again.");
            } else {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
    private static void stopAllThreads(List<Thread> vendorThreads, List<Thread> customerThreads) {
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }
        for (Thread thread : customerThreads) {
            thread.interrupt();
        }

        vendorThreads.clear();
        customerThreads.clear();
        System.out.println("All threads have been stopped.");
    }
}




/*
import systemconfig.SystemConfig;
import core.TicketPool;
import core.Vendor;
import core.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SystemConfig config = new SystemConfig();
        TicketPool ticketPool = null;
        List<Thread> vendorThreads = new ArrayList<>();
        List<Thread> customerThreads = new ArrayList<>();
        boolean simulationRunning = false;

        // Enter the configuration process directly
        System.out.println("--- Welcome to the Real-Time Ticketing System ---");
        System.out.println("Entering configuration process...");

        // Configuration process
        System.out.print("Do you want to load an existing configuration? (yes/no): ");
        String loadChoice = scanner.nextLine().trim().toLowerCase();

        if (loadChoice.equals("yes")) {
            config.loadConfiguration(); // Load configuration
        } else {
            config.configureSystem(); // Manual configuration
            System.out.print("Would you like to save this configuration? (yes/no): ");
            String saveChoice = scanner.nextLine().trim().toLowerCase();
            if (saveChoice.equals("yes")) {
                config.saveConfiguration(); // Save configuration to JSON
            } else {
                System.out.println("Configuration not saved.");
            }
        }

        // Initialize TicketPool with configured values
        ticketPool = new TicketPool(config);

        // Main menu
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Start Simulation");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Start Simulation
                    if (simulationRunning) {
                        System.out.println("Simulation is already running!");
                        break;
                    }

                    // Ask the user for the number of vendors and customers
                    System.out.print("Enter the number of vendors to simulate: ");
                    int numVendors = scanner.nextInt();
                    System.out.print("Enter the number of customers to simulate: ");
                    int numCustomers = scanner.nextInt();
                    System.out.println("Enter the number of tickets to release per batch by a vendor: ");
                    int numTicketsPerBatch = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.println("Simulation started with " + numVendors + " vendors and " + numCustomers + " customers. Press Enter to stop.");


                    // Initialize threads dynamically based on user input
                    for (int i = 1; i <= numVendors; i++) {
                        Vendor vendor = new Vendor("Vendor" + i,numTicketsPerBatch, config.getTicketReleaseRate(), ticketPool);
                        Thread vendorThread = new Thread(vendor);
                        vendorThreads.add(vendorThread);
                        vendorThread.start();
                    }

                    for (int i = 1; i <= numCustomers; i++) {
                        Customer customer = new Customer("Customer" + i, config.getCustomerRetrievalRate(), ticketPool);
                        Thread customerThread = new Thread(customer);
                        customerThreads.add(customerThread);
                        customerThread.start();
                    }

                    simulationRunning = true;
                    try {
                        System.in.read(); // Wait for the user to press Enter
                    } catch (Exception e) {
                        System.out.println("Error while waiting for user input.");
                    }

                    // Stop threads when Enter is pressed
                    System.out.println("Stopping simulation...");
                    for (Thread thread : vendorThreads) {
                        thread.interrupt();
                    }
                    for (Thread thread : customerThreads) {
                        thread.interrupt();
                    }

                    vendorThreads.clear();
                    customerThreads.clear();
                    simulationRunning = false;
                    System.out.println("Simulation stopped. Returning to main menu...");
                    break;

                case 2: // Exit
                    if (simulationRunning) {
                        System.out.println("Please stop the simulation before exiting.");
                        break;
                    }
                    System.out.println("Exiting program. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
*/