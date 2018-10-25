package com.qi.david.spinwheel.exceptions;

public class InsufficientNumberOfColoursException extends RuntimeException {

    public InsufficientNumberOfColoursException() {
        super("Need more than one color for the spin wheel!");
    }

}
