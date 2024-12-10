package core;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer implements Runnable {
    private final String customerId;
    private final int retrievalInterval;  // Time interval between purchase attempts (ms)
    private final TicketPool ticketPool;  // Reference to the shared TicketPool
    private static final Logger logger = Logger.getLogger(Customer.class.getName());

    public Customer(String customerId, int retrievalInterval, TicketPool ticketPool) {
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;

        try {
            FileHandler fileHandler = new FileHandler("customer.log", true);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize logger for Customer: " + customerId, e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Attempt to purchase a ticket
                String ticket = ticketPool.removeTicket();

                // If no tickets are left and the total limit is reached, stop the thread
                if (ticket == null) {
                    logger.log(Level.INFO, "Customer {0} found no tickets available. Stopping.", customerId);

                    //System.out.println("Customer " + customerId + " found no tickets available. Stopping.");
                    return; // Exit the thread
                }

                logger.log(Level.INFO, "Customer {0} purchased: {1}", new Object[]{customerId, ticket});

                // Print the ticket purchased by this customer
                //System.out.println("Customer " + customerId + " purchased: " + ticket);

                // Wait for the next purchase attempt
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Customer {0} interrupted.", customerId);


            //System.out.println("Customer " + customerId + " interrupted.");
        }
    }
}
