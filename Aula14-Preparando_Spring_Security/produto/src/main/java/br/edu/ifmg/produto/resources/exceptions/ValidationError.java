package br.edu.ifmg.produto.resources.exceptions;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandartError{

    private List<FieldMessage> errors = new ArrayList<FieldMessage>();

    public ValidationError() {
    }

    public List<FieldMessage> getFieldMessages(){
        return errors;
    }

    public void setFieldErrors(List<FieldMessage> fieldMessage){
        this.errors = fieldMessage;
    }

    public void addFieldErrors(String field, String message){
        this.errors.add(new FieldMessage(field, message));
    }

}
