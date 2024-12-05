package cli;

import systemconfig.SystemConfig;

public class CLI {
    public static void main(String[] args) {
        System.out.println("Welcome to the Real-Time Ticketing System");
        //Configuring the system.
        SystemConfig config = new SystemConfig();
        config.configureSystem();

    }
}
