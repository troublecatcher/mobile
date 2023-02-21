package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void submit(View view){
        EditText etName = findViewById(R.id.etName);
        EditText etAge = findViewById(R.id.etAge);
        RadioGroup rg = findViewById(R.id.rbSex);

        String name = etName.getText().toString();
        String age = etAge.getText().toString();

        if(name.equals("") || age.equals("") || rg.getCheckedRadioButtonId() == -1)
            Toast.makeText(this,"Введите все необходимые данные",Toast.LENGTH_SHORT).show();
        else {
            int rbId = rg.getCheckedRadioButtonId();
            RadioButton rb = findViewById(rbId);
            String sex = rb.getText().toString();

            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            i.putExtra("iName", name);
            i.putExtra("iAge", age);
            i.putExtra("iSex", sex);
            startActivity(i);
        }
    }
    public void clear(View view){
        EditText name = findViewById(R.id.etName);
        EditText age = findViewById(R.id.etAge);
        RadioGroup rg = findViewById(R.id.rbSex);
        name.setText("");
        age.setText("");
        rg.clearCheck();
    }
}