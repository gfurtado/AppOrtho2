package apportho.apporthov2;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cursorAdapter;
    private AlertDialog.Builder builder;
    private String clinicaFilter;
    private String clinicaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Clinicas");

        builder = new AlertDialog.Builder(this);

        String[] from  = {DBOpenHelper.CLINICA_NOME};
        int[] to = {android.R.id.text1};


        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);

        ListView listView  = (ListView) findViewById(R.id.list_clinica);
        listView.setAdapter(cursorAdapter);

        //Chama ao clicar em uma clinica
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,PacienteActivity.class);

                //Passa o ID da clinica para paciente
                Uri uri = Uri.parse(PacienteProvider.CONTENT_URI + "/" + id);
                intent.putExtra(PacienteProvider.CONTENT_ITEM_TYPE, uri);

                startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Seleciona paciente

                clinicaFilter = DBOpenHelper.CLINICA_ID + "=" + l;
                clinicaID = Long.toString(l);

                builder.setTitle("Clinica");

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

                                getContentResolver().delete(ClinicaProvider.CONTENT_URI,clinicaFilter, null);

                                //Restart Loader
                                getLoaderManager().restartLoader(0,null,MainActivity.this);
                                Toast.makeText(MainActivity.this,"Clinica deletada com sucesso!",Toast.LENGTH_SHORT).show();


                            }
                        }) // Chama Activity da clinica passando o ID da clinica
                        .setNeutralButton("Editar", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Intent intent = new Intent(MainActivity.this, ClinicaActivity.class);

                                //Passa o ID da clinica para update
                                Uri uri = Uri.parse(ClinicaProvider.CONTENT_URI + "/" + clinicaID);
                                intent.putExtra(ClinicaProvider.CONTENT_ITEM_TYPE, uri);
                                //   intent.putExtra("CLINICANAME",);

                                startActivityForResult(intent,1);
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();


                return true;
            }
        });


        getLoaderManager().initLoader(0,null,this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.clinica_menu,menu);
        return true;
    }

    public void createNewClinica(View v){

        Intent intent = new Intent(this, ClinicaActivity.class);
        startActivityForResult(intent,1);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//
//            case R.id.addClinica:
//                Intent intent = new Intent(this, ClinicaActivity.class);
//                startActivityForResult(intent,1);
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,ClinicaProvider.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                getLoaderManager().restartLoader(0,null,this);

                Log.d("MainActivity", "Notificando o adapter ...");

            }
        }
    }
}
