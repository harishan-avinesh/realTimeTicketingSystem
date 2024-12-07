package core;

import systemconfig.SystemConfig;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        // Wait if the pool is empty
        while (tickets.isEmpty()) {
            wait(); // Customer waits if the pool is empty
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



