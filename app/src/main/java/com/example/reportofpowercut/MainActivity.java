package com.example.reportofpowercut;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.Arrays;

import tool.PoiUtil;
import tool.TaiQuInformation;

public class MainActivity extends AppCompatActivity {

    private String line,switchOfLine;
    private String[] strs,lines,switchs;
    private int sum,num;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private StringBuffer report = new StringBuffer();
    private String filepath = "/storage/emulated/0/Download/线路开关台区统计表.xls";//台区数据路径
    int time = 0;

    //读写权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //申请读写权限以及存储管理权限（Android 11开始要读写文件必须申请存储管理权限）
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //判断SDK版本是否是Android 11以后，是的话申请存储管理权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()){
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
        verifyStoragePermissions(MainActivity.this);

        Spinner spinnerOfLine = findViewById(R.id.line);/*线路选择菜单*/
        Spinner spinnerOfswtich = findViewById(R.id.switch_of_line);/*开关选择开关*/
        Button report_button = findViewById(R.id.report_bt);/*信息生成按键，点击后生成停电信息到粘贴板*/
        Button taiquButton = findViewById(R.id.taiqu);/*台区选择按钮，点击后跳转TaiQuActivity*/
        EditText timeText = findViewById(R.id.time_cut);/*输入停电时间*/
        Button loadButton = findViewById(R.id.load_bt);/*载入台区数据表格*/
        TextView choosedLineSwitch = findViewById(R.id.choosed_line_switch);
        TextView choosedNum = findViewById(R.id.choosed_num);
        TextView choosedSum = findViewById(R.id.choosed_sum);

        //读取台区数据表格，获取表格中的线路
        File excelFile = new File(filepath);

        lines = PoiUtil.getLinesFromExcel(excelFile);
        ArrayAdapter<String> adapterOfLine = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,lines);
        adapterOfLine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOfLine.setAdapter(adapterOfLine);

        /*线路菜单点击监听器，根据所选线路确定开关*/
        spinnerOfLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this,"选择线路："+spinnerOfLine.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                line = spinnerOfLine.getSelectedItem().toString();
                switchs = PoiUtil.getSwitchsFormExcel(excelFile,line);
                ArrayAdapter<String> adapterOfSwitch = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,switchs);
                adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOfswtich.setAdapter(adapterOfSwitch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*开关菜单点击监听器，选择该线路开关*/
        spinnerOfswtich.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("进入开关菜单");
                switchOfLine = spinnerOfswtich.getSelectedItem().toString();
                PoiUtil.getTaiQuFromExcel(excelFile,line,switchOfLine);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //数据载入按键监听器
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(filepath);
                if (file.exists()){
                    System.out.println("找到文件");
                    File excelFile = new File(filepath);
                    lines = PoiUtil.getLinesFromExcel(excelFile);
                }else{
                    System.out.println("没有找到文件");
                }
            }
        });

        //信息生成按键监听器
        report_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"线路："+spinnerOfLine.getSelectedItem().toString()
                        +"。开关："+spinnerOfswtich.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                if (!TextUtils.isEmpty(timeText.getText())){
                    time = Integer.parseInt(timeText.getText().toString());
                }

                /*获取TaiQuActivity传送的台区信息*/
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                //判断是否从台区选择界面跳转过来的
                if (bundle == null){
                    TaiQuInformation taiQuInformation = new TaiQuInformation(line,switchOfLine);
                    strs = taiQuInformation.taiQuArray;
                    sum = taiQuInformation.sumOfNum(taiQuInformation.nums);
                    num = taiQuInformation.nums.length;
                    line = spinnerOfLine.getSelectedItem().toString();
                    switchOfLine = spinnerOfswtich.getSelectedItem().toString();
                }else {
                    strs = bundle.getStringArray("taiquArray");/*获得台区数组*/
                    sum = bundle.getInt("sum",0);
                    num = bundle.getInt("num",0);
                    line = bundle.getString("line",line);
                    switchOfLine = bundle.getString("switchOfLine",switchOfLine);
                }
                choosedLineSwitch.append(line+"，");
                choosedLineSwitch.append(switchOfLine);
                choosedNum.append(num+"");
                choosedSum.append(sum+"");

                /*生成停电报备的文字信息*/
                report.append("坐席您好，印台王益区供电公司").append(line).append("发生故障。").append("\n").append("跳闸开关：").append(switchOfLine).append("\n")
                        .append("停电范围共计").append(num).append("个台区，分别是：").append(Arrays.toString(strs)).append("\n").append("影响低压户数：")
                        .append(sum).append("\n").append("停电时户数：").append(num*time).append("\n").append("在此期间客户可能会致电95598，特此报备。烦请各位坐席给予解释安抚，拦截工单谢谢。");

                /*生成一个对话框显示生成停电报备的信息*/
                builder = new AlertDialog.Builder(MainActivity.this);
                alert = builder.setTitle("停电报备信息如下：").setMessage(report)
                        .setPositiveButton("复制到粘贴板", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /*点击确定按键，将信息复制到粘贴板*/
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData mClipData = ClipData.newPlainText("lable", report);
                                cm.setPrimaryClip(mClipData);
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