package DesignPatterns.State;

import java.util.Map;

public class VendingMachine {

    public static class Item {
        private final String code;
        private final String name;
        private final int    priceInCents;
        private int          stockCount;       // mutable — decremented on dispense

        public Item(String code, String name, int priceInCents, int stockCount) {
            this.code         = code;
            this.name         = name;
            this.priceInCents = priceInCents;
            this.stockCount   = stockCount;
        }

        public String getCode()         { return code; }
        public String getName()         { return name; }
        public int    getPriceInCents() { return priceInCents; }
        public int    getStockCount()   { return stockCount; }
        public void   decrementStock()  { stockCount--; }
    }

    private final Map<String, Item> inventory;
    private int                     balance;
    private String                  selectedItemCode;
    private VendingMachineState     state;

    public VendingMachine(Map<String, Item> items) {
        this.inventory        = items;
        this.balance          = 0;
        this.selectedItemCode = null;
        this.state            = new IdleState();
    }

    // === public action API — every method is a one-line delegate ===

    public void insertCoin(int amount)       { state.insertCoin(this, amount); }
    public void selectItem(String itemCode)  { state.selectItem(this, itemCode); }
    public void dispense()                   { state.dispense(this); }
    public void refund()                     { state.refund(this); }

    // === helpers used by the state classes ===

    public void setState(VendingMachineState state) { this.state = state; }

    public int    getBalance()                     { return balance; }
    public void   addBalance(int amount)           { this.balance += amount; }
    public void   removeBalance(int amount)        { this.balance -= amount; }
    public void   clearBalance()                   { this.balance = 0; }

    public String getSelectedItemCode()            { return selectedItemCode; }
    public void   setSelectedItemCode(String code) { this.selectedItemCode = code; }

    public Item   getItem(String itemCode)         { return inventory.get(itemCode); }
    public void   consumeOne(String itemCode) {
        Item item = inventory.get(itemCode);
        if (item != null) item.decrementStock();
    }

    public boolean isAllSoldOut() {
        return inventory.values().stream().allMatch(i -> i.getStockCount() == 0);
    }
}
