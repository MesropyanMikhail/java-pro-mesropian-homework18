package ua.hillel.dbmanager;

public class QueryStore {

    public static String queryType(QueryType queryType) {

        switch (queryType) {
            case AMOUNTGREATERNUMBEROFDIFFERENTEQUALS:
                return provideQueryFindOrderAmountGreaterNumberOfDifferentEquals();
            case CONTAININGGIVENNOMENCLATURE:
                return provideQueryFindOrderContainingGivenNomenclature();
            case NOTCONTAININGGIVENNOMENCLATUREORDERINGCURRENTDATE:
                return provideQueryFindOrderNotContainingGivenNomenclatureOrderingCurrentDate();
            case CREATENEWORDERSHOPORDERTABULARPART:
                return provideQueryCreateNewOrderShopOrder();
            case CREATENEWORDERSHOPORDERSHOPORDER:
                return provideQueryCreateNewOrderShopOrder();
            case DELETEORDERS:
                return provideQueryDeleteOrders();
            default:
                return "";
        }
    }

    private static String provideQueryFindOrderAmountGreaterNumberOfDifferentEquals() {

        return """
                SELECT order_id,
                       MAX(date_receipt)                            AS date_receipt,
                       Sum(price)                                   AS sum_price,
                       Count(DISTINCT tabular_part.nomenclature_id) AS nomenclature_count
                FROM   shop_order
                       JOIN tabular_part
                         ON shop_order.tabular_part_id = tabular_part.tabular_part_id
                       JOIN nomenclature
                         ON tabular_part.nomenclature_id = nomenclature.nomenclature_id
                GROUP  BY order_id
                HAVING sum_price > ?
                       AND nomenclature_count = ?""";
    }

    private static String provideQueryFindOrderContainingGivenNomenclature() {

        return """
                SELECT DISTINCT order_id,
                                date_receipt
                FROM   shop_order
                       JOIN tabular_part
                         ON shop_order.tabular_part_id = tabular_part.tabular_part_id
                WHERE  nomenclature_id = ?""";
    }

    private static String provideQueryFindOrderNotContainingGivenNomenclatureOrderingCurrentDate() {

        return """
                SELECT DISTINCT order_id,
                                date_receipt
                FROM   shop_order
                WHERE  NOT tabular_part_id IN (SELECT tabular_part_id
                                               FROM   tabular_part
                                               WHERE  tabular_part.nomenclature_id = ?)
                       AND date_receipt BETWEEN Addtime(Curdate(), '00:00:00') AND
                                                Addtime(Curdate(), '23:59:59')""";
    }

    private static String provideQueryCreateNewOrderTabularPart() {

        return """
                 INSERT INTO tabular_part
                            (tabular_part_id,
                             nomenclature_id,
                             quantity)
                SELECT Max(tabular_part_id) + 1 AS tabular_part_id,
                       nomenclature_id,
                       Sum(quantity)            AS quantity
                FROM   tabular_part
                WHERE  tabular_part_id IN (SELECT DISTINCT tabular_part_id
                                           FROM   shop_order
                                           WHERE  date_receipt BETWEEN
                                                  Addtime(Curdate(), '00:00:00')
                                                  AND
                                                  Addtime(Curdate(), '23:59:59'))
                GROUP  BY nomenclature_id
                """;
    }

    private static String provideQueryCreateNewOrderShopOrder() {

        return """
                INSERT INTO shop_order
                            (tabular_part_id)
                SELECT Max(tabular_part_id)+1
                FROM   shop_order""";
    }

    private static String provideQueryDeleteOrders() {

        return """
                 DELETE shop_order,
                       tabular_part
                FROM   shop_order
                JOIN   tabular_part
                ON     shop_order.tabular_part_id = tabular_part.tabular_part_id
                WHERE  tabular_part.nomenclature_id = ?
                AND    tabular_part.quantity = ?""";
    }

}
