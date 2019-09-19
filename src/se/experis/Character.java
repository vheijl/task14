package se.experis;

import java.util.ArrayList;

public class Character {

    private String name;
    private ArrayList<String> aliases = new ArrayList<String>();
    private String born;
    private String gender;
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<House> allegiances = new ArrayList<House>();
    private boolean[] povBooks;

    public Character(String name) {
        this.name = name;
    }

    public Character(String name, boolean[] povBooks) {
        this.name = name;
        this.povBooks = povBooks;
    }

    public Character(String name, ArrayList<String> aliases, String born, String gender, ArrayList<String> titles, ArrayList<House> allegiances) {
        this.name = name;
        this.aliases = aliases;
        this.born = born;
        this.gender = gender;
        this.titles = titles;
        this.allegiances = allegiances;
    }

    public String getName() {
        return name;
    }

    public ArrayList<House> getAllegiances() {
        return allegiances;
    }

    public boolean[] getPovBooks() {
        return povBooks;
    }

    public StringBuilder displayCharacter() {
        StringBuilder result = new StringBuilder();

        if (!name.isEmpty()) {
            result.append("Name: \t\t" + name + "\n");
        }
        if (aliases.size() > 1) {
            result.append("Aliases: \t" + buildCommaSeparatedList(aliases) + "\n");
        }
        if (!born.isEmpty()) {
            result.append("Born: \t\t" + born + "\n");
        }
        if(!gender.isEmpty()) {
            result.append("Gender: \t" + gender + "\n");
        }
        if (titles.size() > 0) {
            result.append("Title: \t\t" + buildCommaSeparatedList(titles) + "\n");
        }
        if (allegiances.size() > 0) {
            result.append("Houses: \t" + buildCommaSeparatedListHouse(allegiances)+ "\n");
        } else {
            result.append("Houses: \tThis character doesn't have any allegiances\n");
        }
        return result;
    }

    public StringBuilder displayAllegianceMembers() {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < allegiances.size(); i++) {
            result.append(allegiances.get(i).name + "\n");
            result.append(allegiances.get(i).displayHouseMembers() + "\n");
        }
        return result;
    }

    private StringBuilder buildCommaSeparatedList(ArrayList arr) {
        StringBuilder result = new StringBuilder();
        String delimiter = ", ";
        for (int i = 0; i < arr.size(); i++) {
            if (i == arr.size() - 2) {
                delimiter = " and ";
            } else if (i == arr.size() - 1) {
                delimiter = "";
            }
            result.append(arr.get(i) + delimiter);
        }
        return result;
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
}