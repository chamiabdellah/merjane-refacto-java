package com.nimbleways.springboilerplate.exceptions;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long orderId){
        super("could not find an order with the id : "+ orderId);
    }
}
