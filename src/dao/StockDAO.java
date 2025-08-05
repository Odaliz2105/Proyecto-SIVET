package dao;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class StockDAO {
    private final MongoCollection<Document> coleccion;

    public StockDAO() {
        this.coleccion = Conexion.getInstancia().getBaseDatos().getCollection("stock");  // ✅ Correcto
    }

    // Obtener todos los productos del stock
    public List<Document> obtenerStock() {
        List<Document> lista = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            lista.add(doc);
        }
        return lista;
    }

    // Agregar nuevo producto
    public void agregarProducto(String producto, int cantidad, String unidad) {
        Document doc = new Document("producto", producto)
                .append("cantidad", cantidad)
                .append("unidad", unidad);
        coleccion.insertOne(doc);
    }

    // Eliminar producto por ObjectId
    public void eliminarProductoPorId(String id) {
        coleccion.deleteOne(eq("_id", new ObjectId(id)));
    }

    // Actualizar producto
    public void actualizarProducto(String id, String producto, int cantidad, String unidad) {
        Document actualizacion = new Document("producto", producto)
                .append("cantidad", cantidad)
                .append("unidad", unidad);
        coleccion.updateOne(eq("_id", new ObjectId(id)), new Document("$set", actualizacion));
    }

}
