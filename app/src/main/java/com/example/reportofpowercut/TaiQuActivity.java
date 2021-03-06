package com.example.reportofpowercut;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tool.PoiUtil;
import tool.TaiQuModel;
import tool.TaiQuadapter;

public class TaiQuActivity extends AppCompatActivity {
    public String team,line,switchOfLine;
    public ListView taiQuList;//台区列表
    public String[] newTaiQuArray;//台区数组
    public int sum;//低压户数总和
    public int num;//台区总数
    public int[] nums;//低压户数数组
    public List<TaiQuModel> taiQuModelList = new ArrayList<>();
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private  StringBuffer cancel = new StringBuffer();
    private String filepath = "/storage/emulated/0/Download/线路开关台区统计表（营配一班）.xls";//台区数据路径
    File excelFile = new File(filepath);
    ArrayList<Integer> cancal_position = new ArrayList<Integer>();

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
        team = intent.getStringExtra("team");
        line = intent.getStringExtra("line");
        switchOfLine = intent.getStringExtra("switch");
        System.out.println(team);

        /*根据班组读取相应台区文件*/
        if(team.equals("no.1")){
            filepath = "/storage/emulated/0/Download/线路开关台区统计表（营配一班）.xls";
        }
        if(team.equals("no.2")){
            filepath = "/storage/emulated/0/Download/线路开关台区统计表（营配二班）.xls";
        }
        if(team.equals("no.4")){
            filepath = "/storage/emulated/0/Download/线路开关台区统计表（营配四班）.xls";
        }
        excelFile = new File(filepath);

        /*通过TextView显示MainActivity传送来的线路与开关*/
        line_switch.setText(new StringBuilder().append("当前线路：").append(line).append("\n").append("当前开关：").append(switchOfLine).toString());

        /*根据所选的线路与开关获取相应台区信息*/
        taiQuModelList = PoiUtil.getTaiQuFromExcel(excelFile,line,switchOfLine);
        newTaiQuArray = new String[taiQuModelList.size()];
        nums = new int[taiQuModelList.size()];
        num = taiQuModelList.size();
        for (int i = 0;i<taiQuModelList.size();i++){
            newTaiQuArray[i] = taiQuModelList.get(i).getTaiqu();
            nums[i] = taiQuModelList.get(i).getNum();
            //System.out.println("低压户数"+nums[i]);
        }

        /*生成数组适配器，作为listview和台区数据的桥梁*/
        TaiQuadapter adapter = new TaiQuadapter(TaiQuActivity.this, R.layout.taiqu_item, taiQuModelList);
        this.taiQuList.setAdapter(adapter);

        /*设置台区列表子项点击监听器*/
        taiQuList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                //判断该台区是否已被取消

                if (!cancal_position.contains(position)){//根据位置判断被点击的台区是否已被取消，若未被取消则取消
                    cancal_position.add(position);
                    //Toast.makeText(TaiQuActivity.this,"取消选择台区："+taiQuModelList.get(position).getTaiqu()+position,Toast.LENGTH_SHORT).show();
                    num--; /*每点击一次，台区数量num就减一*/
                    cancel.append(taiQuModelList.get(position).getTaiqu()).append("\n");/*将取消的台区名称添加到字符串cancel中*/
                    System.out.println("选择："+position);
                    /*获取被点击的子项的TextView，为其字符后缀加上"（已取消）"，并将字体颜色设置为红色*/
                    View view1 = taiQuList.getChildAt(position-taiQuList.getFirstVisiblePosition());
                    TextView TaiQutext = view1.findViewById(R.id.taiqu_name);
                    TaiQutext.append("（已取消）");
                    TaiQutext.setTextColor(Color.rgb(255,0,0));
                    newTaiQuArray[position] = "";/*将被点击的台区名称置为空""*/
                    nums[position] = 0;/*将被点击的台区的低压户数置为0*/
                    return false;
                }else {//被点击的台区已被取消，再次点击则恢复该台区。
                    //Toast.makeText(TaiQuActivity.this,"恢复台区："+taiQuModelList.get(position).getTaiqu()+position,Toast.LENGTH_SHORT).show();
                    System.out.println("取消："+position);
                    cancal_position.remove(cancal_position.indexOf(position));
                    num++;
                    cancel = new StringBuffer(cancel.toString().replace(taiQuModelList.get(position).getTaiqu()+"\n",""));
                    View view1 = taiQuList.getChildAt(position-taiQuList.getFirstVisiblePosition());
                    TextView TaiQutext = view1.findViewById(R.id.taiqu_name);
                    TaiQutext.setText(taiQuModelList.get(position-taiQuList.getFirstVisiblePosition()).getTaiqu());
                    TaiQutext.setTextColor(Color.rgb(0,0,0));
                    newTaiQuArray[position] = taiQuModelList.get(position-taiQuList.getFirstVisiblePosition()).getTaiqu();/*将被点击的台区名称恢复*/
                    nums[position] = taiQuModelList.get(position-taiQuList.getFirstVisiblePosition()).getNum();/*将被点击的台区的低压户数恢复*/
                    return false;
                }
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
                                newTaiQuArray = updata(newTaiQuArray);//更新被选择的台区数组，将空元素剔除。
                                Intent intent = new Intent( TaiQuActivity.this,MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("team",team);
                                bundle.putString("line",line);
                                bundle.putString("switchOfLine",switchOfLine);
                                bundle.putStringArray("taiquArray",newTaiQuArray);
                                sum = Arrays.stream(nums).sum();/*对低压户数求和*/
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

    //更新被选择的台区数组，将空元素剔除。
    private String[] updata(String[] strings){
        List<String> strList= Arrays.asList(strings);
        List<String> strListNew=new ArrayList<>();
        for (int i = 0; i <strList.size(); i++) {
            if (strList.get(i)!=null&&!strList.get(i).equals("")){
                strListNew.add(strList.get(i));
            }
        }
        String[] strNewArray = strListNew.toArray(new String[strListNew.size()]);
        return strNewArray;
    }
}