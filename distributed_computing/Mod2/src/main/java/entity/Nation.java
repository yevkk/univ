package entity;

public class Nation extends Entity {
    private String name;
    private String language;

    public Nation(int id, String name, String language) {
        super(id);
        this.name = name;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
