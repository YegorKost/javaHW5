package com.yegor.dao;

import com.yegor.models.Car;
import com.yegor.models.Engine;
import com.yegor.jdbcConnection.PostgreSqlConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by YegorKost on 23.02.2017.
 */
public class EngineDAO implements DAO<Engine> {

    @Override
    public void insert(Engine entity) {
        String query = "INSERT INTO engine (displacement, power) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            preparedStatement.setDouble(1, entity.getDisplacement());
            preparedStatement.setDouble(2, entity.getPower());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Engine get(int id) {
        Engine result = null;
        String query = "SELECT e.*, c.* " +
                "FROM engine e INNER JOIN car c " +
                "ON c.id_engine = e.id " +
                "WHERE e.id=?;";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                result = new Engine();
                result.setId(resultSet.getInt(1));
                result.setDisplacement(resultSet.getDouble(2));
                result.setPower(resultSet.getDouble(3));
            }

            Set<Car> carSet = new HashSet<>();
            do {
                Car car = new Car();
                car.setId(resultSet.getInt(4));
                car.setMake(resultSet.getString(5));
                car.setModel(resultSet.getString(6));
                car.setEngine(result);
                car.setPrice(resultSet.getDouble(8));
                carSet.add(car);
            } while (resultSet.next());
            assert result != null;
            result.setCarSet(carSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Engine entity) {
        String query = "UPDATE engine SET displacement=?, power=? WHERE id=?;";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setDouble(1, entity.getDisplacement());
            preparedStatement.setDouble(2, entity.getPower());
            preparedStatement.setInt(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM engine WHERE id=?";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Engine> getAll() {
        List<Engine> engineList = new ArrayList<>();
        String query = "SELECT * FROM engine;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Engine engine = new Engine();
                engine.setId(resultSet.getInt(1));
                engine.setDisplacement(resultSet.getDouble(2));
                engine.setPower(resultSet.getDouble(3));
                engineList.add(engine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return engineList;
    }
}
