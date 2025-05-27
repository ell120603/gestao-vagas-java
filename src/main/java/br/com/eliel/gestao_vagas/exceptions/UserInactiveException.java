package br.com.eliel.gestao_vagas.exceptions;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException() {
        super("Usuário está inativo. Entre em contato com o administrador para reativar sua conta.");
    }
} 