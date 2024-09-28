package fomichev.prak_4;

import java.util.List;
public class PenguinListWrapper {
    private List<Penguin> cats;
    public List<Penguin> getPenguins() {
        return cats;
    }
    
    public void setPenguins(List<Penguin> cats) {
        this.cats = cats;
    }
}