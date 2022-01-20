package com.example.reportofpowercut;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import tool.TaiQuList;
public class TaiQuMainActivity extends AppCompatActivity {
    public String line,switchOfLine;
    public ListView taiQuList;
    public String[] taiquArray = new String[30];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_qu_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }
        Button bt_finish = findViewById(R.id.bt_finish);
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiQuMainActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("taiquArray",taiquArray);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        line = intent.getStringExtra("line");
        switchOfLine = intent.getStringExtra("switch");
        TextView line_switch = findViewById(R.id.line_switch);
        line_switch.setText("当前线路：" + line + "\n" + "当前开关：" + switchOfLine);
        TaiQuList taiQuList = new TaiQuList(line,switchOfLine);
        //Toast.makeText(TaiQuMainActivity.this,taiQu.taiQus.toString(),Toast.LENGTH_SHORT).show();
        this.taiQuList = findViewById(R.id.taiqu_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                TaiQuMainActivity.this, android.R.layout.simple_list_item_1, taiQuList.taiQu);
        this.taiQuList.setAdapter(adapter);

        for (int i=0;i<taiQuList.taiQu.length;i++){
            taiquArray[i]=taiQuList.taiQu[i];
            System.out.println(taiquArray[i]);
        }
    }
}