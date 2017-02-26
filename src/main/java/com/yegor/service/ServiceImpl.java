package com.yegor.service;

import com.yegor.dao.CarDAO;
import com.yegor.dao.DAO;
import com.yegor.dao.EngineDAO;
import com.yegor.models.Car;
import com.yegor.models.Engine;

/**
 * Created by YegorKost on 23.02.2017.
 */
public class ServiceImpl implements Service {

    private DAO<Car> carDAO = new CarDAO();
    private DAO<Engine> engineDAO = new EngineDAO();

    @Override
    public Car getCarById(int id) {
        return carDAO.get(id);
    }

    @Override
    public Engine getEngineById(int id) {
        return engineDAO.get(id);
    }

    @Override
    public void insertCar(Car car) {
        carDAO.insert(car);
    }

    @Override
    public void insertEngine(Engine engine) {
        engineDAO.insert(engine);
    }
}
