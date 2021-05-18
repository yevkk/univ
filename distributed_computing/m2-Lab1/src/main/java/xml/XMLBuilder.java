package xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class XMLBuilder <T> {
    protected List<T> list;

    public XMLBuilder() {
        list = new ArrayList<>();
    }

    public List<T> getList() {
        return list;
    }

    public abstract void buildList(String filename);
}