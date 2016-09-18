package apportho.apporthov2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "appOrthoDB.sqlite";
    private static final int DB_VERSION = 1;


    public static final String TABLE_CLINICAS = "clinicas";
    public static final String TABLE_PACIENTES = "pacientes";
    public static final String TABLE_FOTOS = "fotos";

    public static final String CLINICA_ID = "_id";
    public static final String CLINICA_NOME = "clinicaNome";
    public static final String CLINICA_CREATED = "clinicaCreated";

    public static final String PACIENTE_ID = "_id";
    public static final String PAC_CLINICA_ID = "id_clinica";
    public static final String PACIENTE_NOME = "pacienteNome";
    public static final String PACIENTE_TELEFONE = "pacienteTelefone";
    public static final String PACIENTE_EMAIL= "pacienteEmail";
    public static final String PACIENTE_CREATED = "pacienteCreated";

    public static final String FOTO_ID = "_id";
    public static final String FOTO_PAC_ID = "id_paciente";
    public static final String FOTO_CREATEDON = "fotoCreatedOn";
    public static final String FOTO_IMG = "fotoIMG";

    public static final String[] CLINICA_ALL_COLUMNS = {CLINICA_ID,CLINICA_NOME};
    public static final String[] PACIENTE_ALL_COLUMNS = {PACIENTE_ID,PAC_CLINICA_ID,PACIENTE_NOME,PACIENTE_EMAIL,PACIENTE_TELEFONE};
    public static final String[] FOTO_ALL_COLUMNS = {FOTO_ID,FOTO_PAC_ID,FOTO_CREATEDON,FOTO_IMG};

    private static final String TABLE_CLINICA_CREATE =
            "CREATE TABLE " + TABLE_CLINICAS + " (" +
                    CLINICA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CLINICA_NOME + " TEXT, " +
                    CLINICA_CREATED + " TIMESTAMP default CURRENT_TIMESTAMP" +
                    ")";

    private static  final String TABLE_FOTOS_CREATE =
            "CREATE TABLE " + TABLE_FOTOS + " (" +
                    FOTO_ID +       " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FOTO_PAC_ID +    " INTEGER," +
                    FOTO_CREATEDON +     " DATE, " +
                    FOTO_IMG + " BLOB, " +
                    " FOREIGN KEY (" + FOTO_PAC_ID + ") REFERENCES "+TABLE_PACIENTES+"("+PACIENTE_ID+"));";

    private static  final String TABLE_PACIENTE_CREATE =
            "CREATE TABLE " + TABLE_PACIENTES + " (" +
                    PACIENTE_ID +       " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PAC_CLINICA_ID +    " INTEGER," +
                    PACIENTE_NOME +     " TEXT, " +
                    PACIENTE_TELEFONE + " TEXT, " +
                    PACIENTE_EMAIL +    " TEXT, " +
                    PACIENTE_CREATED +  " TIMESTAMP default CURRENT_TIMESTAMP," +
                    " FOREIGN KEY (" + PAC_CLINICA_ID + ") REFERENCES "+TABLE_CLINICAS+"("+CLINICA_ID+"));";



    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CLINICA_CREATE);
        db.execSQL(TABLE_PACIENTE_CREATE);
        db.execSQL(TABLE_FOTOS_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion) {

    }


}
