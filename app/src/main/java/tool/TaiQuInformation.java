package tool;

public class TaiQuInformation {
    public String line,switchOfLine;
    public String[] taiQuArray;
    public int[] nums;
    public TaiQuModel[] taiQuModelArray;
    public TaiQuInformation(String line, String switchOfLine){
        this.line=line;
        this.switchOfLine=switchOfLine;
        if ("111新兴线".equals(line)){
            if ("111新兴线#06-#07杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"崖俭组","那坡一组","那坡二组","那坡二组二台区","咀头村","新兴二组","北堡子",
                        "水沟组南头","新兴一组","陈炉街道二台区","马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","湾里","陈炉景区管委会","窑院","后崖组",
                        "陈炉街道","陈炉幼儿园","陈炉中小学","耀州博物馆陈炉分馆"};
                nums = new int[]{50 ,50 ,60 ,0 ,79 ,52 ,64 ,0 ,115 ,23 ,144 ,73 ,38 ,153 ,1 ,0 ,62 ,85 ,0 ,118 ,0 ,137 ,84 ,72 ,10 ,0 ,0};
                for(int i=0;i<taiQuArray.length;i++){
                    taiQuModelArray = new TaiQuModel[taiQuArray.length];
                    taiQuModelArray[i] = new TaiQuModel(line,switchOfLine,taiQuArray[i],nums[i]);
                }
            }

            if ("111新兴线#34杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"咀头村","新兴二组","北堡子",
                        "水沟组南头","新兴一组","陈炉街道二台区","马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","湾里","陈炉景区管委会","窑院","后崖组",
                        "陈炉街道","陈炉幼儿园","陈炉中小学","耀州博物馆陈炉分馆"};
            }

            if ("111新兴线新兴支线#8杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"北堡子","水沟组南头","新兴一组","陈炉街道二台区","新兴二组"};
            }

            if ("111新兴线#44杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"耀州博物馆陈炉分馆","窑院","后崖组","陈炉中小学","陈炉幼儿园","陈炉街道"};
            }

            if ("111新兴线马科支线#01杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","陈炉街道"};
            }
        }

        if ("114东川线".equals(line)){
            if ("114东川线#2杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"崖俭组","那坡一组","那坡二组","那坡二组二台区","咀头村","新兴二组","北堡子",
                        "水沟组南头","新兴一组","陈炉街道二台区","马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","湾里","陈炉景区管委会","窑院","后崖组",
                        "陈炉街道","陈炉幼儿园","陈炉中小学","耀州博物馆陈炉分馆"};
            }

            if ("114东川线#25-#26杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"咀头村","新兴二组","北堡子",
                        "水沟组南头","新兴一组","陈炉街道二台区","马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","湾里","陈炉景区管委会","窑院","后崖组",
                        "陈炉街道","陈炉幼儿园","陈炉中小学","耀州博物馆陈炉分馆"};
            }

            if ("114东川线上店支线#1杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"北堡子","水沟组南头","新兴一组","陈炉街道二台区","新兴二组"};
            }

            if ("114东川线上店支线#55杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"耀州博物馆陈炉分馆","窑院","后崖组","陈炉中小学","陈炉幼儿园","陈炉街道"};
            }

            if ("114东川线林场支线#5杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","陈炉街道"};
            }

            if ("114东川线张家村支线#2杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","陈炉街道"};
            }

            if ("114东川线#59-#60杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","陈炉街道"};
            }

            if ("114东川线黄古寨支线#1杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"马莲滩","沙梁","陈炉敬老院台区","永兴村","永兴光伏",
                        "马科村四组","马科村123组二台区","马科村","印台水务局南部塬抽水","陈炉街道"};
            }

            if ("114东川线祁家沟支线#1杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"祁家沟"};
            }

            if ("114东川线#94杆开关".equals(switchOfLine)){
                taiQuArray= new String[]{"四河沟二台区","四河沟组","椿树沟组","潘家河光伏","潘家河村",
                        "艾家庄","码子组"};
            }

        }

    }

    public int sumOfNum(int[] nums){
        int sum = 0;
        for (int i=0;i<nums.length;i++){
            sum = sum + nums[i];
        }
        return sum;
    }
}
