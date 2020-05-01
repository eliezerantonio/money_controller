package tech.eliezer.meukumbu.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import mehdi.sakout.aboutpage.AboutPage;
import tech.eliezer.meukumbu.R;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_sobre);


        String descricao = "Controle de gastos e despesas mensais.\n\n" +
                "Quer organizar suas finanças e descobrir de uma vez por todas pra onde seu\n" +
                "dinheiro está indo?\n" + "Então o Organizze é perfeito pra você!\n" +

                " Com poucos cliques, você consegue reunir toda a sua vida financeira em um único lugar Sua carteira, seu cartão de crédito, sua conta corrente e poupança em um aplicativo simples, bonito e muito fácil de usar..;\"\n" +
                "    ";
        View sobre = new AboutPage(this)
                //   .setImage(R.drawable.logo)

                .setDescription(descricao)
                .addGroup("Fale conosco")
                .addEmail("eliezerfernandoantonio@gmail.com", "Envie um e-mail")
                .addWebsite("http://google.com.br/", "Acess nosso site")
                .addGroup("Acesse nossas redes sociais")
                .addFacebook("google", "Facebook")
                .addTwitter("google", "Twitter")
                .addYoutube("google", "Youtube")
                .addPlayStore("com.google.android.apps.plus", "Play Store")
                .addGitHub("google", "Github")
                .addInstagram("google", "Instagram")
                .create();


        setContentView(sobre);
    }
}
