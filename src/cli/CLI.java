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
        int numVendors = promptValidInput(scanner, "Enter the number of vendors to simulate (<= Total Tickets): ", config.getTotalTickets());
        int numCustomers = promptValidInput(scanner, "Enter the number of customers to simulate (<= Total Tickets): ", config.getTotalTickets());

        int numTicketsPerBatch;
        while (true) {
            // Ask the user for the number of tickets per batch and ensure it's less than maxTicketCapacity
            System.out.print("Enter the number of tickets to release per batch by a vendor: ");
            numTicketsPerBatch = scanner.nextInt();
            if (numTicketsPerBatch <= config.getMaxTicketCapacity()) {
                break; // Proceed if valid
            } else {
                System.out.println("Tickets per batch cannot exceed the max capacity of " + config.getMaxTicketCapacity() + ". Try again.");
            }
            scanner.nextLine(); // Consume newline
        }
        System.out.println("\nSimulation started with " + numVendors + " vendors and " + numCustomers + " customers. Press Enter to stop.");

        // Initialize threads dynamically based on user input
        for (int i = 1; i <= numVendors; i++) {
            Vendor vendor = new Vendor("Vendor" + i, numTicketsPerBatch, config.getTicketReleaseRate(), ticketPool, config);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        for (int i = 1; i <= numCustomers; i++) {
            Customer customer = new Customer("Customer" + i, config.getCustomerRetrievalRate(), ticketPool, config);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            customerThread.start();
        }

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
        System.out.println("Simulation stopped. Exiting program...");
    }



    private static int promptValidInput(Scanner scanner, String prompt, int maxValue) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (value > 0 && value <= maxValue) {
                    return value;
                }
                System.out.println("Value must be a positive integer and less than or equal to " + maxValue + ". Try again.");
            } else {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.nextLine(); // Consume invalid input
            }
        }

    }
}


