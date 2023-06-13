package port;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ship {

    private List<Package> packages;
    private String name;

    public Ship(String name){
        this.name = name;
        packages = new ArrayList<>();
        int number = util.Randomizer.getRandomNumberBetween(1,4);
        for (int i = 0; i < number; i++) {
            this.packages.add(new Package());
        }
    }

    public List<Package> getPackages() {
        return Collections.unmodifiableList(packages);
    }

    public String getName() {
        return name;
    }
}
