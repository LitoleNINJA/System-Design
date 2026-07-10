package DesignPatterns.State;

public class IdleState implements VendingMachineState{
    @Override
    public void insertCoin(VendingMachine vendingMachine, int amount) {
        vendingMachine.addBalance(amount);
        System.out.printf("[IdleState] Accepted %dc. Balance=%dc %n", amount, vendingMachine.getBalance());
        vendingMachine.setState(new HasMoneyState());
    }

    @Override
    public void selectItem(VendingMachine vendingMachine, String itemCode) {
        System.out.println("[IdleState] Insert coin first");
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        System.out.println("[IdleState] Insert coin first");
    }


    @Override
    public void refund(VendingMachine vendingMachine) {
        System.out.println("[IdleState] Insert coin first");
    }
}
