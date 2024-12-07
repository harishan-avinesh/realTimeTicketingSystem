package core;

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
