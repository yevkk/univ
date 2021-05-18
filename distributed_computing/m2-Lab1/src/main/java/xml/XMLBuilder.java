package xml;

import appdata.DataStorage;

public abstract class XMLBuilder {
    protected DataStorage<?> dataStorage;

    public XMLBuilder() {}

    public DataStorage<?> getDataStorage() {
        return dataStorage;
    }

    public abstract void buildDataStorage(String filename);

    public abstract void saveDataStorage(String filename);
}