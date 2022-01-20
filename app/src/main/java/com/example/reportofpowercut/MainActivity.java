package com.example.reportofpowercut;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public String line,switchOfLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerOfLine = findViewById(R.id.line);/*线路选择菜单*/
        Spinner spinnerOfswtich = findViewById(R.id.switch_of_line);/*开关选择开关*/
        Button report_button = findViewById(R.id.report_bt);/*信息生成按键，点击后生成停电信息到粘贴板*/
        Button taiquButton = findViewById(R.id.taiqu);/*台区选择按钮，点击后跳转TaiQuActivity*/

        report_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"线路："+spinnerOfLine.getSelectedItem().toString()
                        +"。开关："+spinnerOfswtich.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                /*获取TaiQuActivity传送的台区信息*/
                Intent intent = getIntent();
                String[] strs = intent.getExtras().getStringArray("taiquArray");/*获得台区数组*/
                int sum = intent.getIntExtra("num",0);
                /*将信息复制到粘贴板*/
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("lable", "线路："+spinnerOfLine.getSelectedItem().toString()+"\n"+
                        "开关："+spinnerOfswtich.getSelectedItem().toString()+"\n"+
                        "共计"+strs.length+"个台区，分别是："+Arrays.toString(strs)+"\n"+"影响低压户数："+sum);
                cm.setPrimaryClip(mClipData);
            }
        });

        ArrayAdapter<CharSequence> adapterOfLine = ArrayAdapter.createFromResource(getApplicationContext(),R.array.lines, android.R.layout.simple_spinner_item);
        adapterOfLine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOfLine.setAdapter(adapterOfLine);

        /*线路菜单点击监听器，根据所选线路确定开关*/
        spinnerOfLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /*线路菜单点击监听器，根据所选线路确定开关*/
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"选择线路："+spinnerOfLine.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                line = spinnerOfLine.getSelectedItem().toString();
                if ("111新兴线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_111, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("114东川线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_114, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("115宜兴线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_115, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("116立地线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_116, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("117漆河线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_117, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("118农二线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_118, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("120王家河线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_120, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("121农一线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_121, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("134陈陶线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_134, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("167东农线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_167, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("168黑池线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_168, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("191李家沟".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_191, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("193北农线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_193, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("194西农线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_194, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("195河东沟线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_195, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("196王贬线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_196, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("199净水II线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_199, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("197纺织厂线".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_197, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*开关菜单点击监听器，选择该线路开关*/
        spinnerOfswtich.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switchOfLine = spinnerOfswtich.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*台区选择按键监听器，根据所选线路和开关选择台区，点击后跳转TaiQuActivity*/
        taiquButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaiQuActivity.class);
                intent.putExtra("line",line);
                intent.putExtra("switch",switchOfLine);
                startActivity(intent);
            }
        });
    }
}