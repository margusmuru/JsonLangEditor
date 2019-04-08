package csvmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CsvLine {
    private String key;
    private String et;
    private String ru;
    private String en;

    public CsvLine(String[] columns) {
        if(columns == null){
            return;
        }
        if(columns.length > 0){
            key = columns[0];
        }
        if(columns.length > 1){
            et = columns[1];
        }
        if(columns.length > 2){
            ru = columns[2];
        }
        if(columns.length > 3){
            en = columns[3];
        }
    }

    @Override
    public String toString() {
        return "CsvLine{" +
                "key='" + key + '\'' +
                ", et='" + et + '\'' +
                ", ru='" + ru + '\'' +
                ", en='" + en + '\'' +
                '}';
    }
}
