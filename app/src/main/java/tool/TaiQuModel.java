package tool;

public class TaiQuModel {
    String line,switchOfLin,taiqu;
    public int num;

    public TaiQuModel(String line, String switchOfLine, String taiqu, int num) {
        this.line = line;
        this.switchOfLin = switchOfLine;
        this.taiqu = taiqu;
        this.num = num;
    }

    public String getLine() {
        return line;
    }

    public String getSwitchOfLin() {
        return switchOfLin;
    }

    public String getTaiqu() {
        return taiqu;
    }

    public int getNum(){return  num;}

}
