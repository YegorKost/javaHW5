package com.yegor.service;

import com.yegor.models.Car;
import com.yegor.models.Engine;

/**
 * Created by YegorKost on 23.02.2017.
 */
public interface Service {
    Car getCarById(int id);
    Engine getEngineById(int id);
    void insertCar(Car car);
    void insertEngine(Engine engine);
}
