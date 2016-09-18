package apportho.apporthov2;

import java.util.Date;

/**
 * Created by guilh on 12/09/2016.
 */
public class FotoPaciente
{
    public int pacienteId;

    public int getPaciente() {
        return pacienteId;
    }

    public void setPaciente(int pacienteId) {
        this.pacienteId = pacienteId;
    }

    public byte[] picture;

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        CreatedOn = createdOn;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Date CreatedOn;
}
