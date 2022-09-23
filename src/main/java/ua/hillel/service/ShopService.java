package ua.hillel.service;

import ua.hillel.connection.ConnectionProvider;
import ua.hillel.dbmanager.QueryController;
import ua.hillel.dbmanager.QueryStore;
import ua.hillel.dbmanager.QueryType;
import ua.hillel.entity.Nomenclature;
import ua.hillel.entity.Order;
import ua.hillel.entity.TabularPart;
import ua.hillel.repository.NomenclatureRepository;
import ua.hillel.repository.OrderRepository;
import ua.hillel.repository.TabularPartRepository;

import java.sql.SQLException;
import java.util.List;

public class ShopService {
    private OrderRepository orderRepository;
    private TabularPartRepository tabularPartRepository;
    private NomenclatureRepository nomenclatureRepository;

    public ShopService() {
        this.orderRepository = new OrderRepository();
        this.tabularPartRepository = new TabularPartRepository();
        this.nomenclatureRepository = new NomenclatureRepository();
    }

    public List<Order> orderCompleteInformation(int orderId) {

        List<Order> result = orderRepository.findOrderById(orderId);
        for (Order order : result) {
            List<TabularPart> tabularParts = tabularPartRepository.findTabularPartById(order.getOrderId());
            order.setTabularPartId(tabularParts);
            for (TabularPart tabularPart : tabularParts) {
                List<Nomenclature> findingTabularParts = nomenclatureRepository.findNomenclatureById(tabularPart.getTabularPartId());
                tabularPart.setNomenclatureId(findingTabularParts);
            }
        }
        return result;
    }

    public List<Order> findOrderAmountGreaterNumberOfDifferentEquals(int sum, int count) {

        QueryController queryController = new QueryController();
        return queryController.executeSelectQuery(QueryType.AMOUNTGREATERNUMBEROFDIFFERENTEQUALS, List.of(sum, count));

    }

    public List<Order> findOrderContainingGivenNomenclature(int nomenclatureId) {

        QueryController queryController = new QueryController();
        return queryController.executeSelectQuery(QueryType.CONTAININGGIVENNOMENCLATURE, List.of(nomenclatureId));

    }

    public List<Order> findOrderNotContainingGivenNomenclatureOrderingCurrentDate(int nomenclatureId) {

        QueryController queryController = new QueryController();
        return queryController.executeSelectQuery(QueryType.NOTCONTAININGGIVENNOMENCLATUREORDERINGCURRENTDATE, List.of(nomenclatureId));

    }

    public void createNewOrder() {
        QueryController queryController = new QueryController();
        try {
            queryController.createNewOrder();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrders(int nomenclatureId, int quantity) {
        QueryController queryController = new QueryController();
        queryController.deleteOrders(nomenclatureId, quantity);
    }

}
