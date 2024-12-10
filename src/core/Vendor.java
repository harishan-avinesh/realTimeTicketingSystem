package core;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;

public class Vendor implements Runnable {
    private final String vendorId;
    private final int ticketsPerRelease; // Number of tickets to release in one batch
    private final int releaseInterval; // Time interval between releases (ms)
    private final TicketPool ticketPool;
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());


    public Vendor(String vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;

        try {
            FileHandler fileHandler = new FileHandler("vendor.log", true);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);

            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger for Vendor: " + vendorId, e);
        }
    }

    @Override
    public void run() {
        int ticketCount = 1; // Tracks tickets released by this vendor

        while (true) {
            try {
                // Create a batch of tickets
                List<String> batchTickets = new ArrayList<>();
                for (int i = 0; i < ticketsPerRelease; i++) {
                    batchTickets.add("Ticket-" + vendorId + "-" + ticketCount++);
                }

                // Attempt to release the entire batch
                if (!ticketPool.addTickets(batchTickets)) {
                    logger.log(Level.WARNING, "Vendor {0} cannot release more tickets. Limit reached.", vendorId);

                    //System.out.println("Vendor " + vendorId + " cannot release more tickets. Limit reached.");
                    return; // Stop releasing tickets if the limit is reached
                }

                Thread.sleep(releaseInterval); // Simulate delay between releases

            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Vendor {0} interrupted.", vendorId);

                //System.out.println("Vendor " + vendorId + " interrupted.");
                return; // Exit gracefully if the thread is interrupted
            }
        }
    }
}

/*
public class Vendor implements Runnable {
    private final String vendorId;
    private final int ticketsPerRelease;
    private final int releaseInterval;
    private final TicketPool ticketPool;

    public Vendor(String vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        int ticketCount = 1; // Tracks tickets released by this vendor

        while (true) {
            try {
                // Synchronize the whole batch release process to ensure only one vendor works at a time
                synchronized (ticketPool) {
                    // Attempt to release all tickets in the batch
                    boolean success = true;
                    for (int i = 0; i < ticketsPerRelease; i++) {
                        String ticket = "Ticket-" + vendorId + "-" + ticketCount++;
                        // If adding a single ticket fails, stop releasing the batch
                        if (!ticketPool.addTicket(ticket)) {
                            success = false;
                            break; // If pool is full or the ticket limit is reached, stop
                        }
                    }

                    // If the batch release was unsuccessful, handle the failure
                    if (!success) {
                        System.out.println("Vendor " + vendorId + " cannot release the full batch of tickets. Pool is full or ticket limit reached.");
                        return; // Stop releasing tickets if not all could be added
                    }
                }

                // Simulate time between releases (only if the full batch was successfully added)
                Thread.sleep(releaseInterval);

            } catch (InterruptedException e) {
                System.out.println("Vendor " + vendorId + " interrupted.");
                return; // Exit gracefully if the thread is interrupted
            }
        }
    }
}

*/

    /*
    @Override
    public void run() {
        try {
            int ticketCount = 1; // Counter for tickets released by this vendor
            while (true) {
                // Releasing the tickets in batches
                for (int i = 0; i < ticketsPerRelease; i++) {
                    String ticket = "Ticket-" + vendorId + "-" + ticketCount++;

                    // Call TicketPool.addTicket() to add the ticket
                    boolean isAdded = ticketPool.addTicket(ticket);
                    if (!isAdded) {
                        System.out.println("Vendor " + vendorId + " has reached the ticket limit.");
                        return; // Stop releasing tickets if the total number of tickets is reached
                    }
                }

                // Wait for the next release interval after releasing the entire batch
                Thread.sleep(releaseInterval);
            }
        } catch (InterruptedException e) {
            System.out.println("Vendor " + vendorId + " interrupted.");
        }
    }
}
*/
