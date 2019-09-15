package mainPackage.models;

public enum ColumnType {
    KEY,
    ET,
    EN,
    RU;

    public static ColumnType getByIndex(int index){
       if(index == 0){
           return ColumnType.KEY;
       }
        if(index == 1){
            return ColumnType.ET;
        }
        if(index == 2){
            return ColumnType.EN;
        }
        if(index == 3){
            return ColumnType.RU;
        }
        return null;
    }
}
