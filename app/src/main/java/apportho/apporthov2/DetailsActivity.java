package apportho.apporthov2;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by guilh on 16/09/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Foto");

        String photoId = getIntent().getExtras().get("photoId").toString();

        ImageItem item = getImage(Integer.parseInt(photoId));

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(item.getDate());

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(item.getImage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public ImageItem getImage(int photoId)
    {
        String pacientePhotoFilter;
        Cursor photosCursor;

        pacientePhotoFilter = DBOpenHelper.FOTO_ID + " = " + photoId;
        Uri uriPhoto = FotoPacienteProvider.CONTENT_URI;
        photosCursor = getContentResolver().query(uriPhoto,DBOpenHelper.FOTO_ALL_COLUMNS,pacientePhotoFilter, null,null);

        ArrayList<ImageItem> fotos = new ArrayList<ImageItem>();
        for(photosCursor.moveToFirst(); !photosCursor.isAfterLast(); photosCursor.moveToNext())
        {
            int id = photosCursor.getInt(0);
            String date = photosCursor.getString(2);
            byte[] byteArray = photosCursor.getBlob(3);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inPreferQualityOverSpeed = true;

            ImageItem item = new ImageItem(id, BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length,options), date);
            fotos.add(item);
        }

        return fotos.get(0);
    }
}