package com.example.fulanoeciclano.tarefa;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.fulanoeciclano.tarefa.modelo.Aluno;

/**
 * Created by fulanoeciclano on 09/09/2017.
 */
public class formulariohelper {

    private final EditText camponome;
    private final EditText end;
    private final EditText telefone;
    private  final EditText site;
    private final RatingBar estrela;
    private final ImageView campofoto;

    private Aluno aluno;

    public formulariohelper(FormularioActivity activity){

        camponome= (EditText) activity.findViewById(R.id.nomeid);
        end= (EditText) activity.findViewById(R.id.enderecoid);
        telefone= (EditText) activity.findViewById(R.id.telefoneid);
        site= (EditText) activity.findViewById(R.id.siteid);
        estrela= (RatingBar) activity.findViewById(R.id.notaestrela);
        campofoto = (ImageView) activity.findViewById(R.id.formulario_foto);

        aluno = new Aluno();
    }

    public Aluno pegaAluno() {

        aluno.setNome(camponome.getText().toString());
        aluno.setEndereco(end.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setNota(Double.valueOf(estrela.getProgress() ));
        aluno.setCaminhofoto((String)campofoto.getTag());
        return aluno;
    }

    public void preenceformulario(Aluno aluno) {
    camponome.setText(aluno.getNome());
        end.setText(aluno.getEndereco());
        telefone.setText(aluno.getTelefone());
        site.setText(aluno.getSite());
        estrela.setProgress(aluno.getNota().intValue());
        carregaimagem(aluno.getCaminhofoto());
        this.aluno = aluno;
    }

    public void carregaimagem(String caminhofoto) {
        if (caminhofoto!= null){

        Bitmap bitmap = BitmapFactory.decodeFile(caminhofoto);
        Bitmap bitmapreduzido = bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        campofoto.setImageBitmap(bitmapreduzido);
        campofoto.setScaleType(ImageView.ScaleType.FIT_XY);
        campofoto.setTag(caminhofoto);
        }
    }
}
