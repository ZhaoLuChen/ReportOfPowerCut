package tool;
import java.util.Hashtable;

public class TaiQuList {
    public String line,switchOfLine;
    public String[] taiQu;
    public int[] num;
    public Hashtable taiQus;
    public TaiQuList(String line, String switchOfLine){
        this.line=line;
        this.switchOfLine=switchOfLine;
        if ("111新兴线".equals(line)){
            if ("111新兴线#06-#07杆开关".equals(switchOfLine)){
                taiQu= new String[]{"崖俭组","那坡一组","那坡二组","那坡二组二台区","咀头村","新兴二组","北堡子",
                        "水沟组南头","新兴一组","陈炉街道二台区","马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","湾里","陈炉景区管委会","窑院","后崖组",
                        "陈炉街道","陈炉幼儿园","陈炉中小学","耀州博物馆陈炉分馆"};

                num = new int[]{50,50,60,0,79,52,64,0,115,23,144,73,38,153,1,0,62,85,0,118,0,137,84,72,10,0,0};

                taiQus = new Hashtable();
                for(int i=0;i<taiQu.length;i++){
                    taiQus.put(taiQu[i],num[i]);
                }
            }
        }
    }
}
