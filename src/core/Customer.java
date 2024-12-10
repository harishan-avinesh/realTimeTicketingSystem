package core;

import utility.LoggerConfig;

import java.util.logging.Logger;

public class Customer implements Runnable {
    private final String customerId;
    private final int retrievalInterval;  // Time interval between purchase attempts (ms)
    private final TicketPool ticketPool;  // Reference to the shared TicketPool

    private static final Logger logger = LoggerConfig.getLogger(Customer.class); // Using LoggerConfig to initialize logger

    public Customer(String customerId, int retrievalInterval, TicketPool ticketPool) {
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Attempt to purchase a ticket
                String ticket = ticketPool.removeTicket();

                // If no tickets are left and the total limit is reached, stop the thread
                if (ticket == null) {
                    logger.info("Customer " + customerId + " found no tickets available. Stopping.");
                    //System.out.println("Customer " + customerId + " found no tickets available. Stopping.");
                    return; // Exit the thread
                }

                // Print the ticket purchased by this customer
                //System.out.println("Customer " + customerId + " purchased: " + ticket);
                logger.info("Customer " + customerId + " purchased: " + ticket);

                // Wait for the next purchase attempt
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            logger.warning("Customer " + customerId + " interrupted.");

            //System.out.println("Customer " + customerId + " interrupted.");
        }
    }
}
