package br.edu.ifmg.produto.services.exceptions;

public class EmailException extends RuntimeException{

    public EmailException(String mensagem){
        super(mensagem);
    }

}
