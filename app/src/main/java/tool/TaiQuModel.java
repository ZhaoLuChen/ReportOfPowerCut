package tool;

public class TaiQuModel {
    String station,line,switchOfLin,taiqu;
    public int num;

    public TaiQuModel(){}

    public TaiQuModel(String station,String line, String switchOfLine, String taiqu, int num) {
        this.station = station;
        this.line = line;
        this.switchOfLin = switchOfLine;
        this.taiqu = taiqu;
        this.num = num;
    }

    public TaiQuModel(String line, String switchOfLin, String taiqu, int num) {
        this.line = line;
        this.switchOfLin = switchOfLin;
        this.taiqu = taiqu;
        this.num = num;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setSwitchOfLin(String switchOfLin) {
        this.switchOfLin = switchOfLin;
    }

    public void setTaiqu(String taiqu) {
        this.taiqu = taiqu;
    }

    public void setNum(int num) {
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

    @Override
    public String toString() {
        return "TaiQuModel{" +
                "station='" + station + '\'' +
                ", line='" + line + '\'' +
                ", switchOfLin='" + switchOfLin + '\'' +
                ", taiqu='" + taiqu + '\'' +
                ", num=" + num +
                '}';
    }
}
