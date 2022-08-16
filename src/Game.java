public class Game {


    private String level;
    private int chances;
    private int numberOfWords;


    public Game(String level, int chances, int numberOfWords) {
        this.level = level;
        this.chances = chances;
        this.numberOfWords = numberOfWords;
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getChances() {
        return chances;
    }

    public void setChances(int chances) {
        this.chances = chances;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }


}
