package DesignPatterns.State;

public class DispenseState implements VendingMachineState {

    @Override
    public void insertCoin(VendingMachine vm, int amount) {
        System.out.println("[DispensingState] Busy — please wait");
    }

    @Override
    public void selectItem(VendingMachine vm, String itemCode) {
        System.out.println("[DispensingState] Busy — please wait");
    }

    @Override
    public void dispense(VendingMachine vm) {
        String code = vm.getSelectedItemCode();
        VendingMachine.Item item = vm.getItem(code);
        int change = vm.getBalance() - item.getPriceInCents();

        vm.consumeOne(code);
        vm.clearBalance();
        vm.setSelectedItemCode(null);

        System.out.printf("[DispensingState] Dispensed %s. Returning change %dc. Balance=%dc%n",
                item.getName(), change, vm.getBalance());

        if (vm.isAllSoldOut()) {
            System.out.println("[DispensingState] All items sold out — entering SoldOutState");
            vm.setState(new SoldOutState());
        } else if (item.getStockCount() == 0) {
            System.out.printf("[DispensingState] Last unit of %s dispensed — entering SoldOut for %s%n",
                    item.getName(), code);
            vm.setState(new IdleState());
        } else {
            vm.setState(new IdleState());
        }
    }

    @Override
    public void refund(VendingMachine vm) {
        System.out.println("[DispensingState] Cannot refund during dispense");
    }
}
