package com.example.simplecalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    Button btn0, btn00, btndot, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnAdd, btnSubtract, btnMul, btnDiv, btnEqual, btnBs, btnAc,btnPercent;
    TextView textResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        btn0 = findViewById(R.id.no0);
        btn00 = findViewById(R.id.no00);
        btndot = findViewById(R.id.btndot);
        btn1 = findViewById(R.id.no1);
        btn2 = findViewById(R.id.no2);
        btn3 = findViewById(R.id.no3);
        btn4 = findViewById(R.id.no4);
        btn5 = findViewById(R.id.no5);
        btn6 = findViewById(R.id.no6);
        btn7 = findViewById(R.id.no7);
        btn8 = findViewById(R.id.no8);
        btn9 = findViewById(R.id.no9);

        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnMul = findViewById(R.id.btnMultiply);
        btnDiv = findViewById(R.id.btnDivide);
        btnPercent = findViewById(R.id.btnpercentage);
        btnEqual = findViewById(R.id.btneual);

        btnBs = findViewById(R.id.btnBspace);
        btnAc = findViewById(R.id.btnAclear);

        textResult = findViewById(R.id.txtresult);

        //One listener for each button
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String buttontext = button.getText().toString();
                String currenttext = textResult.getText().toString();

                //prevent operator and dot starting
                if(currenttext.isEmpty()){
                    if(isOperator(buttontext) || buttontext.contains("."))
                        return;
                    textResult.append(buttontext);
                    return;
                }

                //prevent multiple operator
                //to check clicked button id operator or not
                char lastChar = currenttext.charAt(currenttext.length()-1);
                if(isOperator(buttontext)) {
                    //if last charactor is operator so replace it
                    if (isOperator(String.valueOf(lastChar))) {

                        //remove last charactor
                        currenttext = currenttext.substring(0, currenttext.length() - 1);

                        //set new text with new operator
                        textResult.setText(currenttext + buttontext);
                        return;
                    }
                }
                     //prevent multiple dots
                if(buttontext.equals(".")){
                        String lastNum = getLastNumber(currenttext);
                        if(lastNum.contains("."))
                            return;
                }
                textResult.append(buttontext);


            }
        };
        btn0.setOnClickListener(numberClickListener);
        btn00.setOnClickListener(numberClickListener);
        btndot.setOnClickListener(numberClickListener);
        btn1.setOnClickListener(numberClickListener);
        btn2.setOnClickListener(numberClickListener);
        btn3.setOnClickListener(numberClickListener);
        btn4.setOnClickListener(numberClickListener);
        btn5.setOnClickListener(numberClickListener);
        btn6.setOnClickListener(numberClickListener);
        btn7.setOnClickListener(numberClickListener);
        btn8.setOnClickListener(numberClickListener);
        btn9.setOnClickListener(numberClickListener);

        btnAdd.setOnClickListener(numberClickListener);
        btnSubtract.setOnClickListener(numberClickListener);
        btnMul.setOnClickListener(numberClickListener);
        btnDiv.setOnClickListener(numberClickListener);
        btnPercent.setOnClickListener(numberClickListener);

        //Action on equal button

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

        //AC button
        btnAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textResult.setText("");
            }
        });

        //backspace button
        btnBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currenttext = textResult.getText().toString();
                //remove last charactor
                if(!currenttext.isEmpty()){
                    currenttext = currenttext.substring(0,currenttext.length()-1);
                    textResult.setText(currenttext);
                }
            }
        });

    }
    private boolean isOperator(String value){
        return value.equals("+") || value.equals("-")
                || value.equals("x") || value.equals("÷")
                || value.equals("%");
    }
    private String getLastNumber(String text){
        int lastOperatorIndex = -1;
        for(int i = text.length()-1;i>=0;i--){
            if(isOperator(String.valueOf(text.charAt(i)))){
                lastOperatorIndex = i;
                break;
            }
        }
        return text.substring(lastOperatorIndex+1);
    }
    private void calculateResult(){
        String expression = textResult.getText().toString();

        if(expression.isEmpty())
            return;
        expression = expression.replace("x","*");
        expression = expression.replace("÷","/");

        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        String number = "";

        //split numbers and operators
        for(int i=0;i<expression.length();i++){
            char ch = expression.charAt(i);
            if(Character.isDigit(ch) || ch == '.'){
                number += ch;
            }
            else{
                numbers.add(Double.parseDouble(number));
                operators.add(ch);
                number = "";
            }
        }
        numbers.add(Double.parseDouble(number));

        //handle * and /
        for(int i=0;i<operators.size();i++){
            char op = operators.get(i);
            if(op == '*' || op == '/'){
                double num1 = numbers.get(i);
                double num2 = numbers.get(i+1);

                double result = 0;
                if(op =='*')
                    result = num1*num2;
                else {
                    if (num2 == 0) {
                        textResult.setText("Error");
                        return;
                    }
                    result = num1 / num2;
                }
                numbers.set(i,result);
                numbers.remove(i+1);
                operators.remove(i);
                i--;
            }
        }
        //handle + and -
        double result = numbers.get(0);
        for(int i = 0;i<operators.size();i++){
            char op = operators.get(i);
            double num = numbers.get(i+1);

            if(op == '+')
                result += num;
            else if (op == '-')
                result -= num;
        }

        //remove .0 if integer
        if(result == (int)result)
            textResult.setText(String.valueOf((int)result));
        else
            textResult.setText(String.valueOf(result));

    }
}

