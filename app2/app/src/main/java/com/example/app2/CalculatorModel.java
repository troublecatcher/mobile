package com.example.app2;

import android.os.IInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.nio.file.LinkOption;

public class CalculatorModel {
    private double first;
    private double second;

    StringBuilder input = new StringBuilder();
    private int action;
    private State state = State.idle;

    private enum State{
        inputFirst,
        inputSecond,
        idle,
    }

    public CalculatorModel(){
        first = 0;
        input.append(trim(first));
    }

    public void onNumberPressed(int buttonId) {
        if(state == State.idle && buttonId != R.id.b0){
            clearInput();
            state = State.inputFirst;
        }
        if(input.length() < 16){
            switch (buttonId){
                case R.id.b0:
                    if(input.length() > 0 && input.charAt(0) != '0' || first != 0)
                        input.append("0");
                    break;
                case R.id.b1: input.append("1"); break;
                case R.id.b2: input.append("2"); break;
                case R.id.b3: input.append("3"); break;
                case R.id.b4: input.append("4"); break;
                case R.id.b5: input.append("5"); break;
                case R.id.b6: input.append("6"); break;
                case R.id.b7: input.append("7"); break;
                case R.id.b8: input.append("8"); break;
                case R.id.b9: input.append("9"); break;
            }
        }
    }
    public void onActionPressed(int actionId) {

        if(state == State.idle){
            state = State.inputFirst;
        }

        if(state == State.inputFirst && input.length() > 0){
            first = Double.parseDouble(input.toString());
            clearInput();
            switch (actionId){
                case R.id.bEquals:
                    input.append(trim(first));
                    break;
                case R.id.bAdd:
                    action = R.id.bAdd;
                    state = State.inputSecond;
                    break;
                case R.id.bSub:
                    action = R.id.bSub;
                    state = State.inputSecond;
                    break;
                case R.id.bMul:
                    action = R.id.bMul;
                    state = State.inputSecond;
                    break;
                case R.id.bDiv:
                    action = R.id.bDiv;
                    state = State.inputSecond;
                    break;
                case R.id.bSqrt:
                    first = Math.sqrt(first);
                    input.append(trim(first));
                    break;
                case R.id.bFrac:
                    first = 1 / first;
                    input.append(trim(first));
                    break;
                case R.id.bNeg:
                    if(first == 0)
                        input.append(trim(first));
                    else{
                        input.append(trim(-first));
                        state = State.idle;
                    };
                    break;
                case R.id.bComma:
                    input.append((int)first + ".");
                    break;
            }
        }

        if(state == State.inputSecond && input.length() > 0){
            second = Double.parseDouble(input.toString());
            clearInput();
            switch (actionId){
                case R.id.bEquals:
                    clearInput();
                    switch (action){
                        case R.id.bAdd:
                            first += second;
                            input.append(trim(first));
                            break;
                        case R.id.bSub:
                            first -= second;
                            input.append(trim(first));
                            break;
                        case R.id.bMul:
                            first *= second;
                            input.append(trim(first));
                            break;
                        case R.id.bDiv:
                            first /= second;
                            input.append(trim(first));
                            break;
                    }
                    state = State.inputFirst;
                    break;
                case R.id.bSqrt:
                    clearInput();
                    second = Math.sqrt(second);
                    input.append(trim(second));
                    break;
                case R.id.bFrac:
                    clearInput();
                    second = 1 / second;
                    input.append(trim(second));
                    break;
                case R.id.bNeg:
                    clearInput();
                    if(second == 0)
                        input.append(trim(second));
                    else
                        input.append(trim(-second));
                    break;
                case R.id.bComma:
                    input.append((int)second + ".");
                    break;
            }
        }

        if(actionId == R.id.bClear){
            clearInput();
            input.append(0);
            first = second = 0;
            state = State.idle;
        }
    }

    public static String trim(Double d){
        String s = Double.toString(d);
        return s.contains(".") ?
                s.replaceAll("0*$","")
                        .replaceAll("\\.$","")
                : s;
    }

    public void clearInput(){
        input.setLength(0);
    }

    public String getText(){
        return input.toString();
    }
}
