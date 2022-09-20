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

    public List<Order> findOrderById(int order_id) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryFindOrderById())) {
                preparedStatement.setInt(1, order_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Order> result = new ArrayList();
                while (resultSet.next()) {
                    result.add(new Order(resultSet.getInt("order_id"), resultSet.getDate("date_receipt")));
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
        /*return "select * from tabular_part \n" +
                "join nomenclature \n" +
                "on tabular_part.nomenclature_id = nomenclature.nomenclature_id\n" +
                "where tabular_part_id in (select tabular_part_id from shop_order where order_id = ?)";*/
        return "select * from shop_order where order_id = ?";
    }

}
