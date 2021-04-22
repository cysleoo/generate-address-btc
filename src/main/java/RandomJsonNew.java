import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author ssliu
 * @date 2020-11-28
 */
public class RandomJsonNew {
  public static void main(String[] args) {
    // System.out.println(randomJson(20));

    // System.out.println(randomArray(77, 156));

    try {
      randomGenerate();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  static JsonArray randomJson(int total) {
    JsonArray result = new JsonArray(total);
    for (int i = 0; i < total; i++) {
      int id = 137+i;

      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("name","CountessDeLo");
      JsonObject itemStyleJson = new JsonObject();
      JsonObject normalJson = new JsonObject();
      normalJson.addProperty("color","rgb(236,81,72)");
      itemStyleJson.add("normal",normalJson);
      jsonObject.add("itemStyle",itemStyleJson);
      jsonObject.addProperty("symbolSize",4);
      jsonObject.addProperty("x", randomX(280,500));
      jsonObject.addProperty("y",randomX(270,600));

      JsonObject attributes = new JsonObject();
      attributes.addProperty("modularity_class",0);
      jsonObject.add("attributes",attributes);
      jsonObject.addProperty("id",id);
      result.add(jsonObject);
    }
    return result;
  }

  static int randomX(int min,int max) {
    Random random = new Random();
    return random.nextInt(max - min) + min;
  }

  static JsonArray randomArray(int start, int end) {
    JsonArray result = new JsonArray(end - start);
    for (int i = start; i <= end; i++) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("source",i);
      jsonObject.addProperty("target","11");

      jsonObject.add("data",RandomJson.randomArray());
      jsonObject.addProperty("addressin", RandomJson.randomAddr(i));
      jsonObject.addProperty("addressout",RandomJson.randomAddr(11));

      result.add(jsonObject);

    }
   return result;


  }

  static void randomGenerate() throws IOException {
    JsonArray jsonArray =
        RandomJson.readJson("/Users/ssliu/Downloads/test.json", JsonArray.class)
            .getAsJsonArray();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
      jsonObject.addProperty("addressin",RandomJson.randomAddr(i));
      jsonObject.addProperty("addressout",RandomJson.randomAddr(jsonArray.size()+i));
      jsonObject.add("data",RandomJson.randomArray());
    }

    RandomJson.writeJson(jsonArray,"/Users/ssliu/Downloads/test-new.json");
  }









}
