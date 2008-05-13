package grammar;

public class KeyWordException extends Exception {
    private String keyWord;

    public KeyWordException(String keyWord) {
        this.keyWord = keyWord;
    }
    public String getKeyWord() {
        return keyWord;
    }
}