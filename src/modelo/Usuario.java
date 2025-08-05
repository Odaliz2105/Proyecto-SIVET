package modelo;
/**
 * Representa un usuario registrado en el sistema.
 * Puede ser un Administrador o Asistente Veterinario.
 * Contiene campos como nombre de usuario, contraseña, y rol.
 *
 * @author Odaliz
 * @version 1.0
 */

public class Usuario {
    private String nombre;
    private String rol;
    private String contraseña;

    public Usuario(String nombre, String rol, String contraseña) {
        this.nombre = nombre;
        this.rol = rol;
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
