package tech.eliezer.meukumbu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import tech.eliezer.meukumbu.R;

public class PerfilFragment extends Fragment {
    EditText editNome;
    EditText editEmail;
    EditText editSenha;
    Button botaoAlterar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        editNome = view.findViewById(R.id.editAlterarNome);
        editSenha = view.findViewById(R.id.editAlterarSenha);
        editEmail = view.findViewById(R.id.editAlterarEmail);

        botaoAlterar = view.findViewById(R.id.btAlterar);

        botaoAlterar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                }
        );


        return view;


    }


}
