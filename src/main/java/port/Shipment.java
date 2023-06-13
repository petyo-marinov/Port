package port;

import java.time.LocalDateTime;

public class Shipment {

    private int packageId;
    private int dockId;
    private int craneId;
    private String shipName;
    private LocalDateTime shipmentTime;

    public Shipment(int packageId, int dockId, int craneId, String shipName, LocalDateTime shipmentTime) {
        this.packageId = packageId;
        this.dockId = dockId;
        this.craneId = craneId;
        this.shipName = shipName;
        this.shipmentTime = shipmentTime;
    }

    public int getPackageId() {
        return packageId;
    }

    public int getDockId() {
        return dockId;
    }

    public int getCraneId() {
        return craneId;
    }

    public String getShipName() {
        return shipName;
    }

    public LocalDateTime getShipmentTime() {
        return shipmentTime;
    }
}
