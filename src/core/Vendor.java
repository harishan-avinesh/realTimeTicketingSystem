package core;
import utility.LoggerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Vendor implements Runnable {
    private final String vendorId;
    private final int ticketsPerRelease; // Number of tickets to release in one batch
    private final int releaseInterval; // Time interval between releases (ms)
    private final TicketPool ticketPool;

    private static final Logger logger = LoggerConfig.getLogger(Vendor.class); // Using LoggerConfig to initialize logger

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

                    logger.info("Vendor " + vendorId + " cannot release more tickets. Ticket limit reached.");

                    //System.out.println("Vendor " + vendorId + " cannot release more tickets. Limit reached.");
                    return; // Stop releasing tickets if the limit is reached
                }

                Thread.sleep(releaseInterval); // Simulate delay between releases

            } catch (InterruptedException e) {
                logger.warning("Vendor " + vendorId + " interrupted.");

                //System.out.println("Vendor " + vendorId + " interrupted.");
                return; // Exit gracefully if the thread is interrupted
            }
        }
    }
}

