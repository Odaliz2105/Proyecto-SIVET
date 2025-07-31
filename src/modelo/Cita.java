package modelo;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;

public class Cita {
    private int id;
    private String propietario;
    private String mascota;
    private Date fecha;
    private String hora;
    private String motivo;
    private String estado; // "Agendada", "Completada", "Cancelada"
    private static int contadorId = 1;

    // Constructor
    public Cita(String propietario, String mascota, Date fecha, String hora, String motivo) {
        this.id = contadorId++;
        this.propietario = propietario;
        this.mascota = mascota;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = "Agendada";
    }

    // Constructor completo (para cargar desde BD)
    public Cita(int id, String propietario, String mascota, Date fecha, String hora, String motivo, String estado) {
        this.id = id;
        this.propietario = propietario;
        this.mascota = mascota;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para formatear la fecha
    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }

    // Método toString para mostrar la cita
    @Override
    public String toString() {
        return "Cita #" + id +
                "\nPropietario: " + propietario +
                "\nMascota: " + mascota +
                "\nFecha: " + getFechaFormateada() +
                "\nHora: " + hora +
                "\nMotivo: " + motivo +
                "\nEstado: " + estado;
    }

    // Método para representación en tabla
    public Object[] toTableRow() {
        return new Object[]{
                id,
                propietario,
                mascota,
                getFechaFormateada(),
                hora,
                motivo,
                estado
        };
    }

}