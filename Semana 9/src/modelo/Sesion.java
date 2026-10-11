package modelo;
/** Identidad autenticada; los servicios vuelven a consultar el rol antes de operar. */
public record Sesion(Usuario usuario) { }
