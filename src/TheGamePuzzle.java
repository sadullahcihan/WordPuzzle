import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.core.Enigma;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class TheGamePuzzle {
	private static enigma.console.Console enigmaScreen = Enigma.getConsole("Ekran", 175, 40, 15); // ("name",x,y,font)
	private KeyListener keyPress;
	private int keypr; // key pressed?
	private int rkey; // key (for press/release)
	private int cursorx;
	private int cursory;
	private boolean gameOver = false;
	private static String[] allfile;
	private static int lineamount;
	private static String[] splittedData;
	private String[][] puzzle;
	private String[][] solution;
	private String[] wordList;
	private SingleLinkedList wordListSequent;
	private MultiLinkedList mllWordList;
	private char[] firstLetters;
	private Player p1 = new Player("Player 1");
	private Player p2 = new Player("Player 2");
	private SingleLinkedList solutionSll;
	private DoubleLinkedList highScoreDll;
	private Scanner sc = new Scanner(System.in);

	public TheGamePuzzle() throws Exception {
		puzzle = new String[15][15];
		solution = new String[15][15];
		wordList = new String[100];
		wordListSequent = new SingleLinkedList();
		mllWordList = new MultiLinkedList();
		firstLetters = new char[30];
		solutionSll = new SingleLinkedList();
		highScoreDll = new DoubleLinkedList();
		for (int i = 0; i < puzzle.length; i++)
			for (int j = 0; j < puzzle[i].length; j++)
				puzzle[i][j] = ".";
		keyPress = new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (keypr == 0) {
					keypr = 1;
					rkey = e.getKeyCode();
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		};
		enigmaScreen.getTextWindow().addKeyListener(keyPress);
		enigmaScreen.getTextWindow().setCursorPosition(0, 0);
		System.out.println("*WORD PUZZLE GAME*");
		enigmaScreen.getTextWindow().setCursorPosition(0, 1);
		System.out.print("Please enter first player's name: ");
		p1.setName(sc.nextLine());
		;
		enigmaScreen.getTextWindow().setCursorPosition(0, 2);
		System.out.print("Please enter second player's name: ");
		p2.setName(sc.nextLine());
		enigmaScreen.getTextWindow().setCursorPosition(0, 1);
		System.out.print("                                                "); // clears console
		enigmaScreen.getTextWindow().setCursorPosition(0, 2);
		System.out.print("                                                "); // clears console
		cursorx = cursory = 2;
		readAllRequiredFiles(); // reads all txt_files
		getFirstLetters(); // creates first letter's array
		createSolutionSll(); // appends solution words to solutionSll
		enigmaScreen.getTextWindow().setCursorPosition(62, 1); // printing word list
		System.out.println("***WORD LIST***");
		printWordListSll(" ");
		printScreen();
		do {
			int p1Score;
			do {
				enigmaScreen.getTextWindow().setCursorPosition(22, 1);
				System.out.print("                                 "); // clears console
				enigmaScreen.getTextWindow().setCursorPosition(22, 1);
				System.out.print("Turn: " + p1.getName());
				p1Score = p1.getScore();
				thePuzzleGame(p1); // The whole game and processes are in this function
				printScreen();
				isAllWordsPlaced(); // End of the game control
				if (gameOver)
					break;
			} while (p1.getScore() > p1Score); // If_the player increased their point turn continues
			if (gameOver)
				break;
			int p2Score;
			do {
				enigmaScreen.getTextWindow().setCursorPosition(22, 1);
				System.out.print("                                 "); // clears console
				enigmaScreen.getTextWindow().setCursorPosition(22, 1);
				System.out.print("Turn: " + p2.getName());
				p2Score = p2.getScore();
				thePuzzleGame(p2); // The whole game and processes are in this function
				printScreen();
				isAllWordsPlaced();// If_all words completed, the game ends
				if (gameOver)
					break;
			} while (p2.getScore() > p2Score); // If_the player increased their point turn continues
			if (gameOver)
				break;
			isAllWordsPlaced();// If_all words completed, the game ends
		} while (!gameOver);
		enigmaScreen.getTextWindow().setCursorPosition(22, 4);
		if (p1.getScore() > p2.getScore()) {
			System.out.println("Winner is -> " + p1.getName());
			highScoreTable(p1); // Winner is added to high-score table
		} else if (p1.getScore() < p2.getScore()) {
			System.out.println("Winner is -> " + p2.getName());
			highScoreTable(p2); // Winner is added to high-score table
		} else {
			System.out.println("DRAW..."); // Players are added to high-score table
			highScoreTable(p1);// The function prints high score table and writes player's score to txt_file
			highScoreTable(p2);
		}
	}

	private void printScreen() {
		enigmaScreen.getTextWindow().setCursorPosition(0, 1);
		System.out.println("  012345678901234");
		for (int i = 0; i < puzzle.length; i++) {
			if (i < 9)
				System.out.print(" " + i);
			else if (i == 9)
				System.out.print(" 9");
			else
				System.out.print(" " + i % 10);
			for (int j = 0; j < puzzle[i].length; j++)
				System.out.print(puzzle[i][j]);
			System.out.println();
		}
		enigmaScreen.getTextWindow().setCursorPosition(22, 2);
		System.out.print("                                    "); // cleans old expressions
		enigmaScreen.getTextWindow().setCursorPosition(22, 2); // prints scores
		System.out.print("Score: " + p1.getName() + " = " + p1.getScore() + " " + p2.getName() + " = " + p2.getScore());
	}

	@SuppressWarnings("resource")
	public static void readFile(String str) throws Exception {
		File fileDir = new File(str);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
		lineamount = 0;
		while (in.readLine() != null) { // Gets line amount
			lineamount++;
		}
		String line;
		allfile = new String[lineamount];
		in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"));
		int i = 0; // upper line: it converts Turkish_chars. ^^^^^^^^
		while ((line = in.readLine()) != null) {
			allfile[i] = line;
			i++;
		}
		in.close();
	}

	private void readAllRequiredFiles() throws Exception {
		readFile("puzzle.txt"); // Reading file
		for (int i = 0; i < allfile.length; i++) {
			splittedData = allfile[i].split("\t");
			for (int j = 0; j < splittedData.length; j++) {
				if (splittedData[j].equals("1"))
					puzzle[i][j] = ".";
				else if (splittedData[j].equals("0"))
					puzzle[i][j] = "#";
				else
					puzzle[i][j] = splittedData[j];
			}
		}
		readFile("solution.txt"); // Reading file
		for (int i = 0; i < allfile.length; i++) {
			splittedData = allfile[i].split("\t");
			for (int j = 0; j < splittedData.length; j++) {
				if (splittedData[j].equals("1"))
					solution[i][j] = ".";
				else if (splittedData[j].equals("0"))
					solution[i][j] = "#";
				else
					solution[i][j] = splittedData[j];
			}
		}
		readFile("word_list.txt"); // Reading file // *****SLL AND MLL PART*****
		for (int i = 0; i < allfile.length; i++)
			wordList[i] = allfile[i];
		for (int i = 0; i < wordList.length; i++) {
			if (wordList[i] != null)
				wordListSequent.addAlphebetically(wordList[i]); // words are added to sll_Falphebetically
		}
		for (int i = 97; i <= 122; i++) { // ascii_chars a to z
			mllWordList.addParent((char) i); // Added all alphabet as parent
			SingleLinkedList.Node temp = wordListSequent.getHead();
			while (temp != null) {
				if ((char) i == (((String) temp.getData()).charAt(0))) // Added words according to its first letter
					mllWordList.addChild((char) i, temp.getData());
				temp = temp.getLink();
			}
		}
		readFile("high_score_table.txt");
		for (int i = 0; i < allfile.length; i++) {
			highScoreDll.addToTheEnd(allfile[i]); // Reads high score table
		}
	}

	public void getFirstLetters() { // The function gets all words's first letters to create solutionSLL easily
		int k = 0;// gets first letters of the solution
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[i].length; j++) {
				if (97 <= puzzle[i][j].charAt(0) && (int) puzzle[i][j].charAt(0) <= 122) { // ascii_ chars a to z
					firstLetters[k] = puzzle[i][j].charAt(0);
					k++;
				}
			}
		}
	}

	public void isAllWordsPlaced() { // The Game end control
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[i].length; j++) {
				if (puzzle[i][j] == ".") {
					gameOver = false;
					break;
				}
				if (puzzle[i][j] != ".") {
					gameOver = true;
				}
			}
			if (!gameOver) {
				break;
			}
		}
	}

	public void createSolutionSll() { // solutionSLL is created from solution matrix
		for (int i = 0; i < solution.length; i++) {// Answer word list is created here.
			for (int j = 0; j < solution[i].length; j++) {
				for (int m = 0; m < firstLetters.length; m++) {
					Boolean isFinded = false; // searches first letters
					if (solution[i][j].charAt(0) == firstLetters[m]) {
						if ((j + 1) <= 14 && puzzle[i][j + 1] == ".") { // horizontal word
							String answer = ""; // boundary controls (14 or 15)
							int tempi = i;
							int tempj = j;
							while (tempj < 15 && solution[tempi][tempj] != "#") {
								answer = answer.concat(solution[tempi][tempj]);
								tempj++;
							}
							if (mllWordList.searchWord(answer)) {
								solutionSll.addAlphebetically(answer);
								isFinded = true;
							}
						}
						if ((i + 1) <= 14 && puzzle[i + 1][j] == ".") { // vertical word
							String answer = ""; // boundary controls (14 or 15)
							int tempi = i;
							int tempj = j;
							while (tempi < 15 && solution[tempi][tempj] != "#") {
								answer = answer.concat(solution[tempi][tempj]);
								tempi++;
							}
							if (mllWordList.searchWord(answer)) {
								solutionSll.addAlphebetically(answer);
								isFinded = true;
							}
						}
						if (isFinded) {
							break;
						}
					}
				}
			}
		}
	}

	public void printWordListSll(Object completedWord) {
		SingleLinkedList.Node head = wordListSequent.getHead(); // Prints 97 words
		if (head == null) {
			System.out.println("Linked list is empty...");
		} else {
			SingleLinkedList.Node temp = head;
			int x = 0;
			int y = 0;
			while (temp != null) {
				if (isCorrectWord(completedWord) && completedWord.equals(((String) temp.getData()).split(",")[0])) {
					enigmaScreen.getTextWindow().setCursorPosition(62 + x, 2 + y); // if word is correct then prints X
					System.out.println("[X]" + ((String) temp.getData()).split(",")[0] + " ");
				} else {
					enigmaScreen.getTextWindow().setCursorPosition(62 + x, 2 + y);
					System.out.print("[");
					enigmaScreen.getTextWindow().setCursorPosition(64 + x, 2 + y);
					System.out.println("]" + ((String) temp.getData()).split(",")[0] + " ");
				}
				temp = temp.getLink();
				y++;
				if (y % 15 == 0) { // to write next to next
					x = x + 16;
					y = 0;
				}
			}
		}
	}

	public boolean isCorrectWord(Object completedWord) { // Completed word control from solutionSLL
		boolean isCorrect = false;
		SingleLinkedList.Node head = solutionSll.getHead(); // it was created from solution matrix
		if (head == null) {
			System.out.println("Linked list is empty...");
		} else {
			SingleLinkedList.Node temp = head;
			while (temp != null) {
				if (completedWord.equals((String) temp.getData())) { // if word is_finded, flag is true and break
					isCorrect = true;
					break;
				}
				temp = temp.getLink();
			}
		}
		return isCorrect;
	}

	private void thePuzzleGame(Player p) throws InterruptedException { // Cursor movements
		enigmaScreen.getTextWindow().setCursorType(1);
		while (true) {
			enigmaScreen.getTextWindow().setCursorPosition(cursorx, cursory);
			if (keypr == 1) {
				if (cursorx < 2)
					cursorx = 2; // left limit
				if (cursorx > 16)
					cursorx = 16; // right limit
				if (cursory < 2)
					cursory = 2; // up limit
				if (cursory > 16)
					cursory = 16; // down limit
				if (rkey == KeyEvent.VK_LEFT)
					cursorx--;
				if (rkey == KeyEvent.VK_RIGHT)
					cursorx++;
				if (rkey == KeyEvent.VK_UP)
					cursory--;
				if (rkey == KeyEvent.VK_DOWN)
					cursory++;
				if (65 <= rkey && rkey <= 90) { // ascii_chars A to Z (65 to 90)
					rkey = rkey + 32; // to_lowercase printing (a to z == 97 to 122) //97-65=32
					if (puzzle[cursory - 2][cursorx - 2].equals(".")) { // if missing letter of the word
						puzzle[cursory - 2][cursorx - 2] = Character.toString((char) rkey);
						enigmaScreen.getTextWindow().setCursorPosition(cursorx, cursory);
						System.out.print((char) rkey);
						letterControls(p); // The whole letter control is in this function
						keypr = 0;
						break;
					}
				}
				keypr = 0;
			}
			enigmaScreen.getTextWindow().setCursorPosition(cursorx, cursory);
		}
	}

	public void letterControls(Player p) { // ALL LETTER CONTROLS
		int tempcx = cursorx;
		int tempcy = cursory;
		String h_WordPattern = "";// horizontal word pattern
		String v_WordPattern = "";// vertical word pattern
		int headLocationX = 0; // if completed word is not correct, ...
		int headLocationY = 0; // head location is kept to delete the wrong letters.
		while (puzzle[tempcy - 2][tempcx - 2] != "#") { // temp_cursor goes to the head letter
			if (tempcx <= 2) {
				tempcx = 2;
				break;
			}
			tempcx--;
		}
		if (tempcx < 16 && tempcx != 2) // boundry_controls
			tempcx++; // head letter location
		headLocationX = tempcx;// horizontal head location is taken
		headLocationY = tempcy;
		if (tempcx != cursorx) { // It_means word is horizontal or not?
			while (puzzle[tempcy - 2][tempcx - 2] != "#") {
				h_WordPattern = h_WordPattern.concat(puzzle[tempcy - 2][tempcx - 2]);
				tempcx++;// horizontal
				if (tempcx > 16) {// boundry_controls
					tempcx = 16;
					break;
				}
			}
		} // horizontal word pattern is ready now
		tempcx = cursorx; // Turns back to input letter's location
		while (puzzle[tempcy - 2][tempcx - 2] != "#") { // temp_ cursor goes to the head letter
			if (tempcy <= 2) {// boundry_controls
				tempcy = 2;
				break;
			}
			tempcy--;
		}
		if (tempcy < 16 && tempcy != 2) // boundry_controls
			tempcy++; // head letter location
		if (h_WordPattern.contains(".") || h_WordPattern == "") { // means word is vertical
			headLocationX = tempcx; // vertical head location is taken
			headLocationY = tempcy;
		}
		if (tempcy != cursory) { // It_means word is vertical or not?
			while (puzzle[tempcy - 2][tempcx - 2] != "#") {
				v_WordPattern = v_WordPattern.concat(puzzle[tempcy - 2][tempcx - 2]);
				tempcy++;
				if (tempcy > 16) {// boundry_controls
					tempcy = 16;
					break;
				}
			}
		} // vertical word pattern is ready now
		if (h_WordPattern.contains(".") && v_WordPattern.contains(".") && mllWordList.searchWord(h_WordPattern)
				&& mllWordList.searchWord(v_WordPattern)) { // This condition just occurs when first letter is given
			p.setScore(p.getScore() + 1); // common letter exists
			enigmaScreen.getTextWindow().setCursorPosition(2, 17);
			System.out.print("                                                                    "); // clears console
			enigmaScreen.getTextWindow().setCursorPosition(2, 17);
			System.out.print(p.getName() + " gets 1 point");
		} else if (h_WordPattern.length() > 1 && mllWordList.searchWord(h_WordPattern)) {
			p.setScore(p.getScore() + 1);
			enigmaScreen.getTextWindow().setCursorPosition(2, 17);
			System.out.print("                                                                    "); // clears console
			enigmaScreen.getTextWindow().setCursorPosition(2, 17);
			System.out.print(p.getName() + " gets 1 point");
		} else if (v_WordPattern.length() > 1 && mllWordList.searchWord(v_WordPattern)) {
			p.setScore(p.getScore() + 1);
			enigmaScreen.getTextWindow().setCursorPosition(2, 17);
			System.out.print("                                                                    "); // clears console
			enigmaScreen.getTextWindow().setCursorPosition(2, 17);
			System.out.print(p.getName() + " gets 1 point");
		} else { // word is not correct. Deletes all given letter
			puzzle[cursory - 2][cursorx - 2] = ".";
		}
		if (!h_WordPattern.contains(".") && h_WordPattern != "" && h_WordPattern.length() > 2) {
			boolean isAllCorrect = false; // Condition means_last letter control from solution matrix^^
			tempcx = headLocationX;
			tempcy = headLocationY;
			for (int i = 0; i < h_WordPattern.length(); i++) {
				if (h_WordPattern.charAt(i) == solution[tempcy - 2][tempcx - 2].charAt(0)) {
					isAllCorrect = true;
				}
				if (h_WordPattern.charAt(i) != solution[tempcy - 2][tempcx - 2].charAt(0)) {
					isAllCorrect = false;
					break;
				}
				tempcx++;
			}
			if (isAllCorrect) { // if word is correct player gets 10 points
				p.setScore(p.getScore() + 10);
				enigmaScreen.getTextWindow().setCursorPosition(2, 22);
				System.out.print("                                           ");
				enigmaScreen.getTextWindow().setCursorPosition(2, 22);
				System.out.print(
						"The word '" + h_WordPattern + "' is placed correctly." + p.getName() + " gets 10 points.");
				turkishMeaning(h_WordPattern, p); // The function asks completed word's Turkish_Meaning
				mllWordList.deleteChild(h_WordPattern.charAt(0), h_WordPattern); // Word is deleted from MLL
			} else { // deletes all the wrong letters
				tempcx = headLocationX;
				tempcy = headLocationY;
				for (int i = 0; i < h_WordPattern.length() - 1; i++) {
					tempcx++;
					puzzle[tempcy - 2][tempcx - 2] = ".";
				}
				p.setScore(p.getScore() - (h_WordPattern.length() * 2)); // penalty points
			}
			printWordListSll(h_WordPattern); // word is X'ed to word list
		}
		if (!v_WordPattern.contains(".") && v_WordPattern != "" && v_WordPattern.length() > 2) {
			boolean isAllCorrect = false; // Condition means_last letter control from solution matrix^^
			tempcx = headLocationX;
			tempcy = headLocationY;
			for (int i = 0; i < v_WordPattern.length(); i++) {
				if (v_WordPattern.charAt(i) == solution[tempcy - 2][tempcx - 2].charAt(0)) {
					isAllCorrect = true;
				}
				if (v_WordPattern.charAt(i) != solution[tempcy - 2][tempcx - 2].charAt(0)) {
					isAllCorrect = false;
					break;
				}
				tempcy++;
			}
			if (isAllCorrect) { // if word is correct player gets 10 points
				p.setScore(p.getScore() + 10);
				enigmaScreen.getTextWindow().setCursorPosition(2, 22);
				System.out.print("                                           ");
				enigmaScreen.getTextWindow().setCursorPosition(2, 22);
				System.out.print(
						"The word '" + v_WordPattern + "' is placed correctly." + p.getName() + " gets 10 points.");
				turkishMeaning(v_WordPattern, p); // The function asks completed word's Turkish_Meaning
				mllWordList.deleteChild(v_WordPattern.charAt(0), v_WordPattern); // Word is deleted from MLL
			} else { // deletes all the wrong letters
				tempcx = headLocationX;
				tempcy = headLocationY;
				for (int i = 0; i < v_WordPattern.length() - 1; i++) {
					tempcy++;
					puzzle[tempcy - 2][tempcx - 2] = ".";
				}
				p.setScore(p.getScore() - (v_WordPattern.length() * 2)); // penalty points
			}
			printWordListSll(v_WordPattern); // word is X'ed to word list
		}
		printScreen();
	}

	public void turkishMeaning(String answerENG, Player p) { // The function asks completed word's Turkish_Meaning
		printScreen();
		enigmaScreen.getTextWindow().setCursorPosition(2, 23);
		System.out.print("What is the meaning of " + answerENG + " in Turkish? Please enter your option.");
		String answer = mllWordList.findWordTurkishMeaning(answerENG);
		String r1, r2;
		do {
			r1 = mllWordList.findRandomTurkishWord(answerENG);
			r2 = mllWordList.findRandomTurkishWord(answerENG); // Gets completely different options
		} while (r1.equalsIgnoreCase(r2) || r1.equalsIgnoreCase(answer) || r2.equalsIgnoreCase(answer));
		enigmaScreen.getTextWindow().setCursorPosition(2, 24);
		System.out.print("                                                   ");
		int random = (int) (Math.random() * 6 + 1); // random options are created
		enigmaScreen.getTextWindow().setCursorPosition(2, 24);
		switch (random) { // random option orders
		case 1:
			System.out.print("a)" + answer + " b)" + r1 + " c)" + r2);
			break;
		case 2:
			System.out.print("a)" + answer + " b)" + r2 + " c)" + r1);
			break;
		case 3:
			System.out.print("a)" + r2 + " b)" + answer + " c)" + r1);
			break;
		case 4:
			System.out.print("a)" + r1 + " b)" + answer + " c)" + r2);
			break;
		case 5:
			System.out.print("a)" + r1 + " b)" + r2 + " c)" + answer);
			break;
		case 6:
			System.out.print("a)" + r2 + " b)" + r1 + " c)" + answer);
			break;
		}
		enigmaScreen.getTextWindow().setCursorPosition(2, 25);
		System.out.print("                                  "); // clears console
		enigmaScreen.getTextWindow().setCursorPosition(2, 26);
		System.out.print("                                                               "); // clears console
		enigmaScreen.getTextWindow().setCursorPosition(2, 25);
		System.out.print("Answer:");
		String input = sc.nextLine(); // the answer is asked
		enigmaScreen.getTextWindow().setCursorPosition(2, 26);
		if (input.length() == 1 && input.equalsIgnoreCase("a") && (random == 1 || random == 2)) {
			p.setScore(p.getScore() + 10); // correct answer controls
			System.out.print("Answer is correct. " + p.getName() + " gets extra 10 points. ");
		} else if (input.length() == 1 && input.equalsIgnoreCase("b") && (random == 3 || random == 4)) {
			p.setScore(p.getScore() + 10);
			System.out.print("Answer is correct. " + p.getName() + " gets extra 10 points. ");
		} else if (input.length() == 1 && input.equalsIgnoreCase("c") && (random == 5 || random == 6)) {
			p.setScore(p.getScore() + 10);
			System.out.print("Answer is correct. " + p.getName() + " gets extra 10 points. ");
		} else {
			System.out.print("Answer is not correct                                        ");
		}
		printScreen();
	}

	public void highScoreTable(Player p) { // Prints high score table and writes player's score to txt_file
		enigmaScreen.getTextWindow().setCursorPosition(75, 21);
		System.out.println("***HIGH-SCORE TABLE***");
		highScoreDll.addBetween((String) p.getName() + ";" + p.getScore());
		DoubleLinkedList.Node head = highScoreDll.getHead();
		DoubleLinkedList.Node temp = head;
		String newHighScores = "";
		int i = 0;
		while (temp != null) {
			enigmaScreen.getTextWindow().setCursorPosition(75, 22 + i); // printing
			System.out.println(i + 1 + "-" + temp.getData().toString().split(";")[0] + " -> "
					+ temp.getData().toString().split(";")[1]);
			String str = temp.getData().toString();
			System.out.println();
			if (temp.getNext() != null)
				newHighScores += str + "\r\n"; // players
			else
				newHighScores += str; // last player
			temp = temp.getNext();
			i++;
		}
		writeToFile("high_score_table.txt", newHighScores); // new player and old players are added to txt_file
	}

	public static void writeToFile(String path, String newTxtContent) {
		try {
			FileOutputStream outputStream = new FileOutputStream(path);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8"); // turns UTF-8
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			bufferedWriter.write(newTxtContent); // writes to file
			bufferedWriter.close();
		} catch (Exception e) {
			System.out.println("Error!");
		}
	}
}