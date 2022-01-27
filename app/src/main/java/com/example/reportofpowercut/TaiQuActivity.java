package com.example.reportofpowercut;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tool.TaiQuInformation;
import tool.TaiQuModel;
import tool.TaiQuadapter;

public class TaiQuActivity extends AppCompatActivity {
    public String line,switchOfLine;
    public ListView taiQuList;//台区列表
    public String[] newTaiQuArray;//台区数组
    public int sum;
    public int num;
    public int[] nums;
    public TaiQuInformation taiQuInformation;//台区信息类
    public List<TaiQuModel> taiQuModelList = new ArrayList<>();
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private  StringBuffer cancel = new StringBuffer();;

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

        /*初始化台区信息*/
        initTaiQu(taiQuInformation);

        /*生成数组适配器，作为listview和台区数据的桥梁*/
        TaiQuadapter adapter = new TaiQuadapter(TaiQuActivity.this, R.layout.taiqu_item, taiQuModelList);
        this.taiQuList.setAdapter(adapter);

        /*获取台区数目*/
        num = taiQuInformation.nums.length;

        /*设置台区列表子项点击监听器*/
        taiQuList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(TaiQuActivity.this,"取消选择台区："+taiQuInformation.taiQuArray[position]+position,Toast.LENGTH_SHORT).show();

                num--; /*每点击一次，台区数量num就减一*/
                cancel.append(taiQuInformation.taiQuArray[position]).append("\n");/*将取消的台区名称添加到字符串cancel中*/
                System.out.println(position);
                /*获取被点击的子项的TextView，为其字符后缀加上"（已取消）"，并将字体颜色设置为红色*/
                View view1 = taiQuList.getChildAt(position-taiQuList.getFirstVisiblePosition());
                TextView TaiQutext = view1.findViewById(R.id.taiqu_name);
                TaiQutext.append("（已取消）");
                TaiQutext.setTextColor(Color.rgb(255,0,0));

                newTaiQuArray[position] = "";/*将被点击的台区名称置为空""*/
                nums[position] = 0;/*将被点击的台区的低压户数置为0*/
                sum = Arrays.stream(nums).sum();/*对低压户数求和*/

                return false;
            }
        });

        /*完成按键监听器，点击后将台区数组和低压户数传送到MainActivity*/
        bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*生成对话框，显示要取消的台区*/
                builder = new AlertDialog.Builder(TaiQuActivity.this);
                alert = builder.setTitle("取消以下台区：").setMessage(cancel)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            /*点击对话框的确认按键后将线路line，开关switchOfLine，低压户数sum，台区数量num，台区名称newTaiQuArray发送到MainActivity*/
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent( TaiQuActivity.this,MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("line",line);
                                bundle.putString("switchOfLine",switchOfLine);
                                bundle.putStringArray("taiquArray",newTaiQuArray);
                                bundle.putInt("sum",sum);
                                bundle.putInt("num",num);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                alert.show();
            }
        });
    }

    /*初始化台区信息，通过taiQuInformation类获取线路开关及台区信息，生成所有台区类taiQuModel，并添加到台区类列表中*/
    private void initTaiQu(TaiQuInformation taiQuInformation){
        newTaiQuArray = taiQuInformation.taiQuArray;
        sum = taiQuInformation.sumOfNum(taiQuInformation.nums);
        nums = taiQuInformation.nums;

        for (int i = 0;i<taiQuInformation.taiQuArray.length;i++){
            TaiQuModel taiQuModel = new TaiQuModel(taiQuInformation.line,taiQuInformation.switchOfLine,taiQuInformation.taiQuArray[i],taiQuInformation.nums[i]);
            taiQuModelList.add(taiQuModel);
            System.out.println(taiQuModelList.get(i).getTaiqu());
        }
    }
}