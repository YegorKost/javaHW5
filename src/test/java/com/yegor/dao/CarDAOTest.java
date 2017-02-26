package com.yegor.dao;

import com.yegor.jdbcConnection.PostgreSqlConnection;
import com.yegor.models.Car;
import com.yegor.models.Engine;
import org.junit.*;
import static org.junit.Assert.*;

import java.sql.*;
import java.util.List;

/**
 * Created by YegorKost on 24.02.2017.
 */
public class CarDAOTest {
    private CarDAO carDAO = new CarDAO();

    @BeforeClass
    public static void createDB() {
        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/javaHW5", "postgres", "1234");
        String query = "CREATE DATABASE test OWNER postgres;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void deleteDB() {
        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/javaHW5", "postgres", "1234");
        String deactivateQuery = "SELECT * " +
                "FROM pg_stat_activity " +
                "WHERE datname = 'test';" +
                "SELECT pg_terminate_backend (pg_stat_activity.pid) " +
                "FROM pg_stat_activity " +
                "WHERE pg_stat_activity.datname = 'test';";
        String dropQuery = "DROP DATABASE IF EXISTS test;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            statement.execute(deactivateQuery);
            statement.execute(dropQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234");
        String query = "DROP TABLE IF EXISTS engine, car;" +
                "CREATE TABLE IF NOT EXISTS car (" +
                "  id SERIAL PRIMARY KEY," +
                "  make CHARACTER VARYING NOT NULL," +
                "  model CHARACTER VARYING NOT NULL," +
                "  id_engine INTEGER NOT NULL," +
                "  price NUMERIC);" +
                "CREATE TABLE IF NOT EXISTS engine (" +
                "  id SERIAL PRIMARY KEY," +
                "  displacement NUMERIC," +
                "  power NUMERIC);" +
                "ALTER TABLE car ADD FOREIGN KEY (id_engine) REFERENCES engine (id);";

        String insertQuery = "INSERT INTO engine (displacement, power) " +
                "VALUES (1.5, 110), (2.0, 160); " +
                "INSERT INTO car (make, model, id_engine, price) " +
                "VALUES ('BMW', '320', 2, 20000), ('Audi', 'A4', 2, 22000);";

        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            statement.execute(query);
            statement.execute(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert() throws Exception {
        Engine engine = new Engine();
        engine.setId(2);
        engine.setDisplacement(2.0);
        engine.setPower(160);

        Car car = new Car();
        car.setModel("testModel");
        car.setMake("testMake");
        car.setEngine(engine);

        carDAO.insert(car);

        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234");
        String query = "SELECT model FROM car WHERE id=3;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(resultSet.next());
            assertEquals("Insert assertion", car.getModel(), resultSet.getString(1));
        }
    }

    @Test
    public void get() throws Exception {
        Engine engineExpected = new Engine();
        engineExpected.setId(2);
        engineExpected.setDisplacement(2.0);
        engineExpected.setPower(160);

        Car carExpected = new Car();
        carExpected.setId(1);
        carExpected.setMake("BMW");
        carExpected.setModel("320");
        carExpected.setEngine(engineExpected);
        carExpected.setPrice(20000);

        assertEquals("Get assertion", carExpected, carDAO.get(1));
    }

    @Test
    public void update() throws Exception {
        Engine engineExpected = new Engine();
        engineExpected.setId(2);
        engineExpected.setDisplacement(2.0);
        engineExpected.setPower(160);

        Car carExpected = new Car();
        carExpected.setId(1);
        carExpected.setMake("BMW");
        carExpected.setModel("3277"); // updated
        carExpected.setEngine(engineExpected);
        carExpected.setPrice(20000);

        carDAO.update(carExpected);

        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234");
        String query = "SELECT model FROM car WHERE model='3277';";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(resultSet.next());
            assertEquals("Update assertion", carExpected.getModel(), resultSet.getString(1));
        }
    }

    @Test
    public void delete() throws Exception {
        carDAO.delete(1);

        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234");
        String query = "SELECT * FROM car WHERE id=1;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            assertFalse("Delete assertion", resultSet.next());
        }
    }

    @Test
    public void getAll() throws Exception {
        Engine engineExpected = new Engine();
        engineExpected.setId(2);
        engineExpected.setDisplacement(2.0);
        engineExpected.setPower(160);

        Car carExpected1 = new Car();
        carExpected1.setId(1);
        carExpected1.setMake("BMW");
        carExpected1.setModel("320");
        carExpected1.setEngine(engineExpected);
        carExpected1.setPrice(20000);

        Car carExpected2 = new Car();
        carExpected2.setId(2);
        carExpected2.setMake("Audi");
        carExpected2.setModel("A4");
        carExpected2.setEngine(engineExpected);
        carExpected2.setPrice(22000);

        List<Car> carList = carDAO.getAll();

        assertTrue("GetAll assertion1", carList.contains(carExpected1));
        assertTrue("GetAll assertion2", carList.contains(carExpected2));
    }
}