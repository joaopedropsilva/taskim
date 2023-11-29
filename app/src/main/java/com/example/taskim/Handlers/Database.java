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

// Classe de handler responsável pelo manejo
// do banco de dados utilizando o SQLiteOpenHelper
public class Database extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String name = "taskimdb";
    private static final String table_lista = "lista";
    private static final String lista_id = "id";
    private static final String lista_nome = "nome";
    private static final String query_create_table_lista =
            "create table " + table_lista + "(" +
                    lista_id + " integer primary key autoincrement," +
                    lista_nome + " text)";
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

    // Construtor do handler
    public Database(Context context) {
        super(context, name, null, version);
    }

    // Método para configurações do banco de dados
    // A ativação do uso de chave estrangeira nas
    // tabelas é adicionada nessa configuração
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Método executado quando o banco de
    // dados é criado pela primeira vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_create_table_lista);
        db.execSQL(query_create_table_tarefa);
    }

    // Método executado quando o banco
    // precisa ser atualizado
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("drop table if exists " + table_tarefa);
        db.execSQL("drop table if exists " + table_lista);
        onCreate(db);
    }

    // Abre (inicializa) o banco de dados a ser utilizado
    public void openDb() { db = this.getWritableDatabase(); }

    // Inserção de novas tarefas
    public void insertTarefa(Tarefa tarefa, int idLista) {
        // Uso de um ContentValues para pouplar
        // o registro de acordo com os parâmetros recebidos
        ContentValues record = new ContentValues();
        record.put(tarefa_conteudo, tarefa.getConteudo());
        record.put(tarefa_status, false);
        record.put(lista_id_fk_column, idLista);

        db.insert(table_tarefa, null, record);
    }

    // Busca de todas as tarefas por id de uma lista de tarefas
    public List<Tarefa> searchAllTarefaByIdLista(int idLista) {
        // Criação de um cursor para a execução da query
        Cursor queryResult = null;

        // Tarefas a serem retornadas
        List<Tarefa> tarefas = new ArrayList<>();

        // Iniciação de uma transação no banco de dados
        db.beginTransaction();

        try {
            // Execução da query de busca
            queryResult = db.query(
                    table_tarefa,
                    null,
                    lista_id_fk_column + "=" + idLista,
                    null, null, null, null);

            // Verificação se a busca retornou
            // alguma coisa diferente de null
            if (queryResult != null) {
                // Verificação do resultado da query,
                // se está ou não vazio
                boolean isQueryResultNotEmpty = queryResult.moveToFirst();

                // Caso não esteja, os objetos a serem
                // retornados são populados com os
                // valores do banco de dados
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
            // Log para alguma exceção ocorrida
            Log.d("Database", error.getMessage(), null);
        } finally {
            // Finalização da transação
            db.endTransaction();

            // Fechamento do cursor se
            // não estiver nulo
            if (queryResult != null) {
                queryResult.close();
            }
        }

        return tarefas;
    }

    // Atualização no status da tarefa
    public void udpateStatusTarefa(int idTarefa, boolean status) {
        // Uso de um ContentValues para pouplar
        // o registro de acordo com os parâmetros recebidos
        ContentValues record = new ContentValues();
        record.put(tarefa_status, status);

        db.update(table_tarefa,
                    record,
                    tarefa_id + "=?",
                    new String[] { String.valueOf(idTarefa) });
    }

    // Atualização no conteúdo da tarefa
    public void udpateConteudoTarefa(int idTarefa, String conteudo) {
        // Uso de um ContentValues para pouplar
        // o registro de acordo com os parâmetros recebidos
        ContentValues record = new ContentValues();
        record.put(tarefa_conteudo, conteudo);

        db.update(table_tarefa,
                record,
                tarefa_id + "=?",
                new String[] { String.valueOf(idTarefa) });
    }

    // Deleção de uma tarefa
    public void deleteTarefa(int idTarefa) {
        db.delete(table_tarefa,
                tarefa_id + "=?",
                new String[] { String.valueOf(idTarefa) });
    }

    // Inserção de novas listas de tarefas
    public void insertLista(Lista lista) {
        // Uso de um ContentValues para pouplar
        // o registro de acordo com os parâmetros recebidos
        ContentValues record = new ContentValues();
        record.put(lista_nome, lista.getNome());

        db.insert(table_lista, null, record);
    }

    // Busca de todas as listas de tarefa
    public List<Lista> searchAllListas() {
        // Criação de um cursor para a execução da query
        Cursor queryResult = null;

        // Listas de tarefas a serem retornadas
        List<Lista> listas = new ArrayList<>();

        // Iniciação de uma transação no banco de dados
        db.beginTransaction();

        try {
            // Execução da query de busca
            queryResult = db.query(
                    table_lista,
                    null, null,
                    null, null,
                    null, null);

            // Verificação se a busca retornou
            // alguma coisa diferente de null
            if (queryResult != null) {
                // Verificação do resultado da query,
                // se está ou não vazio
                boolean isQueryResultNotEmpty = queryResult.moveToFirst();

                // Caso não esteja, os objetos a serem
                // retornados são populados com os
                // valores do banco de dados
                if (isQueryResultNotEmpty) {
                    do {
                        int id = queryResult.getInt(queryResult
                                .getColumnIndexOrThrow(lista_id));
                        String nome = queryResult.getString(queryResult
                                .getColumnIndexOrThrow(lista_nome));

                        Lista l = new Lista();
                        l.setId(id);
                        l.setNome(nome);

                        listas.add(l);
                    } while (queryResult.moveToNext());
                }
            }
        } catch (Exception error) {
            // Log para alguma exceção ocorrida
            Log.d("Database", error.getMessage(), null);
        } finally {
            // Finalização da transação
            db.endTransaction();

            // Fechamento do cursor se
            // não estiver nulo
            if (queryResult != null) {
                queryResult.close();
            }
        }

        return listas;
    }

    // Atualização no nome da lista de tarefas
    public void updateNomeLista(int idLista, String nome) {
        // Uso de um ContentValues para pouplar
        // o registro de acordo com os parâmetros recebidos
        ContentValues record = new ContentValues();
        record.put(lista_nome, nome);

        db.update(table_lista,
                record,
                lista_id + "=?",
                new String[] { String.valueOf(idLista) });
    }

    // Deleção de uma lista de tarefas
    public void deleteLista(int idLista) {
        db.delete(table_lista,
                lista_id + "=?",
                new String[] { String.valueOf(idLista) });
    }
}
