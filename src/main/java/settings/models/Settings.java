package settings.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Settings implements java.io.Serializable{
    private double windowX, windowY, stageW, stageH;
    boolean sortJsonKeys;

    public Settings(){
        this.stageH = 500;
        this.stageW = 800;
        this.windowX = 0;
        this.windowY = 0;
        this.sortJsonKeys = true;
    }

    public String toString(){
        return "settings object [" +
                "H: " + stageH + ", " +
                "W: " + stageW + ", " +
                "X: " + windowX + ", " +
                "Y: " + windowY +
                "]";
    }

}
