package com.example.fulanoeciclano.tarefa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fulanoeciclano.tarefa.dao.AlunoDAO;
import com.example.fulanoeciclano.tarefa.modelo.Aluno;

import java.io.File;

public class FormularioActivity extends AppCompatActivity {

    public static final int codigo_camera = 567;
    private Button botaosalvar;
    private Intent Vaipralista;
    private formulariohelper helper;
    private  Button botaofoto;
    private String caminhofoto;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper= new formulariohelper(this);

        Intent intent =getIntent();
        Aluno aluno=(Aluno)intent.getSerializableExtra("aluno");
        if (aluno!=null){
         helper.preenceformulario(aluno);
        }
        //botao da camera
        botaofoto = (Button) findViewById(R.id.formulario_botao_foto);
        botaofoto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intentcamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               caminhofoto = getExternalFilesDir(null) + "/"+System.currentTimeMillis()+"foto.jpg";
                File arquivofoto = new File(caminhofoto);
                intentcamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivofoto));
                startActivityForResult(intentcamera, codigo_camera);
            }
        });
    }

   //carregar a foto tirada
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //abrir a foto
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == codigo_camera) {
                helper.carregaimagem(caminhofoto);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario,menu);

        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Aluno aluno = helper.pegaAluno();

                AlunoDAO dao = new AlunoDAO(this);
                if (aluno.getId() !=null){
                    dao.altera(aluno);
                } else {
                    dao.insere(aluno);
                }


                dao.close();
                Toast.makeText(FormularioActivity.this, "Contato "+aluno.getNome()+" Salvo!", Toast.LENGTH_SHORT).show();



                finish();
               break;
        }

        return super.onOptionsItemSelected(item);
    }
}
