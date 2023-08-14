package com.nimbleways.springboilerplate.exceptions;

public class NoSuchProductType extends RuntimeException{
    public NoSuchProductType(String productType){
        super("the operation '"+ productType + "' doesn't exist");
    }
}
