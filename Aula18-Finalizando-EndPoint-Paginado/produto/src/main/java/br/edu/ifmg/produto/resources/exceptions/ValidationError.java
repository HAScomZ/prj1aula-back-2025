package br.edu.ifmg.produto.resources.exceptions;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{

    private List<FieldMessage> erros
            = new ArrayList<>();

    public ValidationError() {}

    public List<FieldMessage> getFieldMessages() {
        return erros;
    }

    public void setFieldMessage(List<FieldMessage> fieldMessage) {
        this.erros = fieldMessage;
    }

    public void addFieldMessage(String field, String message){
        this.erros.add(new FieldMessage(field, message));
    }
}
