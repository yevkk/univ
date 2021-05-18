package items;

public class Airline extends Entity {
    private String name;
    private String country;

    public Airline(int id, String name, String country) {
        super(id);
        this.name = name;
        this.country = country;
    }

    public Airline(String name, String country) {
        this(-1, name, country);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
