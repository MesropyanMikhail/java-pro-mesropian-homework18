package ua.hillel.repository;

import ua.hillel.connection.ConnectionProvider;
import ua.hillel.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static java.util.Objects.*;

public class OrderRepository {

    public List<Order> findOrderById(int orderId) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryFindOrderById())) {
                preparedStatement.setInt(1, orderId);
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

    private String provideQueryFindOrderById() {

        return """
                SELECT *
                FROM   shop_order
                WHERE  order_id = ?""";
    }

}
