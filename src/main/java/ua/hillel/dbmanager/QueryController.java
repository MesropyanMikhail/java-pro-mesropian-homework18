package ua.hillel.dbmanager;

import ua.hillel.connection.ConnectionProvider;
import ua.hillel.entity.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;


public class QueryController {

    private int parameterCounter = 1;

    public List<Order> executeSelectQuery(QueryType queryType, List<Integer> parameter) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(QueryStore.queryType(queryType))) {

                parameter.stream().forEach(p -> {
                    try {
                        preparedStatement.setInt(incrementCounter(), p);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                ResultSet resultSet = preparedStatement.executeQuery();
                List<Order> result = new ArrayList();
                while (resultSet.next()) {
                    result.add(new Order(resultSet.getInt("order_id"), resultSet.getDate("date_receipt").toLocalDate()));
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

    public void createNewOrder() throws SQLException {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (Statement statementCreateNewOrder = connection.createStatement();) {
                connection.setAutoCommit(false);
                statementCreateNewOrder.addBatch(QueryStore.queryType(QueryType.CREATENEWORDERSHOPORDERTABULARPART));
                statementCreateNewOrder.addBatch(QueryStore.queryType(QueryType.CREATENEWORDERSHOPORDERSHOPORDER));
                statementCreateNewOrder.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                ConnectionProvider.connectionClose(connection);
            }
        }
    }

    public void deleteOrders(int nomenclatureId, int quantity) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(QueryStore.queryType(QueryType.DELETEORDERS))) {
                preparedStatement.executeUpdate();
                preparedStatement.setInt(1, nomenclatureId);
                preparedStatement.setInt(2, quantity);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionProvider.connectionClose(connection);
            }
        }
    }

    private int incrementCounter() {
        return parameterCounter++;
    }

}
