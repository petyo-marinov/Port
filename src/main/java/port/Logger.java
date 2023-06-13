package port;

import db.DBConnector;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class Logger extends Thread{

    private static int statsCounter = 1;

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listAllPackagesPerDock();
        }
    }

    private void listAllPackagesPerDock() {
        TreeMap<Integer, TreeSet<Shipment>> stats = new TreeMap<>();
        Connection connection = DBConnector.getInstance().getConnection();
        String sql = "SELECT boat_name, dock_id, crane_id, unloading_time, package_id" +
                " FROM port_shipments;";
        try (PreparedStatement ps = connection.prepareStatement(sql);){
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                Shipment shipment = new Shipment(
                        resultSet.getInt("package_id"),
                        resultSet.getInt("dock_id"),
                        resultSet.getInt("crane_id"),
                        resultSet.getString("boat_name"),
                        resultSet.getTimestamp("unloading_time").toLocalDateTime());
                if(!stats.containsKey(shipment.getDockId())){
                    stats.put(shipment.getDockId(), new TreeSet<>());
                }
                stats.get(shipment.getDockId()).add(shipment);
            }
            for (Map.Entry<Integer, TreeSet<Shipment>> e : stats.entrySet()){
                System.out.print("Dock " + e.getKey() + " : ");
                for (Shipment s : e.getValue()){
                    System.out.println(s);
                }
            }
            saveToFile(stats);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveToFile(TreeMap<Integer, TreeSet<Shipment>> stats) {
        try(PrintWriter pr = new PrintWriter("report-" + statsCounter++ + "-"
                +LocalDateTime.now()
                .toString()
                .replace(":", "-")
                .replace("\\.", "-")
                + ".txt")){
            for (Map.Entry<Integer, TreeSet<Shipment>> e : stats.entrySet()){
                pr.print("Dock " + e.getKey() + " : ");
                for (Shipment s : e.getValue()){
                    pr.println(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
