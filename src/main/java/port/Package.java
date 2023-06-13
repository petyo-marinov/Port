package port;

import warehouse.Warehouse;

public class Package {

    private int id;
    private static int uniqueId = 0;

    public Package(){
        this.id = ++uniqueId;
    }

    public int getId() {
        return id;
    }
}
