package com.example.taskim.Handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.taskim.Dados.Lista;
import com.example.taskim.Dados.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String name = "taskimdb";
    private static final String table_lista = "lista";
    private static final String lista_id = "id";
    private static final String lista_nome = "nome";
    private static final String lista_status = "status";
    private static final String query_create_table_lista =
            "create table " + table_lista + "(" +
                    lista_id + " integer primary key autoincrement," +
                    lista_nome + " text," +
                    lista_status + " bit)";
    private static final String table_tarefa = "tarefa";
    private static final String tarefa_id = "id";
    private static final String tarefa_conteudo = "conteudo";
    private static final String tarefa_status = "status";
    private static final String lista_id_fk_column = "id_lista";
    private static final String query_create_table_tarefa =
            "create table " + table_tarefa + "(" +
                    tarefa_id + " integer primary key autoincrement," +
                    tarefa_conteudo + " text," +
                    tarefa_status + " bit," +
                    lista_id_fk_column + " integer," +
                    "foreign key(" + lista_id_fk_column + ") references " +
                    table_lista + "(" + lista_id + ") on delete cascade)";

    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_create_table_lista);
        db.execSQL(query_create_table_tarefa);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("drop table if exists " + table_tarefa);
        db.execSQL("drop table if exists " + table_lista);
        onCreate(db);
    }

    public void openDb() {
        this.db = this.getWritableDatabase();
    }

    public void insertTarefa(Tarefa tarefa, int idLista) {
        ContentValues record = new ContentValues();
        record.put(tarefa_conteudo, tarefa.getConteudo());
        record.put(tarefa_status, false);
        record.put(lista_id_fk_column, idLista);

        db.insert(table_tarefa, null, record);
    }

    public List<Tarefa> searchAllTarefaByIdLista(int idLista) {
        Cursor queryResult = null;
        List<Tarefa> tarefas = new ArrayList<>();

        db.beginTransaction();

        try {
            queryResult = db.query(
                    table_tarefa,
                    null,
                    lista_id_fk_column + "=" + idLista,
                    null, null, null, null);

            if (queryResult != null) {
                boolean isQueryResultNotEmpty = queryResult.moveToFirst();

                if (isQueryResultNotEmpty) {
                    do {
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
                    } while (queryResult.moveToNext());
                }
            }
        } catch (Exception error) {
            Log.d("Database", error.getMessage(), null);
        } finally {
            db.endTransaction();

            if (queryResult != null) {
                queryResult.close();
            }
        }

        return tarefas;
    }

    public void udpateStatusTarefa(int idTarefa, boolean status) {
        ContentValues record = new ContentValues();
        record.put(tarefa_status, status);

        db.update(table_tarefa,
                    record,
                    tarefa_id + "=?",
                    new String[] { String.valueOf(idTarefa) });
    }

    public void udpateConteudoTarefa(int idTarefa, String conteudo) {
        ContentValues record = new ContentValues();
        record.put(tarefa_conteudo, conteudo);

        db.update(table_tarefa,
                record,
                tarefa_id + "=?",
                new String[] { String.valueOf(idTarefa) });
    }

    public void deleteTarefa(int idTarefa) {
        db.delete(table_tarefa, tarefa_id + "=?", new String[] { String.valueOf(idTarefa) });
    }

    public void insertLista(Lista lista) {
        ContentValues record = new ContentValues();
        record.put(lista_nome, lista.getNome());
        record.put(lista_status, false);

        db.insert(table_lista, null, record);
    }

    public List<Lista> searchAllListas() {
        Cursor queryResult = null;
        List<Lista> listas = new ArrayList<>();

        db.beginTransaction();

        try {
            queryResult = db.query(table_lista, null, null, null, null, null, null);

            if (queryResult != null) {
                boolean isQueryResultNotEmpty = queryResult.moveToFirst();

                if (isQueryResultNotEmpty) {
                    do {
                        int id = queryResult.getInt(queryResult
                                .getColumnIndexOrThrow(lista_id));
                        String nome = queryResult.getString(queryResult
                                .getColumnIndexOrThrow(lista_nome));
                        boolean status = queryResult.getInt(queryResult
                                .getColumnIndexOrThrow(lista_status)) > 0;

                        Lista l = new Lista();
                        l.setId(id);
                        l.setNome(nome);
                        l.setStatus(status);

                        listas.add(l);
                    } while (queryResult.moveToNext());
                }
            }
        } catch (Exception error) {
            Log.d("Database", error.getMessage(), null);
        } finally {
            db.endTransaction();

            if (queryResult != null) {
                queryResult.close();
            }
        }

        return listas;
    }

    public void udpateStatusLista(int idLista, boolean status) {
        ContentValues record = new ContentValues();
        record.put(lista_status, status);

        db.update(table_lista,
                record,
                lista_id + "=?",
                new String[] { String.valueOf(idLista) });
    }

    public void updateNomeLista(int idLista, String nome) {
        ContentValues record = new ContentValues();
        record.put(lista_nome, nome);

        db.update(table_lista,
                record,
                lista_id + "=?",
                new String[] { String.valueOf(idLista) });
    }

    public void deleteLista(int idLista) {
        db.delete(table_lista, lista_id + "=?", new String[] { String.valueOf(idLista) });
    }
}
