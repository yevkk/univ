package entity;

public class Region extends Entity {
    private String name;
    private double square;
    private int nationID;

    public Region(int id, String name, double square, int nationID) {
        super(id);
        this.name = name;
        this.square = square;
        this.nationID = nationID;
    }

    public String getName() {
        return name;
    }

    public double getSquare() {
        return square;
    }

    public int getNationID() {
        return nationID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public void setNationID(int nationID) {
        this.nationID = nationID;
    }

}
