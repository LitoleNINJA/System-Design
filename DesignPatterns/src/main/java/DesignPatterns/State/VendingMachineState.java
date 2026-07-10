package DesignPatterns.State;

public interface VendingMachineState {
    void insertCoin(VendingMachine vendingMachine, int amount);
    void selectItem(VendingMachine vendingMachine, String itemCode);
    void dispense(VendingMachine vendingMachine);
    void refund(VendingMachine vendingMachine);
}
