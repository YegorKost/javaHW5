package com.yegor.models;

import java.util.Set;

/**
 * Created by YegorKost on 23.02.2017.
 */
public class Engine {
    private int id;
    private double displacement;
    private double power;
    private Set<Car> carSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDisplacement() {
        return displacement;
    }

    public void setDisplacement(double displacement) {
        this.displacement = displacement;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public Set<Car> getCarSet() {
        return carSet;
    }

    public void setCarSet(Set<Car> carSet) {
        this.carSet = carSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Engine engine = (Engine) o;

        if (id != engine.id) return false;
        if (Double.compare(engine.displacement, displacement) != 0) return false;
        return Double.compare(engine.power, power) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(displacement);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(power);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "id=" + id +
                ", displacement=" + displacement +
                ", power=" + power + '}';
    }
}
