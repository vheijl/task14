package se.experis;

import java.util.ArrayList;

public class Book {
    private String name;
    private String publisher;
    private ArrayList<Character> povCharacters = new ArrayList<Character>();
    private ArrayList<String> povCharactersURL = new ArrayList<String>();

    public Book(String name, String publisher, ArrayList<String> povCharactersURL) {
        this.name = name;
        this.publisher = publisher;
        this.povCharactersURL = povCharactersURL;
    }

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public ArrayList<Character> getPovCharacters() {
        return povCharacters;
    }

    public ArrayList<String> getPovCharactersURL() {
        return povCharactersURL;
    }

    public void setPovCharactersURL(ArrayList<String> povCharactersURL) {
        this.povCharactersURL = povCharactersURL;
    }
}
