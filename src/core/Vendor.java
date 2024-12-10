package core;
import java.util.ArrayList;
import java.util.List;

public class Vendor implements Runnable {
    private final String vendorId;
    private final int ticketsPerRelease; // Number of tickets to release in one batch
    private final int releaseInterval; // Time interval between releases (ms)
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
                // Create a batch of tickets
                List<String> batchTickets = new ArrayList<>();
                for (int i = 0; i < ticketsPerRelease; i++) {
                    batchTickets.add("Ticket-" + vendorId + "-" + ticketCount++);
                }

                // Attempt to release the entire batch
                if (!ticketPool.addTickets(batchTickets)) {
                    System.out.println("Vendor " + vendorId + " cannot release more tickets. Limit reached.");
                    return; // Stop releasing tickets if the limit is reached
                }

                Thread.sleep(releaseInterval); // Simulate delay between releases

            } catch (InterruptedException e) {
                System.out.println("Vendor " + vendorId + " interrupted.");
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
