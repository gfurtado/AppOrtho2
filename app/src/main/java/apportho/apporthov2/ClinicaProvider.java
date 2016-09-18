package apportho.apporthov2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


public class ClinicaProvider extends ContentProvider {

    private static final String AUTHORITY = "apportho.apporthov2.clinicaprovider";
    private static final String BASE_PATH = "clinica";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    public static final String CONTENT_ITEM_TYPE = "Clinica";


    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;


    //Paulo Cauca - Nao esquecer de cadastrar no manifest
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    //Aqui
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selArgs, String sortOrder) {
        Log.d("ClinicaProvider", "QUERY : " + SQLiteQueryBuilder.buildQueryString(false,DBOpenHelper.TABLE_CLINICAS,DBOpenHelper.CLINICA_ALL_COLUMNS, selection, null,null,DBOpenHelper.CLINICA_CREATED + " DESC",null));
        return database.query(DBOpenHelper.TABLE_CLINICAS, DBOpenHelper.CLINICA_ALL_COLUMNS, selection, null,null,null,DBOpenHelper.CLINICA_CREATED + " DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = database.insert(DBOpenHelper.TABLE_CLINICAS,null,values);

        return Uri.parse(BASE_PATH+"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_CLINICAS, selection,selectionArgs);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_CLINICAS,values,selection,selectionArgs);
    }
}
