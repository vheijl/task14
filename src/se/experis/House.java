package se.experis;

import java.util.ArrayList;

public class House {
    public String name;
    public ArrayList<Character> members = new ArrayList<Character>();

    public House(String name, ArrayList<Character> members) {
        this.name = name;
        this.members = members;
    }

    public String displayHouseMembers() {
        String result = "";
        for (int i = 0; i < members.size(); i++) {
            result += "\t" + members.get(i).getName() + "\n";
        }
        return result;
    }
}
