
package dao;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
/**
 * Clase DAO para gestionar las operaciones CRUD de mascotas.
 * Maneja el registro, consulta y actualización de información de mascotas.
 *
 * @author Odaliz Aracely
 * @version 1.0
 * @since 2024
 */
public class MascotaDAO {
    private MongoCollection<Document> coleccion;

    public MascotaDAO() {
        this.coleccion = Conexion.getInstancia().getBaseDatos().getCollection("mascotas");
    }

    public Document obtenerMascotaPorId(String id) {
        return coleccion.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    public List<Document> obtenerTodasLasMascotas() {
        List<Document> mascotas = new ArrayList<>();
        coleccion.find().into(mascotas);
        return mascotas;
    }

    public void crearMascota(String nombre, String especie, String propietario) {
        Document mascota = new Document()
                .append("nombre", nombre)
                .append("especie", especie)
                .append("propietario", propietario)
                .append("fecha_registro", new java.util.Date());
        coleccion.insertOne(mascota);
    }

    public String obtenerNombrePropietarioPorId(ObjectId id) {
        MongoCollection<Document> coleccionPropietarios = Conexion.getInstancia().getBaseDatos().getCollection("propietarios");
        Document doc = coleccionPropietarios.find(Filters.eq("_id", id)).first();
        return (doc != null) ? doc.getString("nombre") : "Desconocido";
    }

    public boolean actualizarCamposMascota(String idHex, String especie, String raza, String sexo) {
        try {
            ObjectId id = new ObjectId(idHex);
            Document actualizacion = new Document()
                    .append("especie", especie)
                    .append("raza", raza)
                    .append("sexo", sexo);

            coleccion.updateOne(Filters.eq("_id", id), new Document("$set", actualizacion));
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar mascota: " + e.getMessage());
            return false;
        }
    }


}