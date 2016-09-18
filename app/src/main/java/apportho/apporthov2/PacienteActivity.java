package apportho.apporthov2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

//Teste 2
public class PacienteActivity extends AppCompatActivity {
    private CursorAdapter cursorAdapter;
    private String clinicaFilter;
    private String pacienteID;
    private String pacienteFilter;
    private String id;
    private Cursor cursor;
    private ListView listView;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);


        builder = new AlertDialog.Builder(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Paciente");

        String[] from  = {DBOpenHelper.PACIENTE_NOME};
        int[] to = {android.R.id.text1};


        atualizaCursor();

        cursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor, from, to, 0);

        listView = (ListView) findViewById(R.id.list_paciente);
        listView.setAdapter(cursorAdapter);

        //Passa o id do paciente para o PacienteActivityPhotos
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PacienteActivity.this,PacienteActivityPhotos.class);

                //Passa o ID da clinica para paciente
                Uri uri = Uri.parse(PacienteProvider.CONTENT_URI + "/" + id);
                intent.putExtra(PacienteProvider.CONTENT_ITEM_TYPE, uri);

                startActivityForResult(intent, 2);
            }
        });




        // Deleta paciente com Long Press
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Seleciona paciente

                pacienteFilter = DBOpenHelper.PACIENTE_ID + "=" + l;
                pacienteID = Long.toString(l);

                builder.setTitle("Pacientes");
                builder.setMessage("Qual ação você gostaria de realizar ?")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getContentResolver().delete(PacienteProvider.CONTENT_URI,pacienteFilter, null);

                                atualizaCursor();

                                cursorAdapter.swapCursor(cursor);
                                cursorAdapter.notifyDataSetChanged();
                                Toast.makeText(PacienteActivity.this,"Paciente deletado com sucesso!", Toast.LENGTH_SHORT).show();


                            }
                        })
                        .setNeutralButton("Editar", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        Intent intent = new Intent(PacienteActivity.this, PacienteActivityAdd.class);

                                        //Passa o ID da clinica para update
                                        Uri uri = Uri.parse(PacienteProvider.CONTENT_URI + "/" + pacienteID);
                                        intent.putExtra(PacienteProvider.CONTENT_ITEM_TYPE, uri);
                                        intent.putExtra("ID","PACIENTE");

                                        startActivityForResult(intent, 1);
                                    }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.paciente_menu,menu);
        return true;
    }

    public void createNewPaciente(View v){

        Intent intent = new Intent(this, PacienteActivityAdd.class);

        //Passa o ID da clinica para o paciente
        Uri uri = Uri.parse(PacienteProvider.CONTENT_URI + "/" + id);
        intent.putExtra(PacienteProvider.CONTENT_ITEM_TYPE, uri);
        intent.putExtra("ID","CLINICA");

        startActivityForResult(intent,1);

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

    protected void atualizaCursor(){

        //Recebe o parametro da MainActivity
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(PacienteProvider.CONTENT_ITEM_TYPE);
        id = uri.getLastPathSegment();

        clinicaFilter = DBOpenHelper.PAC_CLINICA_ID + "=" + uri.getLastPathSegment();

        cursor = getContentResolver().query(uri,DBOpenHelper.PACIENTE_ALL_COLUMNS,clinicaFilter, null,null);
        cursor.moveToFirst();

        cursor = getContentResolver().query(uri,DBOpenHelper.PACIENTE_ALL_COLUMNS,clinicaFilter, null,null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){

                atualizaCursor();

                cursorAdapter.swapCursor(cursor);
                cursorAdapter.notifyDataSetChanged();

                Log.d("PacienteActivity", "Notificando o adapter de paciente ...");

            }
        }
    }



}
