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

import tool.TaiQuInformation;
public class TaiQuActivity extends AppCompatActivity {
    public String line,switchOfLine;
    public ListView taiQuList;//台区列表
    public String[] taiquArray = new String[30];//台区数组
    public TaiQuInformation taiQuInformation;//台区信息类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_qu_main);

        Button bt_finish = findViewById(R.id.bt_finish);
        TextView line_switch = findViewById(R.id.line_switch);
        taiQuList = findViewById(R.id.taiqu_list);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        /*获取MainActivity传送来的线路与开关*/
        Intent intent = getIntent();
        line = intent.getStringExtra("line");
        switchOfLine = intent.getStringExtra("switch");

        /*通过TextView显示MainActivity传送来的线路与开关*/
        line_switch.setText(new StringBuilder().append("当前线路：").append(line).append("\n").append("当前开关：").append(switchOfLine).toString());

        /*根据所选的线路与开关显示相应台区*/
        TaiQuInformation taiQuInformation = new TaiQuInformation(line,switchOfLine);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TaiQuActivity.this, android.R.layout.simple_list_item_1, taiQuInformation.taiQuArray);
        this.taiQuList.setAdapter(adapter);

        /*完成按键监听器，点击后将台区数组传送到MainActivity*/
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiQuActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArray("taiquArray",taiQuInformation.taiQuArray);
                bundle.putInt("num",taiQuInformation.sumOfNum(taiQuInformation.nums));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}