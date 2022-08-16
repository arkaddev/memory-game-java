import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final List<String> wordList = new ArrayList<>();
    public static Game game;
    public static User user;


    public static String[] wordsArray;
    public static String[] wordsArrayMix;

    static int[] goodAnswerA;
    static int[] goodAnswerB;


    // menu with a choice of the game level
    public static void main(String[] args) {

        inputA = 0;
        inputB = 0;

        ascii("W E L C O M E");
        System.out.println();

        System.out.println("Select level: ");
        System.out.println("1 - EASY");
        System.out.println("2 - HARD");
        readFile();

        Scanner in = new Scanner(System.in);
        int level = in.nextInt();
        user = new User();
        if (level == 1) {
            game = new Game("easy", 10, 4);
            user.setChancesOfUser(game.getChances());
            gameEngine();
        } else if (level == 2) {
            game = new Game("hard", 15, 8);
            user.setChancesOfUser(game.getChances());
            gameEngine();
        } else System.out.println("Error! There is no such level.");
    }

    // load txt file and add each line to the list
    public static void readFile() {

        try {
            File file = new File("Words.txt"); // reading the file
            Scanner readWordFile = new Scanner(file);
            while (readWordFile.hasNextLine()) {
                String oneWord = readWordFile.nextLine();
                wordList.add(oneWord); // adding to the list
            }
            readWordFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    public static void gameEngine() {

        selectWords(game.getNumberOfWords()); // randomizing words and storing them in an array (A)
        mixWordsArray(wordsArray); // based on the previously created array, mixing the array (B)

        System.out.println(check()); //checking if 2 words are the same or not

        // information after the end of the game, if the user has won
        if (user.getChancesOfUser() > 0) {
            System.out.println("You solved the memory game after " + (chancesOfUser()) + " chances.");
            System.out.println("It took you: " + timeOfGame() + " s.");
        }

        // saving the results in case the user won
        if (user.getChancesOfUser() > 0) {
            saveUsername();
        }

        createHighScoreObject(); // displaying the best results
        showHighScore();
        newGame(); // the function asks if you want to start a new game
    }

    // the function randomizes the number of words according to the assumed parameter of the function
    // then the word is added to the array, then removed from the list so that it will not be drawn again

    public static void selectWords(int numbers) {

        wordsArray = new String[numbers];
        List<String> wordList2 = wordList;
        Random random = new Random(); // generates random integer

        for (int i = 0; i < numbers; i++) {
            int drawnNumber = random.nextInt(wordList2.size()); // draw a number from 0 to wordCounter
            String phraze = wordList2.get(drawnNumber);
            wordList2.remove(drawnNumber);

            wordsArray[i] = phraze;
        }
    }

    // on the basis of the table with previously drawn words, an additional table is created with randomly ordered words
    public static void mixWordsArray(String[] array) {
        wordsArrayMix = new String[array.length];
        List<String> aList = new ArrayList<>();

        for (int i = 0; i < wordsArray.length; i++) {
            aList.add(wordsArray[i]);
        }

        Random random = new Random();
        for (int i = 0; i < wordsArray.length; i++) {
            int drawnNumber = random.nextInt(aList.size());
            String phraze = aList.get(drawnNumber);
            aList.remove(drawnNumber);
            wordsArrayMix[i] = phraze;

        }

    }

    static long startTime;
    static long endTime;

    //checking if 2 words are the same or not
    public static String check() {

        goodAnswerA = new int[wordsArray.length]; // array for information about the results
        goodAnswerB = new int[wordsArray.length];

        startTime = System.currentTimeMillis();

        while (user.getChancesOfUser() > 0) {

            sampleOfGame();
            loadInputA();
            sampleOfGame();
            loadInputB();

            if (wordsArray[inputA - 1] == wordsArrayMix[inputB - 1]) {

                ascii("G O O D");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                goodAnswerA[inputA - 1] = 1; // sets the value to 1 when the A and B coordinates are the same
                goodAnswerB[inputB - 1] = 1;

                // if the sum of the elements of array is equal to the words drawn earlier, the game is won
                if (sumOfAnswerArray() == wordsArray.length) {
                    endTime = System.currentTimeMillis();

                    System.out.println();
                    ascii("Y O U  W O N");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return "Statistics: ";
                }

                // the program is halted so that the user can remember the wrong words

            } else {
                user.setChancesOfUser(user.getChancesOfUser() - 1);

                sampleOfGame();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                inputA = 0;
                inputB = 0;
            }
        }
        endTime = System.currentTimeMillis();
        return "No more chances";
    }

    // adds an element to an array and returns its result
    public static int sumOfAnswerArray() {
        int count = 0;

        for (int i = 0; i < wordsArray.length; i++) {
            count = goodAnswerA[i] + count;
        }
        return count;
    }

    // displays the game pattern, if the words can be guessed, the given word appears instead of X
    public static void sampleOfGame() {

        System.out.println("----------------------");
        System.out.println("Level: " + game.getLevel());
        System.out.println("Guess chances: " + user.getChancesOfUser());
        System.out.print(" ");

        for (int i = 0; i < game.getNumberOfWords(); i++) {
            System.out.print(" " + (i + 1));
        }

        System.out.println();

        System.out.print("A ");
        for (int i = 0; i < game.getNumberOfWords(); i++) {

            if (inputA == i + 1 || goodAnswerA[i] == 1) {
                System.out.print(wordsArray[i] + " ");
            } else
                System.out.print("X ");
        }

        System.out.println();
        System.out.print("B ");
        for (int i = 0; i < game.getNumberOfWords(); i++) {

            if (inputB == i + 1 || goodAnswerB[i] == 1) {
                System.out.print(wordsArrayMix[i] + " ");
            } else
                System.out.print("X ");
        }
        System.out.println("\n----------------------");
    }

    static int inputA = 0;
    static int inputB = 0;

    // function asks for A coordinates
    public static void loadInputA() {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the coordinate A: <1," + game.getNumberOfWords() + ">");
        inputA = in.nextInt();

    }

    // function asks for B coordinates
    public static void loadInputB() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the coordinate B: <1," + game.getNumberOfWords() + ">");
        inputB = in.nextInt();
    }

    // the function asks if you want to start a new game
    public static void newGame() {

        Scanner in = new Scanner(System.in);
        System.out.println("New game? (y/n): ");
        String input = in.nextLine();

        if (input.equals("y")) {
            main(null);
        } else ascii("S E E  Y O U !");
        ;

    }

    // returns the number of chances that the user remains
    public static int chancesOfUser() {
        return game.getChances() - user.getChancesOfUser();
    }

    // calculates the playing time
    public static String timeOfGame() {

        long result = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
        String resultString = Long.toString(result);
        return resultString;
    }

    // in case of a win, save the username
    public static void saveUsername() {

        String username;
        do {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter your name: ");
            username = in.nextLine();
            if (username.contains(" ")) {
                System.out.println("Username cannot contain spaces!");
            }
            if (username.length() > 10) {
                System.out.println("Username cannot contain more than 10 letters!");
            }

        } while (username.contains(" ") || username.length() > 10);

        user.setUsername(username);

        readResultsFile();
        saveResultsFile();
    }

    static ArrayList<String> statisticsList;

    // read from file and write to list
    public static void readResultsFile() {

        statisticsList = new ArrayList<String>();

        try {
            File resultsFile = new File("results.txt");
            Scanner readResults = new Scanner(resultsFile);
            while (readResults.hasNextLine()) {
                String userData = readResults.nextLine();
                statisticsList.add(userData);

            }
            readResults.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    // saving data to a file
    public static void saveResultsFile() {
        try {
            FileWriter saveFile = new FileWriter("results.txt");
            for (int i = 0; i < statisticsList.size(); i++) {
                String name = statisticsList.get(i);
                saveFile.write(name + "\n");
            }

            saveFile.write(user.getUsername() + " " + user.getDate() + " " + timeOfGame() + " " + chancesOfUser() + " " + game.getLevel().charAt(0));
            saveFile.close();
            System.out.println("The result is saved.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    // the function calculates the number of spaces in a string, returns a queue result
    static Queue<Integer> spacesQueue = new ArrayDeque<>();

    public static void countSpaces(String result) {

        char[] c = result.toCharArray();
        for (int i = 0; i < result.length(); i++) {

            if (c[i] == ' ') {
                spacesQueue.add(i);
            }
        }
    }

    static List<User> objectsList = new ArrayList<>();
    static List<User> objectsListLevel = new ArrayList<>();


    // splits each read string by extracting the data, then creates the objects based on the data

    public static void createHighScoreObject() {

        objectsList.clear();
        objectsListLevel.clear();

        readResultsFile();

        for (int i = 0; i < statisticsList.size(); i++) {

            countSpaces(statisticsList.get(i));

            String name = statisticsList.get(i).substring(0, spacesQueue.peek());
            String dateandTime = statisticsList.get(i).substring(spacesQueue.remove() + 1, spacesQueue.peek());
            long time = Long.parseLong(statisticsList.get(i).substring(spacesQueue.remove() + 1, spacesQueue.peek()));
            int chances = Integer.parseInt(statisticsList.get(i).substring(spacesQueue.remove() + 1, spacesQueue.peek()));
            String level = statisticsList.get(i).substring(spacesQueue.remove() + 1, statisticsList.get(i).length());

            objectsList.add(i, new User(name, time, level, chances, dateandTime));

            char a = level.charAt(0);
            char b = game.getLevel().charAt(0);

            if (a == b) {
                objectsListLevel.add(objectsList.get(i));
            }
        }
        Collections.sort(objectsListLevel);
    }

    // display the top 10 results
    public static void showHighScore() {
        System.out.println("---------------------------------------------------------");
        System.out.println("High Score");
        System.out.println("P  NAME               DATE          CHANCES          TIME");
        if (objectsListLevel.size() > 10) {
            for (int i = 0; i < 10; i++) {
                System.out.println(i + 1 + ". " + objectsListLevel.get(i));
            }
        } else {

            for (int i = 0; i < objectsListLevel.size(); i++) {
                System.out.println(i + 1 + ". " + objectsListLevel.get(i));
            }
        }
    }

    // ascii art
    public static void ascii(String graphicName) {

        int width = 100;
        int height = 30;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setFont(new Font("SansSerif", Font.TRUETYPE_FONT, 10));
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString(graphicName, 10, 20);

        for (int y = 0; y < height; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < width; x++) {
                stringBuilder.append(bufferedImage.getRGB(x, y) == -16777216 ? " " : "X");
            }
            if (stringBuilder.toString().trim().isEmpty()) {
                continue;
            }
            System.out.println(stringBuilder);
        }
    }

}