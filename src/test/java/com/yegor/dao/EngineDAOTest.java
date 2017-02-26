package com.yegor.dao;

import com.yegor.jdbcConnection.PostgreSqlConnection;
import com.yegor.models.Car;
import com.yegor.models.Engine;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by YegorKost on 23.02.2017.
 */
public class EngineDAOTest {
    private EngineDAO engineDAO= new EngineDAO();

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
        engine.setDisplacement(3.0);
        engine.setPower(260);

        engineDAO.insert(engine);

        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234");
        String query = "SELECT power FROM engine WHERE id=3;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(resultSet.next());
            assertEquals("Insert assertion", engine.getPower(), resultSet.getDouble(1), 0);
        }
    }

    @Test
    public void get() throws Exception {
        Engine engineExpected = new Engine();
        engineExpected.setId(2);
        engineExpected.setDisplacement(2.0);
        engineExpected.setPower(160);

        assertEquals("Get assertion", engineExpected, engineDAO.get(2));
    }

    @Test
    public void update() throws Exception {
        Engine engineExpected = new Engine();
        engineExpected.setId(2);
        engineExpected.setDisplacement(2.0);
        engineExpected.setPower(777);

        engineDAO.update(engineExpected);

        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234");
        String query = "SELECT power FROM engine WHERE power=777;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            assertTrue(resultSet.next());
            assertEquals("Update assertion", engineExpected.getPower(), resultSet.getDouble(1), 0);
        }
    }

    @Test
    public void delete() throws Exception {
        engineDAO.delete(1);

        PostgreSqlConnection.setConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234");
        String query = "SELECT * FROM engine WHERE id=1;";
        try (Statement statement = PostgreSqlConnection.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            assertFalse("Delete assertion", resultSet.next());
        }
    }

    @Test
    public void getAll() throws Exception {
        Engine engineExpected1 = new Engine();
        engineExpected1.setId(1);
        engineExpected1.setDisplacement(1.5);
        engineExpected1.setPower(110);

        Engine engineExpected2 = new Engine();
        engineExpected2.setId(2);
        engineExpected2.setDisplacement(2.0);
        engineExpected2.setPower(160);

        List<Engine> engineList= engineDAO.getAll();

        assertTrue("GetAll assertion1", engineList.contains(engineExpected1));
        assertTrue("GetAll assertion2", engineList.contains(engineExpected2));
    }
}