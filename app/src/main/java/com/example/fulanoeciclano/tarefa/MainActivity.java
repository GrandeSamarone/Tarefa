package com.example.fulanoeciclano.tarefa;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fulanoeciclano.tarefa.dao.AlunoDAO;
import com.example.fulanoeciclano.tarefa.modelo.Aluno;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listaalunos;
    private Button botaonovoaluno;
    private Intent intentvaiproformulario;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaalunos= (ListView) findViewById(R.id.listanome);

        listaalunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno)  listaalunos.getItemAtPosition(position);
               Intent intentvaiproformulario = new Intent(MainActivity.this,FormularioActivity.class);
                intentvaiproformulario.putExtra("aluno",aluno);
                startActivity(intentvaiproformulario);
            }
        });
       listaalunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               Toast.makeText(MainActivity.this, "clique longo", Toast.LENGTH_SHORT).show();
               return false;
           }
       });

        botaonovoaluno = (Button) findViewById(R.id.btnadd);
        botaonovoaluno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               intentvaiproformulario=new Intent(MainActivity.this,FormularioActivity.class);
               startActivity(intentvaiproformulario);
            }
        });
        registerForContextMenu(listaalunos);
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscarAlunos();
        dao.close();
        ArrayAdapter<Aluno> adapter= new ArrayAdapter<Aluno>(this,android.R.layout.simple_list_item_1,alunos);

        listaalunos.setAdapter(adapter);
    }

    protected  void onResume(){
     super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
       final  Aluno aluno =(Aluno) listaalunos.getItemAtPosition(info.position);

        MenuItem Itemligar = menu.add("Ligar");
       Itemligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem menuItem) {

                if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                   ActivityCompat.requestPermissions(MainActivity.this,
                           new String[]{android.Manifest.permission.CALL_PHONE},123);
                }else {
                    Intent intentligar = new Intent(Intent.ACTION_CALL);
                    intentligar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentligar);
                }
                return false;
                }

        });

        MenuItem ItemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:"+aluno.getTelefone()));
        ItemSMS.setIntent(intentSMS);

        MenuItem ItemMapa = menu.add("Vizualizar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        ItemMapa.setIntent(intentMapa);

        MenuItem itemSite = menu.add("Visitar site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String site = aluno.getSite();
        if(!site.startsWith("http://")){
            site = "http://"+site;
        }
        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);


        MenuItem deletar= menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                AlunoDAO dao= new AlunoDAO(MainActivity.this);
                dao.deleta(aluno);
                dao.close();
                 carregaLista();
                //Toast.makeText(MainActivity.this, "Deletar o aluno"+ aluno.getNome(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }




}
