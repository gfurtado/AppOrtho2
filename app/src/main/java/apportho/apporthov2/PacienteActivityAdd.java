package apportho.apporthov2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PacienteActivityAdd extends AppCompatActivity {

    protected EditText txtNomePaciente;
    protected EditText txtEmailPaciente;
    protected EditText txtTelPaciente;
    protected EditText idClinicaPaciente;
    private String pacienteFilter;
    private String id;
    private Cursor cursorPaciente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_add);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cadastro de Paciente");

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(PacienteProvider.CONTENT_ITEM_TYPE);

       // Log.d("PacienteActivityAdd", "|" + intent.getStringExtra("ID") + "|");
       // Log.d("PacienteActivityAdd", "intent : " +  intent.getParcelableExtra(PacienteProvider.CONTENT_ITEM_TYPE));
       // Log.d("PacienteActivityAdd", "getLastPathSegment : " +  uri.getLastPathSegment());


        if(intent.getStringExtra("ID").equals("PACIENTE")){
            //Log.d("PacienteActivityAdd","Entrou no IF");
            try{

               // id = uri.getLastPathSegment();
                pacienteFilter = DBOpenHelper.PACIENTE_ID + "=" + uri.getLastPathSegment();
                String[] from  = DBOpenHelper.PACIENTE_ALL_COLUMNS;
                cursorPaciente = getContentResolver().query(PacienteProvider.CONTENT_URI,from,pacienteFilter, null,null);
                cursorPaciente.moveToFirst();

                txtNomePaciente = (EditText) findViewById(R.id.txtNomePaciente);
                txtEmailPaciente = (EditText) findViewById(R.id.txtEmailPaciente);
                txtTelPaciente = (EditText) findViewById(R.id.txtTelPaciente);
                idClinicaPaciente = (EditText) findViewById(R.id.idClinicaPaciente);


                id = cursorPaciente.getString(0);
                idClinicaPaciente.setText(cursorPaciente.getString(1));
                txtNomePaciente.setText(cursorPaciente.getString(2));
                txtEmailPaciente.setText(cursorPaciente.getString(3));
                txtTelPaciente.setText(cursorPaciente.getString(4));


                idClinicaPaciente.setVisibility(View.GONE);

             }catch (Exception e){
                Log.d("PacienteActivityAdd", " ERRO : " + e.getMessage());
            }


        }else{
            idClinicaPaciente = (EditText) findViewById(R.id.idClinicaPaciente);
            idClinicaPaciente.setText(uri.getLastPathSegment());
            idClinicaPaciente.setVisibility(View.GONE);
        }

    }

    public void OnClickSavePaciente(View view){

        if(id != null && !id.equals("")){

            ContentValues values = new ContentValues();

            values.put(DBOpenHelper.PACIENTE_NOME, txtNomePaciente.getText().toString().trim());
            values.put(DBOpenHelper.PACIENTE_EMAIL, txtEmailPaciente.getText().toString().trim());
            values.put(DBOpenHelper.PACIENTE_TELEFONE, txtTelPaciente.getText().toString().trim());

            getContentResolver().update(PacienteProvider.CONTENT_URI, values, pacienteFilter,null);

            Toast.makeText(this, "Paciente " + txtNomePaciente.getText() + " alterada com sucesso!", Toast.LENGTH_LONG).show();



        }else {


            txtNomePaciente = (EditText) findViewById(R.id.txtNomePaciente);
            txtEmailPaciente = (EditText) findViewById(R.id.txtEmailPaciente);
            txtTelPaciente = (EditText) findViewById(R.id.txtTelPaciente);

            idClinicaPaciente = (EditText) findViewById(R.id.idClinicaPaciente);

            ContentValues values = new ContentValues();

            values.put(DBOpenHelper.PACIENTE_NOME, txtNomePaciente.getText().toString().trim());
            values.put(DBOpenHelper.PACIENTE_EMAIL, txtEmailPaciente.getText().toString().trim());
            values.put(DBOpenHelper.PACIENTE_TELEFONE, txtTelPaciente.getText().toString().trim());
            values.put(DBOpenHelper.PAC_CLINICA_ID, Integer.parseInt(idClinicaPaciente.getText().toString()));


            Uri clinicaUri = getContentResolver().insert(PacienteProvider.CONTENT_URI, values);

            Toast.makeText(this, "Paciente " + txtNomePaciente.getText() + " adicionado com sucesso!", Toast.LENGTH_LONG).show();
        }
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }


}
