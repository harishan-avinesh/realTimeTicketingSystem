package systemconfig;

import java.util.Scanner;

public class SystemConfig {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public void configureSystem(){
        Scanner scanner = new Scanner(System.in);

        //A method to validate inputs is called to get positive integers for the four configuration variables.
        totalTickets = promptValidInput(scanner , "Enter Total number of tickets: ");

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

    //Getters

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


