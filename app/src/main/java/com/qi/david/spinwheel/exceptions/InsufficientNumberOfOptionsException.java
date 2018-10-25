package com.qi.david.spinwheel.exceptions;

public class InsufficientNumberOfOptionsException extends RuntimeException{
    public InsufficientNumberOfOptionsException() {
        super("Need more than one text item for the spin wheel!");
    }
}
