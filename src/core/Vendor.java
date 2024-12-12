package core;
import systemconfig.SystemConfig;
import utility.LoggerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Vendor implements Runnable {
    private final String vendorId;
    private final int ticketsPerRelease; // Number of tickets to release in one batch
    private final int releaseInterval; // Time interval between releases (ms)
    private final TicketPool ticketPool;
    private SystemConfig config;


    private static final Logger logger = LoggerConfig.getLogger(Vendor.class); // Using LoggerConfig to initialize logger

    public Vendor(String vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool, SystemConfig config  ) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
        this.config = config;

    }

    @Override
    public void run() {
        int ticketCount = 1; // Tracks tickets released by this vendor

        while (ticketPool.getTotalTicketsAdded() <= config.getTotalTickets()) {
            try {

                // Create a batch of tickets
                List<String> batchTickets = new ArrayList<>();
                for (int i = 0; i < ticketsPerRelease; i++) {
                    batchTickets.add("Ticket-" + vendorId + "-" + ticketCount++);
                }

                //Stop releasing when the limit is reached
                if (!ticketPool.addTickets(batchTickets)) {

                    logger.info(String.format("[Vendor: %s | Thread: %s] Ticket limit reached. Stopping.",
                            vendorId, Thread.currentThread().getName()));
                    //System.out.println("Vendor " + vendorId + " cannot release more tickets. Limit reached.");
                    return; // Stop releasing tickets if the limit is reached
                }
                else {
                    logger.info(String.format("[Vendor: %s | Thread: %s] Added tickets: %s. Total tickets added: %d.",
                            vendorId, Thread.currentThread().getName(), batchTickets, ticketPool.getTotalTicketsAdded()));

                }

                Thread.sleep(releaseInterval); // Simulate delay between releases

            } catch (InterruptedException e) {
                logger.warning("\nVendor " + vendorId + " interrupted.");

                //System.out.println("Vendor " + vendorId + " interrupted.");
                return; // Exit gracefully if the thread is interrupted
            }
        }
    }
}

