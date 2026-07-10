package DesignPatterns.State;

public class HasMoneyState implements VendingMachineState{
    @Override
    public void insertCoin(VendingMachine vendingMachine, int amount) {
        vendingMachine.addBalance(amount);
        System.out.printf("[HasMoneyState] Accepted %dc. Balance=%dc %n", amount, vendingMachine.getBalance());
    }

    @Override
    public void selectItem(VendingMachine vendingMachine, String itemCode) {
        VendingMachine.Item item = vendingMachine.getItem(itemCode);
        if(item == null) {
            System.out.println("[HasMoneyState] No such item: " + itemCode);
            return;
        } else if(item.getStockCount() == 0) {
            System.out.printf("[HasMoneyState] %s (%s) is sold out%n", itemCode, item.getName());
            return;
        } else if(vendingMachine.getBalance() < item.getPriceInCents()) {
            System.out.printf("[HasMoneyState] Not enough — need %dc, have %dc%n",
                    item.getPriceInCents(),
                    vendingMachine.getBalance());
            return;
        }
        vendingMachine.setSelectedItemCode(itemCode);

        System.out.printf("[HasMoneyState] Selected %s (%s). Dispensing...%n", itemCode, item.getName());
        vendingMachine.setState(new DispenseState());
        vendingMachine.dispense();
    }

    @Override
    public void dispense(VendingMachine vendingMachine) {
        System.out.println("[HasMoneyState] Select an item first");
    }

    @Override
    public void refund(VendingMachine vendingMachine) {
        int refunded = vendingMachine.getBalance();
        vendingMachine.clearBalance();
        System.out.printf("[HasMoneyState] Refunding %dc. Balance=%dc%n",
                refunded, vendingMachine.getBalance());
        vendingMachine.setState(new IdleState());
    }
}
