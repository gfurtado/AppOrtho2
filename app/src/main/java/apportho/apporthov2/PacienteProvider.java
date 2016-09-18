package apportho.apporthov2;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class PacienteProvider extends ContentProvider {


    private static final String AUTHORITY = "apportho.apporthov2.pacienteprovider";
    private static final String BASE_PATH = "paciente";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    public static final String CONTENT_ITEM_TYPE = "Paciente";


    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final int CLINICA_ID = 3;



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

        if(uriMatcher.match(uri) == CLINICA_ID){
            selection = DBOpenHelper.PAC_CLINICA_ID + "=" + uri.getLastPathSegment();
        }
        return database.query(DBOpenHelper.TABLE_PACIENTES, DBOpenHelper.PACIENTE_ALL_COLUMNS, selection, null,null,null,DBOpenHelper.PACIENTE_CREATED + " DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = database.insert(DBOpenHelper.TABLE_PACIENTES,null,values);
        return Uri.parse(BASE_PATH+"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_PACIENTES, selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_PACIENTES,values,selection,selectionArgs);
    }
}
