package edu.cnm.deepdive.crossfyre.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DictionaryClueQuery {

  private static final String API_KEY = "2fc887ac-578a-442a-b371-970eae934dfe";
  private static final String BASE_URL = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/";
  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();

  public Map<String, String> fetchDefinitions(List<String> words) {
    Map<String, String> definitions = new HashMap<>();

    for (String word : words) {
      try {
        String url = BASE_URL + word + "?key=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful() && response.body() != null) {
          String json = response.body().string();
          JsonArray array = JsonParser.parseString(json).getAsJsonArray();

          if (array.size() > 0 && array.get(0).isJsonObject()) {
            JsonObject entry = array.get(0).getAsJsonObject();
            JsonArray shortDefs = entry.getAsJsonArray("shortdef");

            if (shortDefs != null && shortDefs.size() > 0) {
              String definition = shortDefs.get(0).getAsString();
              definitions.put(word, definition);
            } else {
              definitions.put(word, "(No short definition found)");
            }
          } else {
            definitions.put(word, "(No valid entry found)");
          }
        } else {
          definitions.put(word, "(Failed to fetch)");
        }
      } catch (IOException | IllegalStateException | JsonParseException e) {
        definitions.put(word, "(Error: " + e.getMessage() + ")");
      }
    }

    return definitions;
  }

  // For testing/demo purposes
  public static void main(String[] args) {
    DictionaryClueQuery fetcher = new DictionaryClueQuery();
    List<String> words = Arrays.asList("voluminous", "ephemeral", "resilient");

    Map<String, String> results = fetcher.fetchDefinitions(words);
    results.forEach((word, definition) -> {
      System.out.println(word + ": " + definition);
    });
  }
}
