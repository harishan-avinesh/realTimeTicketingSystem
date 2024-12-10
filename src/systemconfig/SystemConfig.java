package systemconfig;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.*;

import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;

public class SystemConfig {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    private static final Logger logger = Logger.getLogger(SystemConfig.class.getName());
    private static final String CONFIG_FILE = "config.json";

    public void configureSystem(){
        Scanner scanner = new Scanner(System.in);
        try {
            // Configure FileHandler for file logging
            FileHandler fileHandler = new FileHandler("system_config.log", true);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

            // Configure ConsoleHandler for console logging
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);

            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger for SystemConfig", e);
        }

        //A method to validate inputs is called to get positive integers for the four configuration variables.
        totalTickets = promptValidInput(scanner , "\nEnter Total number of tickets: ");

        maxTicketCapacity = promptValidInput(scanner , "Enter Max Ticket Capacity: ");

        //Ensuring that maxTicketCapacity is less than or equal to totalTickets
        while(totalTickets < maxTicketCapacity){
            System.out.println("Error. Max Ticket Capacity must be equal or less than Total Tickets.");
            maxTicketCapacity = promptValidInput(scanner , "Re-enter Max Ticket Capacity: ");
        }

        ticketReleaseRate = promptValidInput(scanner , "Enter Ticket Release Rate (ms): ");
        customerRetrievalRate = promptValidInput(scanner , "Enter Customer Retrieval Rate (ms): ");
        //checking practical values to be implemented later

        //warning given if customerRetrievalRate > ticketReleaseRate
        if(customerRetrievalRate > ticketReleaseRate){
            System.out.println("\nWarning: Customers are retrieving tickets faster than vendors are adding them. " +
                                    "This might create in waiting periods for customers.\n");
        }

        System.out.println("System Configuration Complete!\n");
        displaySystemConfig();


    }

    private int promptValidInput(Scanner scanner , String prompt){
        int value = -1;
        while(value <= 0){
            System.out.print(prompt);
            if (scanner.hasNextInt()){
                value = scanner.nextInt();
                if (value <= 0){
                    System.out.print("Value must be a positive integer. Try again: ");
                }
            }
            else{
                System.out.print("Invalid input. Please enter a positive integer. \n");
                scanner.next(); //To consume the invalid input
            }
        }
        return value;
    }
    public void displaySystemConfig(){
        System.out.println(
                "System Configuration:\n" +
                "Total Tickets: " + totalTickets + "\n" +
                "Ticket Release Rate: " + ticketReleaseRate + "ms\n" +
                "Customer Retrieval Rate: " + customerRetrievalRate + "ms\n" +
                "Maximum Ticket Capacity: " + maxTicketCapacity);
    }

    //method to save the configuration as a json file
    public void saveConfiguration() {
        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            Gson gson = new Gson();
            gson.toJson(this, writer);

            logger.log(Level.INFO, "Configuration saved: Total Tickets: {0}, Max Capacity: {1}, Ticket Release Rate: {2}, Customer Retrieval Rate: {3}",
                    new Object[]{totalTickets, maxTicketCapacity, ticketReleaseRate, customerRetrievalRate});
            System.out.println("Configuration saved to " + CONFIG_FILE);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving configuration", e);
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    public boolean loadConfiguration() {
        try (Reader reader = new FileReader(CONFIG_FILE)) {
            Gson gson = new Gson();
            SystemConfig loadedConfig = gson.fromJson(reader, SystemConfig.class);

            // Copy values into the current object
            this.totalTickets = loadedConfig.totalTickets;
            this.ticketReleaseRate = loadedConfig.ticketReleaseRate;
            this.customerRetrievalRate = loadedConfig.customerRetrievalRate;
            this.maxTicketCapacity = loadedConfig.maxTicketCapacity;

            logger.log(Level.INFO, "Configuration loaded from file: {0}", CONFIG_FILE);

            //System.out.println("Configuration loaded from " + CONFIG_FILE);
            displaySystemConfig();
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("No saved configuration found. Please configure the system manually.\n");
        } catch (JsonSyntaxException | IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration", e);

            System.err.println("Error loading configuration: " + e.getMessage());
        }
        return false;
    }



    // Getters
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }


}


