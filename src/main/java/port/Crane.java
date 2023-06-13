package port;

public class Crane extends Thread{

    public static Port port;
    private int number;

    public Crane(int number){
        this.number = number;
    }

    @Override
    public void run() {
        while (true) {
            try {
                port.unload(this);
            } catch (Exception e) {
                System.out.println("Kofti - " + e.getMessage());
                return;
            }
        }
    }

    public int getNumber() {
        return number;
    }
}
