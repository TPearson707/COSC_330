public class Maryland implements SalesTax {
    public double charge(double currentCost) {
        return currentCost * 1.06;
    }
}
