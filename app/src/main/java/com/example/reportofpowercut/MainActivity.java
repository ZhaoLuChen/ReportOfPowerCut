package com.example.reportofpowercut;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tool.PoiUtil;
import tool.TaiQuModel;
import tool.TaiQuadapter;


public class MainActivity extends AppCompatActivity {

    private String line,switchOfLine,county = "印王公司",classes = "线路开关台区统计表（营配一班）.xls";//所选线路及开关
    private String[] strs,lines,switchs;//台区数组、线路数组、开关数组
    private int sum,num;//低压户数、台区数
    public ListView taiQuList;//台区列表
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private StringBuffer report = new StringBuffer();//停电报备信息的长字符串
    private String filepath_class="线路开关台区统计表（营配一班）.xls";//班组台区数据文件路径
    private String filepath_county="/storage/emulated/0/Download/yinwang/";//区县台区数据文件路径
    int time = 0;//停电时间
    private File excelFile;
    private View dialogView;


    //读写权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //申请读写权限以及存储管理权限（Android 11开始要读写文件必须申请存储管理权限）
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //屏蔽返回键
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
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

        //确认权限是否申请
        verifyStoragePermissions(MainActivity.this);

        Spinner spinnerOfLine = findViewById(R.id.line);/*线路选择菜单*/
        Spinner spinnerOfswtich = findViewById(R.id.switch_of_line);/*开关选择开关*/
        Spinner spinnerOfClasses = findViewById(R.id.classes);
        Button report_button = findViewById(R.id.report_bt);/*信息生成按键，点击后生成停电信息到粘贴板*/
        Button taiquButton = findViewById(R.id.taiqu);/*台区选择按钮，点击后跳转TaiQuActivity*/
        EditText timeText = findViewById(R.id.time_cut);/*输入停电时间*/
        RadioGroup countyRadgroup = (RadioGroup) findViewById(R.id.county_radioGroup);//区县公司选择

        //将台区信息统计表拷贝到手机下载文件夹
        copyAssetsToDst(MainActivity.this, "excel/yinwang", "/storage/emulated/0/Download/yinwang/");
        copyAssetsToDst(MainActivity.this, "excel/xinqu", "/storage/emulated/0/Download/xinqu/");
        copyAssetsToDst(MainActivity.this, "excel/yaoxian", "/storage/emulated/0/Download/yaoxian/");
        copyAssetsToDst(MainActivity.this, "excel/yijun", "/storage/emulated/0/Download/yijun/");

        initAdapter(spinnerOfClasses,new File(filepath_county).list());

        countyRadgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                county = "";
                county =radioButton.getText().toString();
                if (county.equals("印王")){
                    filepath_county = "/storage/emulated/0/Download/yinwang/";
                }
                if (county.equals("宜君")){
                    filepath_county ="/storage/emulated/0/Download/yijun/";
                }
                if (county.equals("耀县")){
                    filepath_county ="/storage/emulated/0/Download/yaoxian/";
                }
                if (county.equals("新区")){
                    filepath_county ="/storage/emulated/0/Download/xinqu/";
                }
                county = county+"公司";

                filepath_class = Objects.requireNonNull(new File(filepath_county).list())[0];

                File file = new File(filepath_county);
                if (file.exists())
                System.out.println(Arrays.toString(file.list()));

                initAdapter(spinnerOfClasses,file.list());
            }
        });

        spinnerOfClasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filepath_class  = spinnerOfClasses.getSelectedItem().toString();
                classes = "";
                classes = filepath_class.substring(filepath_class.indexOf("（") + 1, filepath_class.indexOf("）"));
                System.out.println(classes);

                excelFile = findFile(filepath_county,filepath_class);
                lines = PoiUtil.getLinesFromExcel(excelFile);
                initAdapter(spinnerOfLine,lines);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*线路菜单点击监听器，根据所选线路确定开关*/
        spinnerOfLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //获取被选择的线路
                line = spinnerOfLine.getSelectedItem().toString();
                System.out.println(line);
                //根据被选择的线路，读取当前线路下的开关
                switchs = PoiUtil.getSwitchsFormExcel(excelFile,line);
                //设置开关菜单数组适配器
                ArrayAdapter<String> adapterOfSwitch = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,switchs);
                adapterOfSwitch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //为开关菜单设置适配器
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
                //获取被选择的开关
                switchOfLine = spinnerOfswtich.getSelectedItem().toString();
                //根据被选择的开关和线路获取台区
                PoiUtil.getTaiQuFromExcel(excelFile,spinnerOfLine.getSelectedItem().toString(),
                        spinnerOfswtich.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //信息生成按键监听器
        report_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //判断是否输入停电时间
                if (timeText.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"请先输入停电时间",Toast.LENGTH_LONG).show();
                }else {
                    if (!TextUtils.isEmpty(timeText.getText())){
                        time = Integer.parseInt(timeText.getText().toString());//获取停电时间
                    }
                    report =new StringBuffer("") ;
                    List<TaiQuModel> taiQuModelList = PoiUtil.getTaiQuFromExcel(excelFile,line,switchOfLine);
                    strs = new String[taiQuModelList.size()];
                    int[] nums = new int[taiQuModelList.size()];
                    for (int i = 0;i<taiQuModelList.size();i++){
                        strs[i] = taiQuModelList.get(i).getTaiqu();
                        nums[i] = taiQuModelList.get(i).getNum();
                    }
                    num = taiQuModelList.size();
                    sum = Arrays.stream(nums).sum();/*对低压户数求和*/
                    line = spinnerOfLine.getSelectedItem().toString();
                    switchOfLine = spinnerOfswtich.getSelectedItem().toString();

                    /*生成停电报备的文字信息*/
                    report.append("坐席您好，").append(county).append(classes).append(line).append("发生故障。").append("\n").append("跳闸开关：").append(switchOfLine).append("\n")
                            .append("停电范围共计").append(num).append("个台区，分别是：").append(Arrays.toString(strs)).append("\n").append("影响低压户数：")
                            .append(sum).append("\n").append("停电时户数：").append(num*time).append("\n").append("在此期间客户可能会致电95598，特此报备。烦请各位坐席给予解释安抚，拦截工单谢谢。");

                    /*生成一个对话框显示生成停电报备的信息*/
                    builder = new AlertDialog.Builder(MainActivity.this);
                    alert = builder.setTitle("停电报备信息如下：").setMessage(report)
                            .setPositiveButton("复制到粘贴板", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                                    /*点击确定按键，将信息复制到粘贴板*/
                                    ClipData mClipData = ClipData.newPlainText("lable", report);
                                    cm.setPrimaryClip(mClipData);

                                    getWechatApi();
                                    /*将report的信息清空*/
                                    report =new StringBuffer("") ;
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    /*将report的信息清空*/
                                    report =new StringBuffer("") ;
                                }
                            })
                            .create();

                    alert.show();
                }
            }
        });
    }

    private void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File findFile(String filepath_county,String filepath_class){
        File excelFile = new File(filepath_county+filepath_class);
        if(excelFile.exists()){
            System.out.println("找到文件"+excelFile.toString());
            lines = PoiUtil.getLinesFromExcel(excelFile);
        }else {
            System.out.println("未到文件");
        }
        return  excelFile;
    }

    private void initAdapter(Spinner spinner,String[] strs){
        //设置线路菜单数组适配器
        ArrayAdapter<String> adapterOfLine = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,strs);
        adapterOfLine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //为线路菜单设置适配器
        spinner.setAdapter(adapterOfLine);
    }

    public List<String> extractMessageByRegular(String msg){
        List<String> list=new ArrayList<String>();
        Pattern p = Pattern.compile("(\\((^\\))*\\))");
        Matcher m = p.matcher(msg);
        while(m.find()){
            list.add(m.group().substring(1, m.group().length()-1));
        }
        return list;
    }

    private void getWechatApi(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
        }
    }


}