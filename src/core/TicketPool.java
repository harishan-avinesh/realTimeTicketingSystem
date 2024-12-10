package core;
import systemconfig.SystemConfig;
import utility.LoggerConfig;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;

public class TicketPool {
    private final List<String> tickets = new LinkedList<>();
    private final int maxCapacity;
    private int totalTicketsAdded = 0;
    private final int totalTickets;

    private final Lock lock = new ReentrantLock(); // Lock for controlling access
    private final Condition notFull = lock.newCondition(); // Condition for vendors
    private final Condition notEmpty = lock.newCondition(); // Condition for customers

    private static final Logger logger = LoggerConfig.getLogger(TicketPool.class); // Using LoggerConfig to initialize logger

    public TicketPool(SystemConfig config) {
        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTickets = config.getTotalTickets();
    }

    // Method for vendors to add tickets in a batch
    public boolean addTickets(List<String> batchTickets) throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            // Wait if the pool is too full to accommodate the batch
            while (tickets.size() + batchTickets.size() > maxCapacity) {
                notFull.await(); // Vendor waits until there is space
            }

            // Add tickets in batch
            for (String ticket : batchTickets) {
                if (totalTicketsAdded >= totalTickets) {
                    return false; // Stop adding tickets if the total limit is reached
                }
                tickets.add(ticket); // Add ticket to the pool
                totalTicketsAdded++;
                logger.info("\nTicket added: " + ticket + " ( Pool size: " + tickets.size() + " ) Ticket No. " + totalTicketsAdded);
                //System.out.println("Ticket added: " + ticket + " ( Pool size: " + tickets.size() + " )" + "Ticket No. " + totalTicketsAdded);
            }

            notEmpty.signalAll(); // Notify customers that tickets are available
            return true;

        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Method for customers to remove tickets
    public String removeTicket() throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            // Wait if the pool is empty
            while (tickets.isEmpty()) {
                notEmpty.await(); // Customer waits until a ticket is available
            }

            String ticket = tickets.remove(0); // Remove ticket from the pool

            logger.info("\nTicket purchased: " + ticket + " ( Pool size: " + tickets.size() + " ) Ticket No. " + (totalTicketsAdded - tickets.size()));

            //System.out.println("Ticket purchased: " + ticket + " ( Pool size: " + tickets.size() + " )" + "Ticket No. " + (totalTicketsAdded - tickets.size())) ;

            notFull.signalAll(); // Notify vendors that space is available
            return ticket;
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Getter for current pool size (optional for monitoring purposes)
    public int getCurrentSize() {
        return tickets.size();
    }

    public int getTotalTickets() {
        return totalTickets;
    }

}
