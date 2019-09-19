package se.experis;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FireAndIce {

    private Scanner scanner = new Scanner(System.in);

    public FireAndIce() {
        try {
            int characterID = Integer.parseInt(promptForCharacterID());
            System.out.println("Fetching characters...");
            Character character = getCharacter(characterID);
            System.out.println(character.displayCharacter());
            if (character.getAllegiances().size() > 0) {
                if (promptForExtraInformation(character.getAllegiances())) {
                    System.out.println(character.displayAllegianceMembers());
                } else {
                    System.out.println("Alright, fine, you never want to see all the characters!");
                }
            }
            if (promptForPOVCharacters()) {

                ArrayList<Book> books = getBooksByPublisher("Bantam Books");
                HashMap<String, boolean[]> characterMap = new HashMap<String, boolean[]>();

                for(int i = 0; i < books.size(); i++) {
                    // Iterates the books
                    Book book = books.get(i);
                    for (int j = 0; j < book.getPovCharactersURL().size(); j++) {
                        boolean[] bookIndex = new boolean[books.size()];
                        // Iterates the pov-characters in the book
                        characterMap.put(book.getPovCharactersURL().get(j), bookIndex);
                    }
                }

                for(int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    for (int j = 0; j < book.getPovCharactersURL().size(); j++) {
                        boolean[] bookIndex = characterMap.get(book.getPovCharactersURL().get(j));
                        bookIndex[i] = true;
                        characterMap.replace(book.getPovCharactersURL().get(j), bookIndex);
                    }
                }
                printTable(characterMap, books);
            }
        } catch(NumberFormatException ex) {
            System.out.println("You must provide an ID in a form of an Integer");
        }
    }

    private void printTable(HashMap<String, boolean[]> characterMap, ArrayList<Book> books) {
        String separator = new String(new char[132]).replace("\0", "-");
        System.out.println(separator);
        ArrayList<Character> characters = new ArrayList<Character>();
        String result = "|" + padRight("Name", 25, ' ');
        for (int i = 0; i < books.size(); i++) {
           if (i == books.size() -1) {
               result += "|" + padRight(books.get(i).getName(), 20, ' ') + "|";
           } else {
            result += "|" + padRight(books.get(i).getName(), 20, ' ');
           }
        }
        System.out.println(result);
        System.out.println(separator);
        for (String key : characterMap.keySet()) {
            Character c = getSimpleCharacter(key, characterMap.get(key));
            StringBuilder sb = new StringBuilder();
            sb.append("|" + padRight(c.getName(), 25, ' '));

            for (int i = 0; i < c.getPovBooks().length; i++) {
                if (c.getPovBooks()[i]) {
                    sb.append("|" + padBoth("*", 20, ' '));
                } else {
                    sb.append("|" + padBoth(" ", 20, ' '));
                }
                if (i == c.getPovBooks().length -1) {
                    sb.append("|");
                }
            }
            System.out.println(sb);
        }
        System.out.println(separator);
    }

    private String padRight(String str, int length, char c) {
        while(str.length() < length) {
            str += c;
        }
        return str;
    }

    private String padLeft(String str, int length, char c) {
        while(str.length() < length) {
            str = c + str;
        }
        return str;
    }

    private String padBoth(String str, int length, char c) {
        if (str.length() < length) {
            int strLength = length - str.length();
            str = padLeft(str, strLength/2, c);
            str = padRight(str, length, c);
        }
        return str;
    }

    private String promptForCharacterID() {
        System.out.println("Please enter the character ID:");
        return scanner.nextLine().toLowerCase();
    }

    private Boolean promptForExtraInformation(ArrayList<House> allegiances) {
        System.out.println("Do you want to see all characters of the house " + buildCommaSeparatedListHouse(allegiances) +  "?");
        System.out.println("Type Yes or No:");
        if (scanner.nextLine().toLowerCase().equals("yes")) {
            return true;
        }
        return false;
    }

    private Boolean promptForPOVCharacters() {
        System.out.println("Do you want to see all the POV characters from the books by Bentam Books?");
        System.out.println("Type Yes or No:");
        if (scanner.nextLine().toLowerCase().equals("yes")) {
            return true;
        }
        return false;
    }

    private ArrayList<Book> getBooksByPublisher(String publisher) {
        ArrayList<Book> allBooks = new ArrayList<Book>();
        JSONArray books = new JSONArray(getData("https://anapioficeandfire.com/api/books?pageSize=15"));
        for (int i = 0; i < books.length(); i++) {
            if (
                books.getJSONObject(i).getString("publisher").equals(publisher) &&
                books.getJSONObject(i).getJSONArray("povCharacters").length() > 0) {
                allBooks.add(
                    new Book(
                        books.getJSONObject(i).getString("name"),
                        books.getJSONObject(i).getString("publisher"),
                        JSONArrayToStringArrayList(books.getJSONObject(i).getJSONArray("povCharacters"))
                    )
                );
            }
        }
        return allBooks;
    }

    private StringBuilder buildCommaSeparatedListHouse(ArrayList<House> arr) {
        StringBuilder result = new StringBuilder();
        String delimiter = ", ";
        for (int i = 0; i < arr.size(); i++) {
            if (i == arr.size() - 2) {
                delimiter = " and ";
            } else if (i == arr.size() - 1) {
                delimiter = "";
            }
            result.append(arr.get(i).name + delimiter);
        }
        return result;
    }

    private Character getCharacter(int characterID) {
        JSONObject c = new JSONObject(getData("https://www.anapioficeandfire.com/api/characters/" + characterID));
        return new Character(
                c.getString("name"),
                JSONArrayToStringArrayList(c.getJSONArray("aliases")),
                c.getString("born"),
                c.getString("gender"),
                JSONArrayToStringArrayList(c.getJSONArray("titles")),
                resolveHouseJSONArray(c.getJSONArray("allegiances"))
        );
    }

    private Character getSimpleCharacter(String characterURL, boolean[] POVbooks) {
        JSONObject c = new JSONObject(getData(characterURL));
        return new Character(
                c.getString("name"),
                POVbooks
        );
    }

    private JSONObject getHouse(String houseURL) {
        return new JSONObject(getData(houseURL));
    }

    private JSONObject getCharacterURL(String characterURL) {
        return new JSONObject(getData(characterURL));
    }

    private ArrayList<House> resolveHouseJSONArray(JSONArray arr) {
        ArrayList<House> houses = new ArrayList<House>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject jo = getHouse(arr.get(i).toString());
            houses.add(
                new House(
                    jo.getString("name"),
                    resolveCharacterJSONArray(jo.getJSONArray("swornMembers"))
                )
            );
        }
        return houses;
    }

    private ArrayList<Character> resolveCharacterJSONArray(JSONArray arr) {
        ArrayList<Character> characters = new ArrayList<Character>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject jo = getCharacterURL(arr.get(i).toString());
            characters.add(
                new Character(
                        jo.getString("name")
                )
            );
        }
        return characters;
    }

    private ArrayList<String> JSONArrayToStringArrayList(JSONArray arr) {
        ArrayList<String> list = new ArrayList<String>();
        if (arr != null) {
            int len = arr.length();
            for (int i = 0; i < len; i++){
                list.add(arr.get(i).toString());
            }
        }
        return list;
    }

    private ArrayList<House> JSONArrayToHouseArrayList(JSONArray arr) {
        ArrayList<House> list = new ArrayList<House>();
        if (arr != null) {
            int len = arr.length();
            for (int i = 0; i < len; i++){
                list.add(
                    new House(
                        new JSONObject(arr.get(i)).getString("name"),
                        JSONArrayToCharacterArrayList(
                            new JSONObject(arr.get(i)).getJSONArray("swornMembers")
                        )
                    )
                );
            }
        }
        return list;
    }

    private ArrayList<Character> JSONArrayToCharacterArrayList(JSONArray arr) {
        ArrayList<Character> list = new ArrayList<Character>();
        if (arr != null) {
            int len = arr.length();
            for (int i = 0; i < len; i++){
                list.add(new Character(new JSONObject(arr.get(i)).getString("name")));
            }
        }
        return list;
    }

    public String getData(String URLString) {
        try {
            URL url = new URL(URLString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // The getInputStream call implicitly calls connection.connect(), essentially starting the connection.
            // To improve the code, this method should be split, with the connection setup (above these comments) being
            // initialized on the instantiation of this class. The connecting part (below) could be done on a per need basis.
            // I am unsure if it's possible to perform several connections to one connection object, to minimize overhead.
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
