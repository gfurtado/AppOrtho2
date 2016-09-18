package apportho.apporthov2;

public class Paciente {

    public int id;
    public int id_clinica;
    public String nome;
    public String email;
    public String telefone;

    public Paciente(int id, int id_clinica, String nome, String email, String telefone) {
        this.id = id;
        this.id_clinica = id_clinica;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public Paciente() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_clinica() {
        return id_clinica;
    }

    public void setId_clinica(int id_clinica) {
        this.id_clinica = id_clinica;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
