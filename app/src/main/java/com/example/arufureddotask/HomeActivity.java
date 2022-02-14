package com.example.arufureddotask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ProgressDialog loader;
    private FirebaseUser ManagerUser;
    private FirebaseAuth Auth;
    private String UserID;
    private DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.homeToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Arufureddo manager");
        Auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);

        ManagerUser = Auth.getCurrentUser();
        UserID = ManagerUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(UserID);


        floatingActionButton = findViewById(R.id.Fab);
        floatingActionButton.setOnClickListener(v -> addTask());

    }

    private void addTask() {
        AlertDialog.Builder MyDiadlog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View MyView = inflater.inflate(R.layout.inputfile,null);
        MyDiadlog.setView(MyView);

        final AlertDialog dialog = MyDiadlog.create();
        dialog.setCancelable(false);
//        dialog.show();

        final EditText task = MyView.findViewById(R.id.Task);
        final EditText description = MyView.findViewById(R.id.Description);
        Button save = MyView.findViewById(R.id.SaveButton);
        Button cancel = MyView.findViewById(R.id.CancelButton);

            cancel.setOnClickListener(v -> dialog.dismiss());

            save.setOnClickListener(v -> {
                String Task = task.getText().toString().trim();
                String Description = description.getText().toString().trim();
                String id = reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                if(TextUtils.isEmpty(Task)){
                    task.setError("Требуется задача");
                    return;
                }
                if(TextUtils.isEmpty(Description)){
                    description.setError("Требуется описание");
                    return;
                }else{
                    loader.setMessage("Вводится ваша информация");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    Model model = new Model(Task,Description,id,date);
                    reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(HomeActivity.this,"Задание было успешно добавлено",Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(HomeActivity.this,"Неудачно" +error,Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }
                dialog.dismiss();

            });
         dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(reference, Model.class).build();
        FirebaseRecyclerAdapter<Model,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Model model) {
            holder.setDate(model.getDate());
            holder.setTask(model.getTask());
            holder.setDesc(model.getDescription());
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved,parent,false);
                return new MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

        }

        public void setTask(String task){
            TextView TaskTextView = view.findViewById(R.id.TaskRetrieved);
            TaskTextView.setText(task);
        }

        public void setDesc(String desc){
            TextView TaskTextView = view.findViewById(R.id.DescriptionRetrieved);
            TaskTextView.setText(desc);
        }
        public void setDate(String Date){
            TextView TaskTextView = view.findViewById(R.id.Date);
            TaskTextView.setText(Date);
        }
    }
}