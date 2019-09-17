package se.experis;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class FireAndIce {

    private StringBuffer characters;
    private int characterID;
    private Character character;

    public FireAndIce() {
        characterID = Integer.parseInt(promptForCharacterID());
        character = getCharacter(characterID);
        System.out.println(character.displayCharacter());
        if (promptForExtraInformation(character.allegiances).equals("yes")) {
            System.out.println(character.displayAllegianceMembers());
        } else {
            System.out.println("Alright, fine, you never want to see all the characters!");
        }
    }

    private String promptForCharacterID() {
        System.out.println("Please enter the character ID:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toLowerCase();
    }

    private String promptForExtraInformation(ArrayList<House> allegiances) {
        System.out.println("Do you want to see all characters of the house " + buildCommaSeparatedListHouse(allegiances) +  "?");
        System.out.println("Type Yes or No:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().toLowerCase();
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

    private Character getCharacter(int characterID) {
        JSONObject c = getData("https://www.anapioficeandfire.com/api/characters/" + characterID);
        return new Character(
                c.getString("name"),
                JSONArrayToStringArrayList(c.getJSONArray("aliases")),
                c.getString("born"),
                c.getString("gender"),
                JSONArrayToStringArrayList(c.getJSONArray("titles")),
                resolveHouseJSONArray(c.getJSONArray("allegiances"))
        );
    }

    private JSONObject getHouse(String houseURL) {
        return getData(houseURL);
    }

    private JSONObject getCharacterURL(String characterURL) {
        return getData(characterURL);
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

    public JSONObject getData(String URLString) {
        try {
            URL url = new URL(URLString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return new JSONObject(content.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
