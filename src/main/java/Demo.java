import port.Crane;
import port.Logger;
import port.Port;
import port.ShipProducer;

public class Demo {

    public static void main(String[] args) throws InterruptedException {

        Port port = new Port("Burgas");
        Crane crane1 = new Crane(1);
        Crane crane2 = new Crane(2);
        Crane.port = port;
        ShipProducer.port = port;

        crane1.start();
        crane2.start();
        Thread.sleep(2000);
        new ShipProducer().start();
        Logger logger = new Logger();
        logger.setDaemon(true);
        logger.start();
    }
}
