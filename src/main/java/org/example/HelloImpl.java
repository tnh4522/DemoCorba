package org.example;

import org.example.Example.HelloPOA;

public class HelloImpl extends HelloPOA {
    @Override
    public String sayHello() {
        return "Hello from CORBA server!";
    }
}
