package org.as2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JsonProcessor {

    private static final String RESOURCES_FOLDER = "src/java/resources/json/";



    public static Map<String, JsonObject> readJsonFiles(String folderPath) throws IOException {
        Gson gson = new Gson();
        return Files.list(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .map(path -> {
                    try (JsonReader reader = new JsonReader(new FileReader(path.toFile()))) {
                        reader.setLenient(true);
                        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                        return Map.entry(path.getFileName().toString(), jsonObject);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }



    public static Map<String, JsonArray> readJsonFiles1(String folderPath) throws IOException {
        Gson gson = new Gson();
        return Files.list(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".json"))
                .map(path -> {
                    try (JsonReader reader = new JsonReader(new FileReader(path.toFile()))) {
                        reader.setLenient(true);
                        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                        String fileName = path.getFileName().toString();
                        String prefix = fileName.substring(0, fileName.indexOf('.'));
                        return Map.entry(prefix, jsonObject);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            JsonArray jsonArray = new JsonArray();
                            entry.getValue().forEach(jsonArray::add);
                            return jsonArray;
                        }
                ));
    }

    public static void combineAndSaveJsonArrays(String folderPath, String outputFilePath) throws IOException {
        Map<String, JsonArray> jsonArrays = JsonProcessor.readJsonFiles1(folderPath);
        JsonArray combinedArray = new JsonArray();
        Map<String, JsonObject> categotyMap = new HashMap<>();

//        jsonArrays.values().forEach(
//                combinedArray::add
//        );

        int duaId = 0;
        for(JsonArray entry : jsonArrays.values()) {
            JsonObject categoryObject = new JsonObject();
            if(entry.isJsonArray()){
                for(JsonElement element : entry) {
                    JsonObject item = element.getAsJsonObject();
                    item.addProperty("id", duaId);
                    combinedArray.add(item);
                    categotyMap.put(item.get("categoryId").getAsString(),prepareCategory(item,categotyMap));
                    duaId++;
                }
            }else {
                JsonObject item = entry.getAsJsonObject();
                item.addProperty("id", duaId);
                combinedArray.add(item);
                categotyMap.put(item.get("categoryId").getAsString(),prepareCategory(item,categotyMap));
                duaId++;
            }


//            categoryObject.addProperty("category", entry.getKey());
//            categoryObject.add("items", entry.getValue());
//            categoryArray.add(categoryObject);
        }

        categotyMap.values().forEach(
                x -> {
                    System.out.println(x);
                }
        );

       // print the combined array
//        for (JsonElement element : combinedArray) {
//            System.out.println(element);
//        }

        try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
            new Gson().toJson(combinedArray, fileWriter);
        }
    }

    private static JsonObject prepareCategory(JsonObject item, Map<String, JsonObject> categotyMap) {
        //JsonArray categoryArray = new JsonArray();

        JsonArray duaArray;
        JsonObject category = categotyMap.get(item.get("categoryId").getAsString());
        if(category != null){
            duaArray = category.getAsJsonArray("duaIds");
           if(duaArray != null){
               duaArray.add(item.get("id").getAsString());
           }else {
               duaArray = new JsonArray();
               duaArray.add(item.get("id").getAsString());
           }
            return category;
        }else{
            JsonObject categoryObject = new JsonObject();
            categoryObject.addProperty("categoryId", item.get("categoryId").getAsString());
            duaArray = new JsonArray();
            duaArray.add(item.get("id").getAsString());
            categoryObject.add("duaIds", duaArray);
            return categoryObject;
        }
    }
}
