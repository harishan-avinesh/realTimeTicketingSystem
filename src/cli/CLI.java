package cli;

import systemconfig.SystemConfig;

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
            config.loadConfiguration();
        } else {
            config.configureSystem();
            System.out.println("Do you want to save this configuration for future use? (yes/no)");
            choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("yes")) {
                config.saveConfiguration();
            }
        }

    }
}
