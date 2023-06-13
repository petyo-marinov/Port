package warehouse;

public class Distributor implements Runnable {

    private Warehouse warehouse;
    private String name;

    public Distributor(Warehouse warehouse, String name){
        this.warehouse = warehouse;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            warehouse.deliver();
        }
    }

    public String getName() {
        return name;
    }
}
