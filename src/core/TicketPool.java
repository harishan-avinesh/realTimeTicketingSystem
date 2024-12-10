package core;
import systemconfig.SystemConfig;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;

public class TicketPool {
    private final List<String> tickets = new LinkedList<>();
    private final int maxCapacity;
    private int totalTicketsAdded = 0;
    private final int totalTickets;
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());

    private final Lock lock = new ReentrantLock(); // Lock for controlling access
    private final Condition notFull = lock.newCondition(); // Condition for vendors
    private final Condition notEmpty = lock.newCondition(); // Condition for customers


    public TicketPool(SystemConfig config) {
        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTickets = config.getTotalTickets();
        try {
            FileHandler fileHandler = new FileHandler("ticket_pool.log", true);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);

            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger for TicketPool", e);
        }
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

                logger.log(Level.INFO, "Ticket added: {0} (Pool size: {1})", new Object[]{ticket, tickets.size()});

                //System.out.println("Ticket added: " + ticket + " ( Pool size: " + tickets.size() + " )");
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

            logger.log(Level.INFO, "Ticket purchased: {0} (Pool size: {1})", new Object[]{ticket, tickets.size()});
            //System.out.println("Ticket purchased: " + ticket + " ( Pool size: " + tickets.size() + " )");

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


}

/*
import systemconfig.SystemConfig;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final List<String> tickets = Collections.synchronizedList(new LinkedList<>());
    private final int maxCapacity;
    private int totalTicketsAdded = 0; // Track the number of tickets added to the pool
    private final int totalTickets;



    // Constructor to initialize TicketPool with values from SystemConfig
    public TicketPool(SystemConfig config) {
        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTickets = config.getTotalTickets();
    }

    // Synchronized method to add tickets by the vendors
    public synchronized boolean addTicket(String ticket) throws InterruptedException {
        // Wait if the pool is full
        while (tickets.size() >= maxCapacity) {
            wait(); // Vendor waits if the pool is full
        }

        if (totalTicketsAdded >= totalTickets) {
            return false; // Stop adding tickets if the limit is reached
        }

        tickets.add(ticket); // Adds to the end of the list (First in First out method)
        totalTicketsAdded++;
        System.out.println("Ticket added: " + ticket + " | Pool size: " + tickets.size());

        // Notify customers that a ticket has been added
        notifyAll();
        return true;
    }

    // Synchronized method to remove tickets by consumers
    public synchronized String removeTicket() throws InterruptedException {
        // Checks if the ticket pool is empty
        while (tickets.isEmpty()) {
            // Check if total ticket limit is reached
            if (totalTicketsAdded >= totalTickets) {
                return null; // Signal that no tickets are left and no more will be added
            }
            wait(); // Customer waits if the pool is empty and total ticket limit not reached yet
        }

        String ticket = tickets.remove(0); // Removes from the front (FIFO)
        System.out.println("Ticket purchased: " + ticket + " | Pool size: " + tickets.size());

        // Notify vendors that there is space in the pool for more tickets
        notifyAll();
        return ticket;
    }

    // Getter for current pool size
    public synchronized int getCurrentSize() {
        return tickets.size();
    }
}
*/


