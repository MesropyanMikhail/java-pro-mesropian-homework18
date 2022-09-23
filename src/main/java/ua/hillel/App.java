package ua.hillel;

import ua.hillel.service.ShopService;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) {

        ShopService shopService = new ShopService();
        System.out.println(shopService.findOrderAmountGreaterNumberOfDifferentEquals(10, 2));
        System.out.println(shopService.findOrderContainingGivenNomenclature(3));
        System.out.println(shopService.findOrderNotContainingGivenNomenclatureOrderingCurrentDate(3));
        shopService.createNewOrder();
        //shopService.deleteOrders(1, 3);

    }
}
