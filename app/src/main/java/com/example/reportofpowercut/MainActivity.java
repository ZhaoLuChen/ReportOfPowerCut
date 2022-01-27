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
    private String[] strs;
    private int sum,num;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private StringBuffer report = new StringBuffer();
    private String filepath = "/storage/emulated/0/Download/线路开关台区统计表.xls";
    int time = 0;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(filepath);
                if (file.exists()){
                    System.out.println("找到文件");
                    System.out.println(PoiUtil.readExcel(file));
                }else{
                    System.out.println("没有找到文件");
                }
            }
        });

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

                /*生成停电报备的文字信息*/
                report.append("线路：").append(line).append("\n").append("开关：").append(switchOfLine).append("\n")
                        .append("共计").append(num).append("个台区，分别是：").append(Arrays.toString(strs)).append("\n").append("影响低压户数：")
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

                if ("114东川线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_114, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("115宜兴线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_115, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("116立地线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_116, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("117漆河线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_117, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("118农二线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_118, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("120王家河线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_120, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("121农一线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_121, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("134陈陶线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_134, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("167东农线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_167, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("168黑池线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_168, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("191李家沟（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_191, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("193北农线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_193, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("194西农线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_194, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("195河东沟线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_195, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("196王贬线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_196, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("199净水II线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
                    ArrayAdapter<CharSequence> adapterOfSwitch = ArrayAdapter.createFromResource(getApplicationContext(), R.array.switch_of_199, android.R.layout.simple_spinner_item);
                    adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerOfswtich.setAdapter(adapterOfSwitch);
                }

                if ("197纺织厂线（数据暂未导入）".equals(spinnerOfLine.getSelectedItem().toString())) {
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