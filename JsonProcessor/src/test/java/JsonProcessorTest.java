import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.as2.JsonProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class JsonProcessorTest {

//    @Test
//    void readJsonFiles_readsAllJsonFilesInDirectory() throws IOException {
//        String folderPath = "src/test/resources/json";
//        Map<String, JsonObject> jsonObjects = JsonProcessor.readJsonFiles(folderPath);
//        //print the map
//        for (Map.Entry<String, JsonObject> entry : jsonObjects.entrySet()) {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        }
//        Assertions.assertFalse(jsonObjects.isEmpty());
//    }
//
//    @Test
//    void readJsonFiles_returnsEmptyMapForEmptyDirectory() throws IOException {
//        String folderPath = "src/test/resources/empty";
//        Map<String, JsonObject> jsonObjects = JsonProcessor.readJsonFiles(folderPath);
//        Assertions.assertTrue(jsonObjects.isEmpty());
//    }


    @Test
    void readJsonFiles1_readsAllJsonFilesInDirectory() throws IOException {
        String folderPath = "src/test/resources/json";
        Map<String, JsonArray> jsonArrays = JsonProcessor.readJsonFiles1(folderPath);
        //print the map
        for (Map.Entry<String, JsonArray> entry : jsonArrays.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        Assertions.assertFalse(jsonArrays.isEmpty());
    }

    @Test
    void readJsonFiles1_returnsEmptyMapForEmptyDirectory() throws IOException {
        String folderPath = "src/test/resources/empty";
        Map<String, JsonArray> jsonArrays = JsonProcessor.readJsonFiles1(folderPath);
        Assertions.assertTrue(jsonArrays.isEmpty());
    }

//    @Test
//    void readJsonFiles1_ignoresNonJsonFiles() throws IOException {
//        String folderPath = "src/test/resources/mixed";
//        Map<String, JsonArray> jsonArrays = JsonProcessor.readJsonFiles1(folderPath);
//        Assertions.assertEquals(1, jsonArrays.size());
//    }
//
//    @Test
//    void readJsonFiles1_handlesMalformedJsonGracefully() throws IOException {
//        String folderPath = "src/test/resources/malformed";
//        Map<String, JsonArray> jsonArrays = JsonProcessor.readJsonFiles1(folderPath);
//        Assertions.assertTrue(jsonArrays.isEmpty());
//    }
//
//    @Test
//    void readJsonFiles1_groupsJsonFilesByPrefix() throws IOException {
//        String folderPath = "src/test/resources/grouped";
//        Map<String, JsonArray> jsonArrays = JsonProcessor.readJsonFiles1(folderPath);
//        Assertions.assertTrue(jsonArrays.containsKey("1"));
//        Assertions.assertTrue(jsonArrays.containsKey("2"));
//        Assertions.assertEquals(2, jsonArrays.get("1").size());
//        Assertions.assertEquals(1, jsonArrays.get("2").size());
//    }

    @Test
    void combineAndSaveJsonArrays_combinesAllJsonArraysIntoSingleFile() throws IOException {
        String folderPath = "src/test/resources/json";
        String outputFilePath = "src/test/resources/output/combined.json";
        JsonProcessor.combineAndSaveJsonArrays(folderPath, outputFilePath);
        File outputFile = new File(outputFilePath);
        Assertions.assertTrue(outputFile.exists());
        JsonArray combinedArray = new Gson().fromJson(new FileReader(outputFile), JsonArray.class);
        Assertions.assertFalse(combinedArray.isEmpty());
    }

    @Test
    void combineAndSaveJsonArrays_createsEmptyFileForEmptyDirectory() throws IOException {
        String folderPath = "src/test/resources/empty";
        String outputFilePath = "src/test/resources/output/combined_empty.json";
        JsonProcessor.combineAndSaveJsonArrays(folderPath, outputFilePath);
        File outputFile = new File(outputFilePath);
        Assertions.assertTrue(outputFile.exists());
        JsonArray combinedArray = new Gson().fromJson(new FileReader(outputFile), JsonArray.class);
        Assertions.assertTrue(combinedArray.isEmpty());
    }

    @Test
    void combineAndSaveJsonArrays_ignoresNonJsonFiles() throws IOException {
        String folderPath = "src/test/resources/mixed";
        String outputFilePath = "src/test/resources/output/combined_mixed.json";
        JsonProcessor.combineAndSaveJsonArrays(folderPath, outputFilePath);
        File outputFile = new File(outputFilePath);
        Assertions.assertTrue(outputFile.exists());
        JsonArray combinedArray = new Gson().fromJson(new FileReader(outputFile), JsonArray.class);
        Assertions.assertEquals(1, combinedArray.size());
    }

    @Test
    void combineAndSaveJsonArrays_handlesMalformedJsonGracefully() throws IOException {
        String folderPath = "src/test/resources/malformed";
        String outputFilePath = "src/test/resources/output/combined_malformed.json";
        JsonProcessor.combineAndSaveJsonArrays(folderPath, outputFilePath);
        File outputFile = new File(outputFilePath);
        Assertions.assertTrue(outputFile.exists());
        JsonArray combinedArray = new Gson().fromJson(new FileReader(outputFile), JsonArray.class);
        Assertions.assertTrue(combinedArray.isEmpty());
    }

    @Test
    void combineAndSaveJsonArrays_combinesGroupedJsonFilesCorrectly() throws IOException {
        String folderPath = "src/test/resources/grouped";
        String outputFilePath = "src/test/resources/output/combined_grouped.json";
        JsonProcessor.combineAndSaveJsonArrays(folderPath, outputFilePath);
        File outputFile = new File(outputFilePath);
        Assertions.assertTrue(outputFile.exists());
        JsonArray combinedArray = new Gson().fromJson(new FileReader(outputFile), JsonArray.class);
        Assertions.assertEquals(3, combinedArray.size());
    }

}