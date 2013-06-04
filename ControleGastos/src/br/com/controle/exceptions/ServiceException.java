/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controle.exceptions;

/**
 *
 * @author igor.santos
 */
public class ServiceException extends Exception {
    
    private String message;
    
    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
