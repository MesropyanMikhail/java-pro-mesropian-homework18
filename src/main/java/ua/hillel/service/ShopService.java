package ua.hillel.service;

import ua.hillel.connection.ConnectionProvider;
import ua.hillel.entity.Nomenclature;
import ua.hillel.entity.Order;
import ua.hillel.entity.TabularPart;
import ua.hillel.repository.NomenclatureRepository;
import ua.hillel.repository.OrderRepository;
import ua.hillel.repository.TabularPartRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class ShopService {
    private OrderRepository orderRepository;
    private TabularPartRepository tabularPartRepository;
    private NomenclatureRepository nomenclatureRepository;

    public ShopService() {
        this.orderRepository = new OrderRepository();
        this.tabularPartRepository = new TabularPartRepository();
        this.nomenclatureRepository = new NomenclatureRepository();
    }

    public List<Order> orderCompleteInformation(int order_id) {

        List<Order> result = orderRepository.findOrderById(order_id);
        for (Order order : result) {
            List<TabularPart> tabularParts = tabularPartRepository.findTabularPartById(order.getOrder_id());
            order.setTabular_part_id(tabularParts);
            for (TabularPart tabularPart : tabularParts) {
                List<Nomenclature> findingTabularParts = nomenclatureRepository.findNomenclatureById(tabularPart.getTabular_part_id());
                tabularPart.setNomenclature_id(findingTabularParts);

            }
        }
        return result;
    }

    public List<Integer> findOrderAmountGreaterNumberOfDifferentEquals(int sum, int count) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryFindOrderAmountGreaterNumberOfDifferentEquals())) {
                preparedStatement.setInt(1, sum);
                preparedStatement.setInt(2, count);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Integer> result = new ArrayList();
                while (resultSet.next()) {
                    result.add(resultSet.getInt("order_id"));
                }
                return result;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionProvider.connectionClose(connection);
            }
        }

        return List.of();
    }

    public List<Integer> findOrderContainingGivenNomenclature(int nomenclature_id) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryFindOrderContainingGivenNomenclature())) {
                preparedStatement.setInt(1, nomenclature_id);

                ResultSet resultSet = preparedStatement.executeQuery();
                List<Integer> result = new ArrayList();
                while (resultSet.next()) {
                    result.add(resultSet.getInt("order_id"));
                }
                return result;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionProvider.connectionClose(connection);
            }
        }

        return List.of();
    }

    public List<Integer> findOrderNotContainingGivenNomenclatureOrderingCurrentDate(int nomenclature_id) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryFindOrderNotContainingGivenNomenclatureOrderingCurrentDate())) {
                preparedStatement.setInt(1, nomenclature_id);

                ResultSet resultSet = preparedStatement.executeQuery();
                List<Integer> result = new ArrayList();
                while (resultSet.next()) {
                    result.add(resultSet.getInt("order_id"));
                }
                return result;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionProvider.connectionClose(connection);
            }
        }

        return List.of();
    }

    public void createNewOrder() {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatementTabularPart = connection.prepareStatement(provideQueryCreateNewOrderTabularPart());
                 PreparedStatement preparedStatementShopOrder = connection.prepareStatement(provideQueryCreateNewOrderShopOrder())) {
                preparedStatementTabularPart.executeUpdate();
                preparedStatementShopOrder.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionProvider.connectionClose(connection);
            }
        }
    }

    public void DeleteOrders(int nomenclature_id, int quantity) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryDeleteOrders())) {
                preparedStatement.executeUpdate();
                preparedStatement.setInt(1, nomenclature_id);
                preparedStatement.setInt(2, quantity);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionProvider.connectionClose(connection);
            }
        }
    }

    private String provideQueryFindOrderAmountGreaterNumberOfDifferentEquals() {

        return "SELECT order_id,\n" +
                "       Sum(price)                                   AS sum_price,\n" +
                "       Count(DISTINCT tabular_part.nomenclature_id) AS nomenclature_count\n" +
                "FROM   shop_order\n" +
                "       JOIN tabular_part\n" +
                "         ON shop_order.tabular_part_id = tabular_part.tabular_part_id\n" +
                "       JOIN nomenclature\n" +
                "         ON tabular_part.nomenclature_id = nomenclature.nomenclature_id\n" +
                "GROUP  BY order_id\n" +
                "HAVING sum_price > ?\n" +
                "       AND nomenclature_count = ?";
    }

    private String provideQueryFindOrderContainingGivenNomenclature() {

        return "SELECT DISTINCT order_id\n" +
                "FROM   shop_order\n" +
                "       JOIN tabular_part\n" +
                "         ON shop_order.tabular_part_id = tabular_part.tabular_part_id\n" +
                "WHERE  nomenclature_id = ?";
    }

    private String provideQueryFindOrderNotContainingGivenNomenclatureOrderingCurrentDate() {

        return "SELECT DISTINCT order_id\n" +
                "FROM   shop_order\n" +
                "WHERE  NOT tabular_part_id IN (SELECT tabular_part_id\n" +
                "                               FROM   tabular_part\n" +
                "                               WHERE  tabular_part.nomenclature_id = ?)\n" +
                "       AND date_receipt BETWEEN Addtime(Curdate(), '00:00:00') AND\n" +
                "                                Addtime(Curdate(), '23:59:59')";
    }

    private String provideQueryCreateNewOrderTabularPart() {

        return " INSERT INTO tabular_part\n" +
                "            (tabular_part_id,\n" +
                "             nomenclature_id,\n" +
                "             quantity)\n" +
                "SELECT Max(tabular_part_id) + 1 AS tabular_part_id,\n" +
                "       nomenclature_id,\n" +
                "       Sum(quantity)            AS quantity\n" +
                "FROM   tabular_part\n" +
                "WHERE  tabular_part_id IN (SELECT DISTINCT tabular_part_id\n" +
                "                           FROM   shop_order\n" +
                "                           WHERE  date_receipt BETWEEN\n" +
                "                                  Addtime(Curdate(), '00:00:00')\n" +
                "                                  AND\n" +
                "                                  Addtime(Curdate(), '23:59:59'))\n" +
                "GROUP  BY nomenclature_id";
    }

    private String provideQueryCreateNewOrderShopOrder() {

        return "INSERT INTO shop_order\n" +
                "            (tabular_part_id)\n" +
                "SELECT Max(tabular_part_id)+1\n" +
                "FROM   shop_order  ";
    }

    private String provideQueryDeleteOrders() {

        return " DELETE shop_order,\n" +
                "       tabular_part\n" +
                "FROM   shop_order\n" +
                "JOIN   tabular_part\n" +
                "ON     shop_order.tabular_part_id = tabular_part.tabular_part_id\n" +
                "WHERE  tabular_part.nomenclature_id = ?\n" +
                "AND    tabular_part.quantity = ?";
    }

}
