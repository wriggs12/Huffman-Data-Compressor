/**
 * @author Winston Riggs
 * @version 1.0 9/24/20
 *
 * Compresses a given String using the huffman compression algorithm
 */

//import required classes
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        //gets the users String to encrypt
        System.out.println("What massage should I encrypt?");
        String message = keyboard.nextLine();

        //Converts input into single characters
        char[] msgChar = message.toCharArray();
        ArrayList<Character> characters = new ArrayList<>();

        //Removes any repeated characters
        for (char c : msgChar) {
            if (!(characters.contains(c)))
                characters.add(c);
        }

        int[] countOfChar = new int[characters.size()];

        //Finds the frequency of each character in the String
        for (int i = 0; i < characters.size(); i++) {
            char checker = characters.get(i);
            for (char c : msgChar) {
                if (checker == c)
                    countOfChar[i]++;
            }
        }

        //Sorts the characters in descending order of frequency
        bubbleSort(characters, countOfChar);

        //generates the binary tree with the huffman algorithm
        BinaryTree huffmanTree = generateHuffmanTree(characters, countOfChar);

        //gets the binary values of each character
        String[] codes = findEncryptedVals(huffmanTree, characters.size());

        HashMap<String, String> newKeys = new HashMap<>();
        HashMap<String, String> oldKeys = new HashMap<>();

        //Assigns each character with a binary value
        for (int i = 0; i < characters.size(); i++) {
            newKeys.put(codes[i].substring(0, 1), codes[i].substring(1));
            oldKeys.put(codes[i].substring(0, 1), Integer.toBinaryString(Character.getNumericValue(codes[i].substring(0, 1).charAt(0))));
        }

        //Builds the string of binary values with the assigned binary values
        StringBuilder newCode = new StringBuilder();
        StringBuilder oldCode = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            newCode.append(newKeys.get(message.substring(i, i + 1)));
            oldCode.append(oldKeys.get(message.substring(i, i + 1)));
        }

        System.out.println("Uncompressed: " + oldCode + "\nCompressed: " + newCode);

        //Finds the compression amount
        System.out.println((int)(((newCode.length() / (double)oldCode.length()) + 0.05) * 100) + "% of original size");
    }

    /**
     * Generates the huffman binary tree
     * @param characters ArrayList containing all the non-repeated characters
     * @param countOfChar Array the contains all the frequencies of the letters in the same order and the characters ArrayList
     * @return Binary tree of Nodes
     */
    public static BinaryTree generateHuffmanTree(ArrayList<Character> characters, int[] countOfChar) {
        //Creates all required Nodes with the character and frequency
        ArrayList<Node> allNodes = new ArrayList<>();
        for (int i = 0; i < characters.size(); i++) {
            allNodes.add(new Node(characters.get(i).toString(), countOfChar[i]));
        }

        Node current = allNodes.get(0);
        BinaryTree huffmanTree = new BinaryTree();

        //generates the huffman binary tree by connecting the bottom two Nodes and placing them back in
        while(allNodes.size() > 1) {
            //Temporary parent Node
            Node temp = new Node(null, allNodes.get(0).getFreq() + allNodes.get(1).getFreq());
            temp.setLeft(allNodes.get(0));
            temp.setRight(allNodes.get(1));

            //No need for these as parent node points to them
            allNodes.remove(0);
            allNodes.remove(0);

            //put back in the temporary parent node into the array to be sorted
            siftInNode(allNodes, temp);

            current = allNodes.get(0);
        }

        //Sets the root of the tree to the last node which will be the root
        huffmanTree.setRoot(current);

        return huffmanTree;
    }

    /**
     * Will sift the Node back into place
     * @param allNodes ArrayList of Nodes in descending order of frequency
     * @param temp Temporary Node to sift into the dynamic array
     */
    public static void siftInNode(ArrayList<Node> allNodes, Node temp) {
        int index = 0;
        //Finds the index at which the Node belongs
        while(index < allNodes.size() && allNodes.get(index).getFreq() < temp.getFreq())
            index++;
        allNodes.add(index, temp);
    }

    /**
     * Sorts an ArrayList in descending order using bubbleSort
     * @param characters ArrayList of characters
     * @param countOfChar integer array of frequencies related to the characters in the arraylist
     */
    public static void bubbleSort(ArrayList<Character> characters, int[] countOfChar) {
        //Bubble Sort
        for (int i = 0; i < countOfChar.length - 1; i++) {
            for (int j = 0; j < countOfChar.length - 1; j++) {
                if (countOfChar[j] > countOfChar[j + 1]) {
                   int temp = countOfChar[j];
                   countOfChar[j] = countOfChar[j + 1];
                   countOfChar[j + 1] = temp;

                   char tempChar = characters.get(j);
                   characters.set(j, characters.get(j + 1));
                   characters.set(j + 1, tempChar);
                }
            }
        }

    }

    /**
     * Given the huffman binary tree, will find the binary values of all the characters in the String
     * @param huffmanTree BinaryTree created with the huffman algorithm
     * @param size Number of unique characters in the String
     * @return An array of Strings and each string has the character at index 0 and the binary code after
     */
    public static String[] findEncryptedVals(BinaryTree huffmanTree, int size) {
        //Starts at the root of the tree
        Node current = huffmanTree.getRoot();
        Node parent;

        //Creates list of the completed values
        ArrayList<Node> finalVals = new ArrayList<>();
        //Array for final coded of all characters
        String[] codes = new String[size];

        //Temporary code for current binary value
        StringBuilder code = new StringBuilder();
        int currentNum = 0;

        //Preventing indexOutOFBounds
        if (size > 1) {
            //While there are more characters left
            while (finalVals.size() < size) {
                //If the current node is a character value to the left
                if (current.getLeft() == null && !current.hasBeenVisited()) {
                    finalVals.add(current.getLeft());
                    codes[currentNum] = current.getVal() + code;
                    current.visit();
                    current = current.getParent();
                    code = new StringBuilder(code.substring(0, code.length() - 1));
                    currentNum++;
                }
                //If there is an available unvisited path to the left
                else if (!current.getLeft().hasBeenVisited()) {
                    parent = current;
                    current = current.getLeft();
                    current.setParent(parent);
                    code.append("0");
                }
                //If the current node is a character value to the left
                else if (current.getRight() == null && !current.hasBeenVisited()) {
                    finalVals.add(current.getRight());
                    codes[currentNum] = current.getVal() + code;
                    current.visit();
                    current = current.getParent();
                    code = new StringBuilder(code.substring(0, code.length() - 1));
                    currentNum++;
                }
                //If there is an available unvisited path to the right
                else if (!current.getRight().hasBeenVisited()) {
                    parent = current;
                    current = current.getRight();
                    current.setParent(parent);
                    code.append("1");
                }
                //Else just go back up the tree
                else {
                    current.visit();
                    current = current.getParent();
                    code = new StringBuilder(code.substring(0, code.length() - 1));
                }
            }
        }
        else if (size == 1){
            code.append("0".repeat(size));
            codes[0] = current.getVal() + code;
        }
        return codes;
    }
}