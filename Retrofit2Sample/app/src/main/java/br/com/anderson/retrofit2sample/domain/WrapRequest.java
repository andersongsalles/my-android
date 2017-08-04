package br.com.anderson.retrofit2sample.domain;


public class WrapRequest {
    private String method;
    private Car car;


    public WrapRequest() {}
    public WrapRequest(String method, Car car) {
        this.method = method;
        this.car = car;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
