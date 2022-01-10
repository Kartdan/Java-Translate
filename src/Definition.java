import java.util.*;

public class Definition
{
    String dict;
    String dictType;
    int year;
    List<String> text;

    public Definition()
    {
        this.text = new ArrayList<>();
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
