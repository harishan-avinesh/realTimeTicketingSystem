package cli;
/*
import systemconfig.SystemConfig;
import core.TicketPool;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        System.out.println("Welcome to the Real-Time Ticketing System");
        SystemConfig config = new SystemConfig();
        Scanner scanner = new Scanner(System.in);

        // Option to load existing configuration
        System.out.println("Do you want to load an existing configuration? (yes/no)");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            config.loadConfiguration(); //implementing the method to load existing configuration
        } else {
            config.configureSystem(); // Starting the configuration process
            System.out.println("Do you want to save this configuration for future use? (yes/no)");
            choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("yes")) {
                config.saveConfiguration();
            }
        }

        // Initialize the TicketPool with values from the configuration
        TicketPool ticketPool = new TicketPool(config);


    }
}


import systemconfig.SystemConfig;
import core.TicketPool;
import core.Vendor;
import core.Customer;

public class CLI {
    public static void main(String[] args) {
        SystemConfig config = new SystemConfig();

        System.out.println("--- Real-Time Ticketing System Test ---");

        // Step 1: Configure the system manually or load configuration
        System.out.println("Configuring system with hardcoded values...");
        config.configureSystem(); // You can manually provide inputs when prompted

        // Step 2: Initialize the TicketPool
        TicketPool ticketPool = new TicketPool(config);

        // Hardcoded number of vendors and customers
        int numVendors = 2; // Fixed number of vendors
        int numCustomers = 3; // Fixed number of customers

        // Step 3: Create vendor threads
        Thread vendor1 = new Thread(new Vendor("Vendor1", 3, config.getTicketReleaseRate(), ticketPool));
        Thread vendor2 = new Thread(new Vendor("Vendor2", 3, config.getTicketReleaseRate(), ticketPool));

        // Step 4: Create customer threads
        Thread customer1 = new Thread(new Customer("Customer1", config.getCustomerRetrievalRate(), ticketPool));
        Thread customer2 = new Thread(new Customer("Customer2", config.getCustomerRetrievalRate(), ticketPool));
        Thread customer3 = new Thread(new Customer("Customer3", config.getCustomerRetrievalRate(), ticketPool));

        // Start vendor threads
        vendor1.start();
        vendor2.start();

        // Start customer threads
        customer1.start();
        customer2.start();
        customer3.start();

        // Simulate system running
        System.out.println("Simulation running... Press Enter to stop.");
        try {
            System.in.read(); // Wait for the user to press Enter
        } catch (Exception e) {
            System.out.println("Error while waiting for user input.");
        }

        // Stop simulation
        System.out.println("Stopping simulation...");
        vendor1.interrupt();
        vendor2.interrupt();
        customer1.interrupt();
        customer2.interrupt();
        customer3.interrupt();

        System.out.println("Simulation stopped.");
    }
}

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
        boolean isConfigured = false; // Track if the system is configured
        boolean simulationRunning = false; // Track if the simulation is running

        while (true) {
            System.out.println("\n--- Ticketing System CLI ---");
            System.out.println("1. Configure System");
            System.out.println("2. Load Configuration");
            System.out.println("3. Start Simulation");
            System.out.println("4. Stop Simulation");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Configure the system manually
                    config.configureSystem(); // Prompt user for input
                    isConfigured = true; // Mark system as configured
                    System.out.print("Would you like to save this configuration? (yes/no): ");
                    String saveChoice = scanner.nextLine().trim().toLowerCase();
                    if (saveChoice.equals("yes")) {
                        config.saveConfiguration();
                    } else {
                        System.out.println("Configuration not saved.");
                    }
                    break;

                case 2: // Load configuration
                    config.loadConfiguration(); // Load settings from JSON
                    isConfigured = true; // Mark system as configured
                    break;

                case 3: // Start simulation
                    if (!isConfigured) {
                        System.out.println("Please configure or load the system first!");
                        break;
                    }
                    if (simulationRunning) {
                        System.out.println("Simulation is already running!");
                        break;
                    }

                    // Initialize TicketPool and threads
                    ticketPool = new TicketPool(config);
                    Thread vendor1 = new Thread(new Vendor("Vendor1", 3, config.getTicketReleaseRate(), ticketPool));
                    Thread vendor2 = new Thread(new Vendor("Vendor2", 3, config.getTicketReleaseRate(), ticketPool));
                    Thread customer1 = new Thread(new Customer("Customer1", config.getCustomerRetrievalRate(), ticketPool));
                    Thread customer2 = new Thread(new Customer("Customer2", config.getCustomerRetrievalRate(), ticketPool));

                    vendorThreads.add(vendor1);
                    vendorThreads.add(vendor2);
                    customerThreads.add(customer1);
                    customerThreads.add(customer2);

                    // Start threads
                    vendor1.start();
                    vendor2.start();
                    customer1.start();
                    customer2.start();

                    simulationRunning = true;
                    System.out.println("Simulation started.");
                    break;

                case 4: // Stop simulation
                    if (!simulationRunning) {
                        System.out.println("Simulation is not running!");
                        break;
                    }

                    System.out.println("Stopping simulation...");
                    // Interrupt threads
                    for (Thread thread : vendorThreads) {
                        thread.interrupt();
                    }
                    for (Thread thread : customerThreads) {
                        thread.interrupt();
                    }

                    vendorThreads.clear();
                    customerThreads.clear();
                    simulationRunning = false;
                    System.out.println("Simulation stopped.");
                    break;

                case 5: // Exit
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
