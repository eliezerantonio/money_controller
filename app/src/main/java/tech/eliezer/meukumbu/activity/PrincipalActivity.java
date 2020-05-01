package tech.eliezer.meukumbu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

import tech.eliezer.meukumbu.R;
import tech.eliezer.meukumbu.adapter.AdapterMovimentacao;
import tech.eliezer.meukumbu.config.ConfiguracaoFirebase;
import tech.eliezer.meukumbu.config.UsuarioFirebase;
import tech.eliezer.meukumbu.fragment.ContactoFragment;
import tech.eliezer.meukumbu.fragment.PerfilFragment;
import tech.eliezer.meukumbu.helper.NumberCustom;
import tech.eliezer.meukumbu.model.Movimentacao;
import tech.eliezer.meukumbu.model.Usuario;


public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;
    private Double despesaTotal = 0.0;
    private Double receitaTotal = 0.0;
    private Double resumoUsuario = 0.0;

    private DrawerLayout drawerLayout;


    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;
    private Movimentacao movimentacao;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        inicializarComponentes();
        configuraCalendarView();
        swipe();

        //Configurar adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);


        //drawer layout

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_contato:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ContactoFragment()).commit();
                break;
            case R.id.nav_perfil:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PerfilFragment()).commit();
                break;
            case R.id.nav_movimento:

              Toast.makeText(this, "Em Construção",Toast.LENGTH_SHORT).show();

                break;



            case R.id.nav_sobre:
                startActivity(new Intent(this, SobreActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                excluirMovimentacao(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir movimentação da conta");
        alertDialog.setMessage("Você tem certeza que deseja  excluir essa movimentação? ");
        alertDialog.setCancelable(false);


        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int posicao = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(posicao);

                String idUsuario = UsuarioFirebase.getIdentificadorUsuario();

                movimentacaoRef = firebaseRef.child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);

                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(posicao);
                atualizarSaldo();


            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_LONG).show();

                adapterMovimentacao.notifyDataSetChanged();


            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void atualizarSaldo() {
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        if (movimentacao.getTipo().equals("r")) {
            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }
        if (movimentacao.getTipo().equals("d")) {

            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(
                    despesaTotal);
        }

    }

    public void recuperarMovimentacoes() {
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        movimentacaoRef = firebaseRef.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentacoes.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setKey(dados.getKey());
                    movimentacoes.add(movimentacao);

                }

                adapterMovimentacao.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void recuperarResumo() {
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;


                String resumoFormatado = NumberCustom.formatarNumero(resumoUsuario);

                textoSaudacao.setText("Olá, " + usuario.getNome());
                textoSaldo.setText(resumoFormatado + " KZ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSair:
                autenticacao.signOut();

                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void adicionarDespesa(View view) {

        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));

    }

    public void configuraCalendarView() {
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1));
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", (date.getMonth() + 1));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Evento", "Evento foi removido");
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }

    public double recuperarRes() {
        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return 0;
    }

    public void inicializarComponentes() {
        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimentos);
    }
}
