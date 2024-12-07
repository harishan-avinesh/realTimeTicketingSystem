package cli;

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
