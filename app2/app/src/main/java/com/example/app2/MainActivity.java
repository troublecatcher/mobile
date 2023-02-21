package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] numbers = new int[]{
                R.id.b0,
                R.id.b1,
                R.id.b2,
                R.id.b3,
                R.id.b4,
                R.id.b5,
                R.id.b6,
                R.id.b7,
                R.id.b8,
                R.id.b9
        };

        int[] actions = new int[]{
                R.id.bAdd,
                R.id.bSub,
                R.id.bMul,
                R.id.bDiv,
                R.id.bEquals,
                R.id.bClear,
                R.id.bComma,
                R.id.bSqrt,
                R.id.bFrac,
                R.id.bNeg
        };

        text = findViewById(R.id.output);
        CalculatorModel calc = new CalculatorModel();

        View.OnClickListener numberPressListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.onNumberPressed(view.getId());
                text.setText(calc.getText());
            }
        };
        View.OnClickListener actionPressListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc.onActionPressed(view.getId());
                text.setText(calc.getText());
            }
        };

        for (int i = 0; i < numbers.length; i++) {
            findViewById(numbers[i]).setOnClickListener(numberPressListener);
        }
        for (int i = 0; i < actions.length; i++) {
            findViewById(actions[i]).setOnClickListener(actionPressListener);
        }
    }
}