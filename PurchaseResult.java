import java.io.Serializable;

public class PurchaseResult implements Serializable {
    private static final long serialVersionUID = 1L;
    public int hungerLevel;
    public double balance;

    public PurchaseResult(int hungerLevel, double balance) {
        this.hungerLevel = hungerLevel;
        this.balance = balance;
    }
}
