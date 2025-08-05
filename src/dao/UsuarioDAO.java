package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import modelo.Usuario;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
/**
 * Acceso a datos para la entidad Usuario.
 * Gestiona la verificación de credenciales, registro y roles de usuarios.
 * Interactúa con la base de datos para validar inicio de sesión.
 *
 * @author Odaliz
 * @version 1.0
 */

public class UsuarioDAO {

    private MongoCollection<Document> coleccionUsuarios;

    public UsuarioDAO() {
        MongoDatabase db = Conexion.getInstancia().getBaseDatos();
        coleccionUsuarios = db.getCollection("usuarios");
    }

    public boolean crearUsuario(Usuario usuario) {
        long conteo = coleccionUsuarios.countDocuments(eq("rol", usuario.getRol()));

        if (usuario.getRol().equals("Administrador") && conteo >= 2) {
            return false; // Máximo 2 admins
        } else if (usuario.getRol().equals("Asistente Veterinario") && conteo >= 10) {
            return false; // Máximo 10 asistentes
        }

        Document doc = new Document("rol", usuario.getRol())
                .append("nombre", usuario.getNombre())
                .append("contraseña", usuario.getContraseña());

        coleccionUsuarios.insertOne(doc);
        return true;
    }


    public boolean validarCredenciales(String rol, String contraseña) {
        Document doc = coleccionUsuarios.find(and(eq("rol", rol), eq("contraseña", contraseña))).first();
        return doc != null;
    }

    public boolean existeUsuario(String rol) {
        Document doc = coleccionUsuarios.find(eq("rol", rol)).first();
        return doc != null;
    }

    public boolean autenticar(String rol, String password) {
        Document usuario = coleccionUsuarios.find(
                and(
                        eq("rol", rol),
                        eq("contraseña", password)
                )
        ).first();

        return usuario != null;
    }

    public Document obtenerUsuarioPorRol(String rol) {
        return coleccionUsuarios.find(eq("rol", rol)).first();
    }

    public Document obtenerUsuarioPorUsuario(String usuario) {
        return coleccionUsuarios.find(eq("usuario", usuario)).first();
    }

    public String obtenerNombrePorRolYContraseña(String rol, String contraseña) {
        Document doc = coleccionUsuarios.find(and(eq("rol", rol), eq("contraseña", contraseña))).first();
        return (doc != null) ? doc.getString("nombre") : null;
    }

}
