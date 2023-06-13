package warehouse;

import port.Package;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Warehouse {

    private int id = 1;
    private ConcurrentLinkedQueue<Package> packages;
    private Distributor distributor;

    public Warehouse(){
        distributor = new Distributor(this, "Distributor "+id);
        this.id++;
        packages = new ConcurrentLinkedQueue<>();
        new Thread(distributor).start();
    }

    public void addPackage(Package p){
        packages.offer(p);
        synchronized (this){
            notifyAll();
        }
    }

    public Package removePackage(){
        System.out.println(distributor.getName() + " removing package...");
        return packages.poll();
    }

    public boolean isEmpty(){
        return packages.isEmpty();
    }

    public void deliver() {
        while (isEmpty()){
            try {
                synchronized (this){
                    wait();
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        removePackage();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
