import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class App {
	public static void main(String[] args) {
		
		// Scanner to read from keyboards
		Scanner input = new Scanner(System.in);
		
		// Keep track on the top5
		ArrayList<String> top5 = new ArrayList<>();
		// The list of all the words
		ArrayList<String> wordBank = new ArrayList<>(); 
		try {
			readData("data/dictionary.txt", wordBank);
			readData("data/top5.txt", top5);
		} 
		catch (IOException e) {
			System.out.println(e.getMessage() + "\nFile not found!");
		}
		
		// Current score
		int score = 0;
		
		// Loop control: Stop loop if the user runs out of chances
		boolean outOfChance = false;
		
		System.out.println("-----HANGMAN GAME-----\n");
		while (!outOfChance) {
			//Total chances
			int guesses = 7;
			
			// Count the turn
			int turn = 0;
			
			//Take a new secret word
			String word = wordBank.get((int)(Math.random()*wordBank.size())).toUpperCase(); 
			
			// Create a dashed string
			StringBuilder dashes = new StringBuilder();
			for (int i = 0; i < word.length(); i++)
				dashes.append("_ ");
			
			String incorrectGuesses = ""; // record of incorrect guesses
			
			while (!dashes.toString().replaceAll("\\s+","").equalsIgnoreCase(word)) {
				/*
				 * Use regular expression to match spaces and remove all space 
				 * by invoking removeAll method from a String object.
				 */
				//Increment the turn count
				turn += 1;
				// Print out current hidden word in dashes (& chars if correct char is entered)
				System.out.println("Hidden words: "+ dashes);
				//Print chances left
				System.out.println("Guesses left: " + guesses);
				//Print score
				System.out.println("Score: " + score);
				
				//Print out previous incorrect guesses
				if (turn > 1) {
					System.out.println("Incorrect Guesses: " 
							+ ((incorrectGuesses.length() > 2)? 
									incorrectGuesses.substring(0,incorrectGuesses.length() - 2):""));
					System.out.print("Enter next guess: ");
				}
				else System.out.print("Enter a guess: ");
				
				//Guess from user
				char guess = ' ';
				boolean badGuess = true;
				while (badGuess) {
						String text = input.nextLine().trim();
						// If the user enter more than 1 character
						if (text.length() > 1) {
							System.out.println("Invalid character!.Try again: ");
						} 
						else if (text.isEmpty())
							System.out.println("Empty input. Try again: ");
						else {
							guess = Character.toUpperCase(text.charAt(0));
							if (guess < 'A' || guess > 'Z') {
								System.out.println("Invalid character!.Try again: ");
							} else badGuess = false;
						}			
				}

				// Check if the character is in hidden word and the dashed string
				String checkWord = isIn(guess,word);
				String checkDash = isIn(guess, dashes.toString());
				
				if (!checkWord.isEmpty()) {
					if (checkDash.isEmpty()) {
						String[] index = checkWord.split(" ");
						for (int i = 0; i < index.length; i++)
							dashes.setCharAt(Integer.parseInt(index[i]) * 2, guess);
						System.out.println("Yes, there were "+ guess +"’s\n");
						
						// Set score
						score += 10 * index.length;
					}
					else System.out.println(guess + " is already entered.\n");
					
				} else {
					if (isIn(guess, incorrectGuesses).isEmpty()) {
						System.out.println("Sorry, there were no "+ guess +"’s\n");
						guesses -= 1;
						incorrectGuesses += String.valueOf(guess) + ", ";
					} else {
						System.out.println(guess + " is already entered.\n");
					}
				}

				// Condition when the user loses: chances == 0
				if (guesses < 1) {
					outOfChance = true;
					break;
				}
			}
			
			
			if (!outOfChance)
				score += 100 + 30 * guesses; // After the score is set
			System.out.println("The correct word: " + word + "\n");
		}
		


		// Check top 5 all time list
		if (top5.isEmpty()) {
			System.out.println("You've made top 5 all time. What is your name? ");
			top5.add(input.nextLine() + "\t" + score);
		} else {
			int index = -1;
			for (int i = 0; i < top5.size(); i++) {
				if (score > Integer.parseInt(top5.get(i).substring(top5.get(i).indexOf("\t") + 1))) {
					index = i;
					break;
				}
			}
				
			if (index > -1) {
				System.out.println("You've made top 5 all time. What is your name? ");
				top5.add(index,input.nextLine() + "\t" + score);
				if (top5.size() > 5) top5.remove(top5.size()-1);
			} else if (top5.size() < 5) { //In case, size < 5 so just add at the end of the list
				System.out.println("You've made top 5 all time. What is your name? ");
				top5.add(input.nextLine() + "\t" + score);
			}
		}
		
		//Print the top 5 all time information
		System.out.println("\n-----All time top 5 list-----");
		for (int i = 0; i < top5.size(); i++) {
			System.out.println(top5.get(i));
		}
		
		//Store the top 5 all time information
		try {
		writeData("data/top5.txt", top5);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		//Close input
		input.close();
		System.out.println("-----END GAME-----");

		
	}
	
	
	
	public static void readData(String fileName, ArrayList<String> wordBank) throws IOException{
		Scanner input = new Scanner(new File(fileName));
		while (input.hasNext()) {wordBank.add(input.nextLine());}
		input.close();
	}
	
	public static void writeData(String fileName, ArrayList<String> holder) throws IOException{
		PrintWriter output = new PrintWriter(new File(fileName));
		for (int i = 0; i < holder.size(); i++)
			output.println(holder.get(i));
		output.close();
	}
	
	
	public static String isIn(char ch, String word){
		// Initialize a number containing the index
		String index = "";
		
		for (int i = 0; i < word.length(); i++)
			if (ch == word.charAt(i)) {index += i + " ";}

		return index.trim(); // If char is not in word, the index returned is an empty string
	}
	
	
	
	
	

	

}
