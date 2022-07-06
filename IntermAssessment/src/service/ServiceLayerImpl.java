package service;

import dao.Dao;
import dao.PersistenceException;
import dto.Coins;
import dto.Item;
import ui.View;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceLayerImpl implements ServiceLayer {
    Dao dao;
    View view;

    public ServiceLayerImpl(Dao dao, View view) {
        this.dao = dao;
        this.view = view;
    }

    @Override
    public List<Item> getAllItems() throws
            PersistenceException {
        return dao.getAllItems();
    }



    @Override
    public int decrementItemInventoryQty(String itemId) throws NoItemInventoryException, PersistenceException {
        Item item = dao.getItem(itemId);
        int currentItemQuantity = Integer.parseInt(item.getItemQuantity());
        if (currentItemQuantity == 0) {
            String itemName = item.getItemName();
            System.out.println("Insufficient quantity: " + itemName + " not in stock");
            return 0;
        }
        currentItemQuantity = currentItemQuantity - 1;
        return currentItemQuantity;
    }

    // method that calculates change
    @Override
    public BigDecimal calculateReturnedChange(BigDecimal userMoneyBalance, Item item) throws InsufficientFundsException {
        BigDecimal price = new BigDecimal(item.getItemPrice());
        if (userMoneyBalance.compareTo(price) == -1) {
            throw new InsufficientFundsException("You have not entered enough coins." + "The item price is: £ " + price);
        }
        return userMoneyBalance.subtract(price);
    }


    @Override
    public BigDecimal changeDueInPennies(BigDecimal itemCost, BigDecimal money) {
        BigDecimal changeDueInPennies = (money.subtract(itemCost)).multiply(new BigDecimal("100"));
        System.out.println("Change due: £" + (changeDueInPennies.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).toString()));
        return changeDueInPennies;
    }
    @Override
    public Map<BigDecimal, BigDecimal> changeDuePerCoin(BigDecimal itemCost, BigDecimal money) {
        Coins[] coinEnumArray = Coins.values();
        ArrayList<String> coinStringList = new ArrayList<>();
        for (Coins coin : coinEnumArray) {
            int coinInt = coin.getValue();
            coinStringList.add(String.valueOf(coinInt));
        }

        ArrayList<BigDecimal> coins = new ArrayList<BigDecimal>();
        for (String coin : coinStringList) {
            coins.add(new BigDecimal(coin));
        }

        BigDecimal changeDueInPennies = changeDueInPennies(itemCost, money);

        BigDecimal noOfCoin;
        BigDecimal zero = new BigDecimal("0");

        Map<BigDecimal, BigDecimal> amountPerCoin = new HashMap<>();


        for (BigDecimal coin : coins) {

            if (changeDueInPennies.compareTo(coin) >= 0) {

                if (!changeDueInPennies.remainder(coin).equals(zero)) {

                    noOfCoin = changeDueInPennies.divide(coin, 0, RoundingMode.DOWN);

                    amountPerCoin.put(coin, noOfCoin);

                    changeDueInPennies = changeDueInPennies.remainder(coin);

                    if (changeDueInPennies.compareTo(zero) < 0) {
                        break;
                    }

                } else if (changeDueInPennies.remainder(coin).equals(zero)) {  //could change to just else
                    noOfCoin = changeDueInPennies.divide(coin, 0, RoundingMode.DOWN);
                    amountPerCoin.put(coin, noOfCoin);

                    if ((changeDueInPennies.compareTo(zero)) < 0) {
                        break;
                    }
                }
            } else {

            }
        }
        return amountPerCoin;
    }
}
