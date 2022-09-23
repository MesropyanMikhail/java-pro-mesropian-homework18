package ua.hillel.repository;

import ua.hillel.connection.ConnectionProvider;
import ua.hillel.entity.Nomenclature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class NomenclatureRepository {

    public List<Nomenclature> findNomenclatureById(int nomenclatureId) {

        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(provideQueryFindNomenclatureById())) {
                preparedStatement.setInt(1, nomenclatureId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List result = new ArrayList();
                while (resultSet.next()) {
                    result.add(new Nomenclature(resultSet.getInt("nomenclature_id"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getDouble("price")));
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

    private String provideQueryFindNomenclatureById() {
        return """
                SELECT *
                FROM   nomenclature
                WHERE  nomenclature_id = ?""";
    }

}
