package com.yegor.dao;

import com.yegor.models.Car;
import com.yegor.models.Engine;
import com.yegor.jdbcConnection.PostgreSqlConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YegorKost on 23.02.2017.
 */
public class CarDAO implements DAO<Car> {

    @Override
    public void insert(Car entity) {
        String query = "INSERT INTO car (make, model, id_engine, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, entity.getMake());
            preparedStatement.setString(2, entity.getModel());
            preparedStatement.setInt(3, entity.getEngine().getId());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Car get(int id) {
        Car result = null;
        String query = "SELECT c.*, e.displacement, e.power " +
                "FROM car c INNER JOIN engine e " +
                "ON c.id_engine=e.id WHERE c.id=?;";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = new Car();
                result.setId(resultSet.getByte(1));
                result.setMake(resultSet.getString(2));
                result.setModel(resultSet.getString(3));
                Engine engine = new Engine();
                engine.setId(resultSet.getInt(4));
                engine.setDisplacement(resultSet.getDouble(6));
                engine.setPower(resultSet.getDouble(7));
                result.setEngine(engine);
                result.setPrice(resultSet.getDouble(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Car entity) {
        String query = "UPDATE car SET make=?, model=?, id_engine=?, price=? WHERE id=?";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, entity.getMake());
            preparedStatement.setString(2, entity.getModel());
            preparedStatement.setInt(3, entity.getEngine().getId());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM car WHERE id=?";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> getAll() {
        List<Car> carList = new ArrayList<>();
        String query = "SELECT * FROM car c INNER JOIN engine e ON c.id_engine = e.id;";
        try (PreparedStatement preparedStatement = PostgreSqlConnection.getConnection().prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getInt(1));
                car.setMake(resultSet.getString(2));
                car.setModel(resultSet.getString(3));
                car.setPrice(resultSet.getDouble(5));
                Engine engine = new Engine();
                engine.setId(resultSet.getInt(6));
                engine.setDisplacement(resultSet.getDouble(7));
                engine.setPower(resultSet.getDouble(8));
                car.setEngine(engine);
                carList.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carList;
    }
}
