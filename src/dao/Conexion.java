
package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
/**
 * Clase para gestionar la conexión a la base de datos MongoDB Atlas.
 * Implementa el patrón Singleton para garantizar una única instancia
 * de conexión durante la ejecución de la aplicación.
 *
 * @author Odaliz Aracely
 * @version 1.0
 * @since 2024
 */

public class Conexion {

    private static Conexion instancia;
    private MongoClient mongoClient;
    private MongoDatabase baseDatos;

    private Conexion() {
        String uri = "mongodb+srv://Andres2025:1234@cluster0.ljn7k4k.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

        mongoClient = MongoClients.create(uri);
        baseDatos = mongoClient.getDatabase("veterinaria"); // Usa la misma base que en local
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public MongoDatabase getBaseDatos() {
        return baseDatos;
    }
}
