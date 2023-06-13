package port;

import db.DBConnector;
import util.Randomizer;
import warehouse.Warehouse;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Port {

    private String name;
    private List<Dock> docks;
    private Warehouse wh1;
    private Warehouse wh2;
    private Set<Shipment> shipments;

    public Port(String name) {
        this.name = name;
        this.shipments = new HashSet();
        wh1 = new Warehouse();
        wh2 = new Warehouse();
        this.docks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            docks.add(new Dock(i+1));
        }
    }

    public synchronized void addShip(Ship ship){
        docks.get(Randomizer.getRandomNumberBetween(0, docks.size()-1)).addShip(ship);
        System.out.println("Ship "+ ship.getName() +" added with "+ ship.getPackages().size() +" packages, all cranes notified...");
        notifyAll();
    }

    private boolean isEmpty(){
        for(Dock d : docks) {
            if(!d.isEmpty() && !d.isNowUnloading()) {
                return false;
            }
        }
        return true;
    }

    public void unload(Crane crane) throws Exception {
        Dock dock = null;
        Ship ship = null;
        synchronized (this){
            while (isEmpty()){
                System.out.println("Crane " + crane.getNumber() + " waits because port is empty");
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("i've been reawaken!");
                }
            }
            dock = getDockToUnload();
            dock.setNowUnloading(true);
            ship = dock.getNextShip();
        }
        System.out.println("Crane " + crane.getNumber() + " starts unloading ship " + ship.getName());
        for(Package p : ship.getPackages()){
            Thread.sleep(2000);
            getRandomWarehouse().addPackage(p);
            logShipment(new Shipment(p.getId(), dock.getId(), crane.getNumber(), ship.getName(), LocalDateTime.now()));
        }
        dock.removeShip();
        dock.setNowUnloading(false);
        System.out.println("Crane " + crane.getNumber() + " finishes unloading ship " + ship.getName());
    }

    private void logShipment(Shipment shipment) {
        this.shipments.add(shipment);
        addShipmentToDB(shipment);
    }

    private void addShipmentToDB(Shipment shipment) {
        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "INSERT INTO port_shipments (boat_name, dock_id, crane_id, unloading_time, package_id) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, shipment.getShipName());
            ps.setInt(2, shipment.getDockId());
            ps.setInt(3, shipment.getCraneId());
            ps.setTimestamp(4, Timestamp.valueOf(shipment.getShipmentTime()));
            ps.setInt(5, shipment.getPackageId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting into log " + e.getMessage());
        }

    }

    private Warehouse getRandomWarehouse() {
        if(new Random().nextBoolean()){
            return wh1;
        }
        else {
            return wh2;
        }
    }

    private Dock getDockToUnload() throws Exception {
        for(Dock d : docks){
            if(!d.isEmpty() && !d.isNowUnloading()){
                return d;
            }
        }
        throw new Exception("Trying to get dock for unloading from empty port");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Port port = (Port) o;
        return Objects.equals(name, port.name) && Objects.equals(docks, port.docks) && Objects.equals(wh1, port.wh1) && Objects.equals(wh2, port.wh2) && Objects.equals(shipments, port.shipments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, docks, wh1, wh2, shipments);
    }
}
