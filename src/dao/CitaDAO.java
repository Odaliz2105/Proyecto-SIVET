package dao;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import modelo.Cita;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class CitaDAO {
    private MongoCollection<Document> coleccion;

    public CitaDAO() {
        this.coleccion = Conexion.getInstancia().getBaseDatos().getCollection("citaAsistente");
    }

    public void guardarCita(Cita cita) {
        try {
            Document doc = new Document()
                    .append("propietario", cita.getPropietario())
                    .append("mascota", cita.getMascota())
                    .append("fecha", cita.getFecha())
                    .append("hora", cita.getHora())
                    .append("motivo", cita.getMotivo())
                    .append("estado", cita.getEstado());

            coleccion.insertOne(doc);
            cita.setId(doc.getObjectId("_id").toHexString());
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la cita en MongoDB", e);
        }
    }

    public ArrayList<Cita> obtenerTodas() {
        ArrayList<Cita> citas = new ArrayList<>();
        try {
            FindIterable<Document> resultados = coleccion.find();

            for (Document doc : resultados) {
                Cita cita = new Cita(
                        doc.getObjectId("_id").toHexString(),
                        doc.getString("propietario"),
                        doc.getString("mascota"),
                        doc.getDate("fecha"),
                        doc.getString("hora"),
                        doc.getString("motivo"),
                        doc.getString("estado")
                );
                citas.add(cita);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener citas de MongoDB", e);
        }
        return citas;
    }

    public boolean eliminarCita(String id) {
        try {
            return coleccion.deleteOne(Filters.eq("_id", new ObjectId(id))).getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar cita de MongoDB", e);
        }
    }
}