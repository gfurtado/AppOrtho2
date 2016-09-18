package apportho.apporthov2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class ClinicaActivity extends AppCompatActivity {
    protected EditText clinicaEditText;
    private String id;
    private String clinicaFilter;
    private Cursor cursorClinica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinica);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cadastrar Clinica");

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(ClinicaProvider.CONTENT_ITEM_TYPE);

        try{

         //   Log.d("ClinicaActivity", uri.toString());
          //  Log.d("ClinicaActivity", uri.getLastPathSegment());
            id = uri.getLastPathSegment();
            clinicaFilter = DBOpenHelper.CLINICA_ID + "=" + id;
            String[] from  = {DBOpenHelper.CLINICA_NOME};

            cursorClinica = getContentResolver().query(ClinicaProvider.CONTENT_URI,from,clinicaFilter, null,null);
            cursorClinica.moveToFirst();

         //   Log.d("ClinicaActivity", " Cursor  count " + cursorClinica.getString(1).toString());

            clinicaEditText = (EditText) findViewById(R.id.txtAddClinica);
            clinicaEditText.setText(cursorClinica.getString(1));


        }catch (Exception e){
            Log.d("ClinicaActivity", " NAO RECEBI ID VAI SER CLINICA NOVA !!!" + e.getMessage());
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }




    public void OnClickSaveClinica(View view) {


        if(id != null && !id.equals("")){


            ContentValues values = new ContentValues();
            values.put(DBOpenHelper.CLINICA_NOME, clinicaEditText.getText().toString().trim());

            getContentResolver().update(ClinicaProvider.CONTENT_URI, values, clinicaFilter,null);

            Toast.makeText(this, "Clinica " + clinicaEditText.getText() + " alterada com sucesso!", Toast.LENGTH_LONG).show();



        }else {



            clinicaEditText = (EditText) findViewById(R.id.txtAddClinica);

            ContentValues values = new ContentValues();

            values.put(DBOpenHelper.CLINICA_NOME, clinicaEditText.getText().toString().trim());
            Uri clinicaUri = getContentResolver().insert(ClinicaProvider.CONTENT_URI, values);


               Toast.makeText(this, "Clinica " + clinicaEditText.getText() + " adicionada com sucesso!", Toast.LENGTH_LONG).show();
        }
        setResult(RESULT_OK);
        this.finish();
    }


}