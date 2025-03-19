public class Main {
    public static void main(String[] args) {
        TicketSales sales;
        double ticketCost;
        String formatted;

        sales = new TicketSales(new Child(), new Maryland());
        ticketCost = sales.calcTotal();
        formatted = String.format("%.2f", ticketCost);
        System.out.println("Total cost for child from Maryland: " + formatted);

        sales = new TicketSales(new Adult(), new Maryland());
        ticketCost = sales.calcTotal();
        formatted = String.format("%.2f", ticketCost);
        System.out.println("Total cost for adult from Maryland: " + formatted);



    }
}