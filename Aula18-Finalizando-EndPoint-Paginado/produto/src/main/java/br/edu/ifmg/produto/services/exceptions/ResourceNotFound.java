package br.edu.ifmg.produto.services.exceptions;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound() {
        super();
    }

    public ResourceNotFound(String msg) {
        super(msg);
    }
}