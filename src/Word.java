import java.util.*;

public class Word
{
    String word;
    String word_en;
    String type;
    List<String> singular;
    List<String> plural;

    public Word()
    {
        this.singular = new ArrayList<>();
        this.plural = new ArrayList<>();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord_en() {
        return word_en;
    }

    public void setWord_en(String word_en) {
        this.word_en = word_en;
    }

    public void setType(String type) {
        this.type = type;
    }
}
