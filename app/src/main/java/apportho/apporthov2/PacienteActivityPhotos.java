package apportho.apporthov2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PacienteActivityPhotos extends AppCompatActivity {

    private String id;
    private Cursor cursor;
    private Cursor photosCursor;
    private String pacienteFilter;
    private String pacientePhotoFilter;
    private TextView txtNomePaciente;
    private Button btnSalvarClinica;
    private Uri file;
    private String pacienteId;
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    String[] perms = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQUEST_CODE = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_photos);

        txtNomePaciente = (TextView) findViewById(R.id.txtNomePaciente);
        btnSalvarClinica = (Button) findViewById(R.id.btnSalvarClinica);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Adicionar Fotos");

        //Recebe o parametro da Paciente Activity
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(PacienteProvider.CONTENT_ITEM_TYPE);

        pacienteFilter = DBOpenHelper.PACIENTE_ID + "=" + uri.getLastPathSegment();
        pacientePhotoFilter = DBOpenHelper.FOTO_PAC_ID + "=" + uri.getLastPathSegment();

        pacienteId = uri.getLastPathSegment();

        cursor = getContentResolver().query(uri, DBOpenHelper.PACIENTE_ALL_COLUMNS, pacienteFilter, null, null);
        cursor.moveToFirst();

        atualizarCursor();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intentDetail = new Intent(PacienteActivityPhotos.this, DetailsActivity.class);
                intentDetail.putExtra("photoId", item.getId());

                startActivity(intentDetail);
            }
        });

        txtNomePaciente.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.PACIENTE_NOME)));
    }

    public void atualizarCursor() {
        Uri uriPhoto = FotoPacienteProvider.CONTENT_URI;
        photosCursor = getContentResolver().query(uriPhoto, DBOpenHelper.FOTO_ALL_COLUMNS, pacientePhotoFilter, null, null);

        ArrayList<ImageItem> fotos = new ArrayList<ImageItem>();
        //photosCursor.moveToFirst();
        if (photosCursor.moveToFirst()) {
            do {
                String id = photosCursor.getString(0);

                Log.d("PacienteActivityPhotos", photosCursor.getString(2));
                //Date date = new SimpleDateFormat("dd/MM/yyyy").parse(photosCursor.getString(2));
                String date = photosCursor.getString(2);
                byte[] byteArray = photosCursor.getBlob(3);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                options.inPreferQualityOverSpeed = true;

                ImageItem item = new ImageItem(Integer.parseInt(id), BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options), date);
                fotos.add(item);
            } while (photosCursor.moveToNext());
        }


        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, fotos);
        gridView.setAdapter(gridAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
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


    // Function para habilidar a camera
    public void onClickTakePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        startActivityForResult(intent, 100);
    }


    private File getOutputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "AppOrthoPhotos");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("AppOrthoPhotos", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {

            if (resultCode == RESULT_OK) {

                String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                ContentValues values = new ContentValues();

                Bitmap bitmap = data.getExtras().getParcelable("data");

                //Bitmap bitmap = BitmapFactory.decodeFile(data.getData().getPath());

                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);

                values.put(DBOpenHelper.FOTO_CREATEDON, timeStamp.toString());
                values.put(DBOpenHelper.FOTO_IMG, blob.toByteArray());
                values.put(DBOpenHelper.FOTO_PAC_ID, pacienteId);
                Uri pacienteUrl = getContentResolver().insert(FotoPacienteProvider.CONTENT_URI, values);
                setResult(RESULT_OK);
                atualizarCursor();
            }

        }
    }
}
