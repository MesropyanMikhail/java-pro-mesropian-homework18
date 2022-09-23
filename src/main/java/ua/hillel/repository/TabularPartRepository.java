package ua.hillel.repository;

import ua.hillel.connection.ConnectionProvider;
import ua.hillel.entity.TabularPart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class TabularPartRepository {

    public List<TabularPart> findTabularPartById(int tabularPartId) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryFindTabularPartById())) {
                preparedStatement.setInt(1, tabularPartId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List result = new ArrayList();
                while (resultSet.next()) {
                    result.add(new TabularPart(resultSet.getInt("tabular_part_id"), resultSet.getDouble("quantity")));
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

    private String provideQueryFindTabularPartById() {
        return """
                SELECT *
                FROM   tabular_part
                WHERE  tabular_part_id = ?""";
    }

}
