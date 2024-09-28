package fomichev.prak_4;

import java.util.List;
public class PenguinListWrapper {
    private List<Penguin> penguins;
    public List<Penguin> getPenguins() {
        return penguins;
    }
    
    public void setPenguins(List<Penguin> penguins) {
        this.penguins = penguins;
    }
}