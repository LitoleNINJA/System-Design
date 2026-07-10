package DesignPatterns.State;

public class SoldOutState implements VendingMachineState {

    @Override
    public void insertCoin(VendingMachine vm, int amount) {
        System.out.println("[SoldOutState] Machine sold out — coin rejected");
    }

    @Override
    public void selectItem(VendingMachine vm, String itemCode) {
        System.out.println("[SoldOutState] Machine sold out");
    }

    @Override
    public void dispense(VendingMachine vm) {
        System.out.println("[SoldOutState] Machine sold out");
    }

    @Override
    public void refund(VendingMachine vm) {
        int refunded = vm.getBalance();
        if (refunded > 0) {
            vm.clearBalance();
            System.out.printf("[SoldOutState] Refunding %dc%n", refunded);
        }
    }
}
