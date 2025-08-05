
package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import modelo.Cita;
import org.bson.Document;

/**
 * Clase DAO para generar reportes y estadísticas del sistema veterinario.
 * Proporciona métodos para obtener información analítica y reportes.
 *
 * @author Odaliz Aracely
 * @version 1.0
 * @since 2024
 */
public class ReporteDAO {

    private final MongoCollection<Document> coleccion;

    public ReporteDAO() {
        MongoDatabase db = Conexion.getInstancia().getBaseDatos();
        this.coleccion = db.getCollection("reportes");
    }

    public void guardarCitaComoReporte(Cita cita) {
        try {
            // Verificar si ya existe un reporte igual
            Document existente = coleccion.find(
                    Filters.and(
                            Filters.eq("propietario", cita.getPropietario()),
                            Filters.eq("mascota", cita.getMascota()),
                            Filters.eq("fecha", cita.getFecha()),
                            Filters.eq("hora", cita.getHora())
                    )
            ).first();

            // Si no existe, lo insertamos
            if (existente == null) {
                Document doc = new Document()
                        .append("propietario", cita.getPropietario())
                        .append("mascota", cita.getMascota())
                        .append("fecha", cita.getFecha())
                        .append("hora", cita.getHora())
                        .append("motivo", cita.getMotivo())
                        .append("estado", cita.getEstado());

                coleccion.insertOne(doc);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el reporte en MongoDB", e);
        }
    }

}
