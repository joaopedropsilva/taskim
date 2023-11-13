package com.example.taskim_2.Handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.taskim_2.Dados.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String name = "taskimdb";
    private static final String table_tarefa = "tarefa";
    private static final String tarefa_id = "id";
    private static final String tarefa_conteudo = "conteudo";
    private static final String tarefa_status = "status";
    private static final String query_create_table_tarefa =
            "create table " + table_tarefa + "(" +
                    tarefa_id + "integer primary key autoincrement," +
                    tarefa_conteudo + "text," +
                    tarefa_status + "bit)";
    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_create_table_tarefa);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("drop table if exists " + table_tarefa);
        onCreate(db);
    }

    public void openDb() {
        db = this.getWritableDatabase();
    }

    public void insertTarefa(Tarefa tarefa) {
        ContentValues record = new ContentValues();
        record.put(tarefa_conteudo, tarefa.getConteudo());
        record.put(tarefa_status, false);

        db.insert(table_tarefa, null, record);
    }

    public List<Tarefa> searchAllTarefa() {
        Cursor queryResult = null;
        List<Tarefa> tarefas = new ArrayList<>();

        db.beginTransaction();

        try {
            queryResult = db.query(table_tarefa, null, null, null, null, null, null);

            if (queryResult != null && queryResult.moveToFirst()) {
                while (queryResult.moveToNext()) {
                    int id = queryResult.getInt(queryResult
                            .getColumnIndexOrThrow(tarefa_id));
                    String conteudo = queryResult.getString(queryResult
                            .getColumnIndexOrThrow(tarefa_conteudo));
                    boolean status = queryResult.getInt(queryResult
                            .getColumnIndexOrThrow(tarefa_status)) > 0;

                    Tarefa t = new Tarefa();
                    t.setId(id);
                    t.setConteudo(conteudo);
                    t.setStatus(status);

                    tarefas.add(t);
                }
            }
        } catch (Exception error) {
            Log.d("Database", "Falha ao ler dados do banco", null);
        } finally {
            db.endTransaction();
            queryResult.close();
        }

        return tarefas;
    }

    public void udpateStatusTarefa(int id_tarefa, boolean status) {
        ContentValues record = new ContentValues();
        record.put(tarefa_status, status);

        db.update(table_tarefa,
                    record,
                    tarefa_id + "=?",
                    new String[] { String.valueOf(id_tarefa) });
    }

    public void udpateConteudoTarefa(int id_tarefa, String conteudo) {
        ContentValues record = new ContentValues();
        record.put(tarefa_conteudo, conteudo);

        db.update(table_tarefa,
                record,
                tarefa_id + "=?",
                new String[] { String.valueOf(id_tarefa) });
    }

    public void deleteTarefa(int id_tarefa) {
        db.delete(table_tarefa, tarefa_id + "=?", new String[] { String.valueOf(id_tarefa) });
    }
}
