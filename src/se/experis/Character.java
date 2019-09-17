package se.experis;

import java.util.ArrayList;

public class Character {

    public String name;
    private ArrayList<String> aliases = new ArrayList<String>();
    private String born;
    private String gender;
    private ArrayList<String> titles = new ArrayList<String>();
    public ArrayList<House> allegiances = new ArrayList<House>();

    public Character(String name) {
        this.name        = name;
    }

    public Character(String name, ArrayList<String> aliases, String born, String gender, ArrayList<String> titles, ArrayList<House> allegiances) {
        this.name        = name;
        this.aliases     = aliases;
        this.born        = born;
        this.gender      = gender;
        this.titles      = titles;
        this.allegiances = allegiances;
    }

    public String displayCharacter() {
        String result = "";
        if (!name.isEmpty()) {
            result += "Name: \t\t" + name + "\n";
        }
        if (aliases.size() > 1) {
            result += "Aliases: \t" + buildCommaSeparatedList(aliases) + "\n";
        }
        if (!born.isEmpty()) {
            result += "Born: \t\t" + born + "\n";
        }
        if(!gender.isEmpty()) {
            result += "Gender: \t" + gender + "\n";
        }
        if (titles.size() > 0) {
            result += "Title: \t\t" + buildCommaSeparatedList(titles) + "\n";
        }

        if (allegiances.size() > 0) {
            result += "Houses: \t" + buildCommaSeparatedListHouse(allegiances)+ "\n";
        }
        return result;
    }

    public String displayAllegianceMembers() {
        String result = "";
        for(int i = 0; i < allegiances.size(); i++) {
            result += allegiances.get(i).name + "\n";
            result += allegiances.get(i).displayHouseMembers() + "\n";
        }
        return result;
    }

    private String buildCommaSeparatedList(ArrayList arr) {
        String result = "";
        String delimiter;
        for (int i = 0; i < arr.size(); i++) {
            if (i == arr.size() - 2) {
                delimiter = " and ";
            } else if (i == arr.size() - 1) {
                delimiter = "";
            } else{
                delimiter = ", ";
            }
            result += arr.get(i) + delimiter;
        }
        return result;
    }

    private String buildCommaSeparatedListHouse(ArrayList<House> arr) {
        String result = "";
        String delimiter;
        for (int i = 0; i < arr.size(); i++) {
            if (i == arr.size() - 2) {
                delimiter = " and ";
            } else if (i == arr.size() - 1) {
                delimiter = "";
            } else{
                delimiter = ", ";
            }
            result += arr.get(i).name + delimiter;
        }
        return result;
    }
}