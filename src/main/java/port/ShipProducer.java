package port;

public class ShipProducer extends Thread{

    public static Port port;

    @Override
    public void run() {
        int cnt = 1;
        while (true){
            Ship ship = new Ship("Ship " + cnt++ +".");
            port.addShip(ship);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
