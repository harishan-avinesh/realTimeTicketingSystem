package core;

import systemconfig.SystemConfig;
import utility.LoggerConfig;

import java.util.logging.Logger;

public class Customer implements Runnable {
    private final String customerId;
    private final int retrievalInterval;  // Time interval between purchase attempts (ms)
    private final TicketPool ticketPool;  // Reference to the shared TicketPool
    private SystemConfig config;
    private static final Logger logger = LoggerConfig.getLogger(Customer.class); // Using LoggerConfig to initialize logger

    public Customer(String customerId, int retrievalInterval, TicketPool ticketPool, SystemConfig config ) {
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
        this.config = config;
    }

    @Override
    public void run() {
        while (ticketPool.getTicketsSold()<= config.getTotalTickets()) {
            try{
                if (ticketPool.getTicketsSold()>= config.getTotalTickets()) {
                    logger.info(String.format("[Customer: %s | Thread: %s] No tickets available. Stopping.",
                            customerId, Thread.currentThread().getName()));
                    //System.out.println("Customer " + customerId + " found no tickets available. Stopping.");
                    return; // Exit the thread
                }
                // Attempt to purchase a ticket
                String ticket = ticketPool.removeTicket();

                // If no tickets are left and the total limit is reached, stop the thread


                // Print the ticket purchased by this customer
                //System.out.println("Customer " + customerId + " purchased: " + ticket);
                logger.info(String.format("[Customer: %s | Thread: %s] Purchased ticket: %s. Tickets sold: %d/%d.",
                        customerId, Thread.currentThread().getName(), ticket, ticketPool.getTicketsSold(),config.getTotalTickets()));
                // Wait for the next purchase attempt
                Thread.sleep(retrievalInterval);
            }
            catch (InterruptedException e) {
                logger.warning("Customer " + customerId + " interrupted.");
            }
            //System.out.println("Customer " + customerId + " interrupted.");
        }
    }
}
