public class TicketSales {
    private TicketPrice price;
    private SalesTax tax;

    TicketSales(TicketPrice p, SalesTax st) {
        price = p;
        tax = st;
    }

    public void setTicketPrice(TicketPrice p) {
        price = p;
    }

    public void setSalesTax(SalesTax st) {
        tax = st;
    }

    public double calcTotal() {
        double currentCost = price.charge();
        return tax.charge(currentCost);

    }
}
