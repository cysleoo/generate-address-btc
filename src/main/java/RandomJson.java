import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

/**
 * @author ssliu
 * @date 2020-11-27
 */
public class RandomJson {

  public static void main(String[] args) {
    try {
      String fromPath = "/Users/ssliu/work/项目/15.btc-addr/tsconfig.json";
      String toPath = "/Users/ssliu/work/项目/15.btc-addr/tsconfig-new.json";

      JsonObject jsonObject = readJson(fromPath,JsonObject.class).getAsJsonObject();
      handleJson(jsonObject);
      writeJson(jsonObject, toPath);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  static JsonElement readJson(String filePath,Class clazz) throws FileNotFoundException {
    FileInputStream fileInputStream = new FileInputStream(filePath);
    InputStreamReader in = new InputStreamReader(fileInputStream);
    JsonReader jsonReader = new JsonReader(in);
    return new Gson().fromJson(jsonReader, clazz);
  }

  static void writeJson(JsonElement jsonElement, String pathname) throws IOException {
    File file = new File(pathname);
    file.createNewFile();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Writer writer = Files.newBufferedWriter(Paths.get(pathname));
    gson.toJson(jsonElement,writer);
    writer.close();
  }

  static void handleJson(JsonObject jsonObject) {
    jsonObject = jsonObject.get("data").getAsJsonObject();
    JsonArray nodes = jsonObject.get("nodes").getAsJsonArray();
    JsonArray links = jsonObject.get("links").getAsJsonArray();
    // 1.随机填充ndoes中的地址
    for (int i = 0; i < nodes.size(); i++) {
      nodes.get(i).getAsJsonObject().addProperty("name", randomAddr(i));
    }

    //  2.遍历links，补充相应字段
    for (int i = 0; i < links.size(); i++) {
      JsonObject link= links.get(i).getAsJsonObject();
      int source = link.get("source").getAsInt();
      int target = link.get("target").getAsInt();
      String addressin = nodes.get(source).getAsJsonObject().get("name").getAsString();
      String addressout = nodes.get(target).getAsJsonObject().get("name").getAsString();


      // String fee = randomFee();
      // String value = randomValue();
      // String time = randomTime("2015-10-10","2020-05-10");

      link.addProperty("addressin",addressin);
      link.addProperty("addressout",addressout);
      // link.addProperty("time",time);
      // link.addProperty("value",value);
      // link.addProperty("fee", fee);
      JsonArray dataArray = randomArray();
      link.add("data",dataArray);
    }

  }

  static JsonArray randomArray() {
    Random random = new Random();
    int total;
    int n = random.nextInt(9);
    if (n < 2) {
      total = 1;
    } else if (n < 4) {
      total = 2;
    } else if (n < 7) {
      total = 3;
    } else {
      total = 4;
    }

    JsonArray resultArray = new JsonArray();
    for (int i = 0; i < total; i++) {
      resultArray.add(randomData());
    }
    return resultArray;
  }

  static JsonObject randomData() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("time",randomTime("2019-01-01","2019-06-27"));
    jsonObject.addProperty("value",randomValue());
    jsonObject.addProperty("fee",randomFee());
    return jsonObject;
  }

  static String randomAddr(int index) {
    String words =
        "park envelope bid whip link rug carbon cousin shoulder arena jewel expect trip canal sound";
    String[] wordArray = words.split(" ");
    List<String> wordList = Arrays.asList(wordArray);

    return generateAddr(wordList, index);

  }

  static String generateAddr(List<String> wordsList, int index) {
    ArrayList<String> resultList = new ArrayList<String>();

    DeterministicSeed deterministicSeed = new DeterministicSeed(wordsList, null, "", 0);
    DeterministicKeyChain deterministicKeyChain =
        DeterministicKeyChain.builder().seed(deterministicSeed).build();

    String path = "m/44'/0'/0'/0/" + index;
    DeterministicKey
        keyByPath = deterministicKeyChain.getKeyByPath(parsePath(path), true);
    BigInteger privKey = keyByPath.getPrivKey();
    ECKey ecKey = ECKey.fromPrivate(privKey);
    String publicKey = ecKey.getPublicKeyAsHex();

    NetworkParameters networkParameters = MainNetParams.get();
    String privateKey = ecKey.getPrivateKeyEncoded(networkParameters).toString();
    return ecKey.toAddress(networkParameters).toString();
  }

  static List<ChildNumber> parsePath(@Nonnull String path) {
    String[] parsedNodes = path.replace("m", "").split("/");
    List<ChildNumber> nodes = new ArrayList<>();

    for (String n : parsedNodes) {
      n = n.replaceAll(" ", "");
      if (n.length() == 0)
        continue;
      boolean isHard = n.endsWith("'");
      if (isHard)
        n = n.substring(0, n.length() - 1);
      int nodeNumber = Integer.parseInt(n);
      nodes.add(new ChildNumber(nodeNumber, isHard));
    }

    return nodes;
  }


  static String randomFee() {
    // Random r = new Random(System.currentTimeMillis());
    // int fiveDigits = (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
    int fiveDigits = (int) ((Math.random() * 9 + 1) * Math.pow(10, 5 - 1));
    String pre = "0.000" + fiveDigits;
    return pre;
  }

  static String randomValue() {
    Random random = new Random();
    String pre;
    int nextInt = random.nextInt(100);
    // if (nextInt<7) {
    //   pre = "0";
    // } else if (nextInt < 9) {
    //   pre = random.nextInt(9) + "";
    // } else {
    //   pre = "1"+random.nextInt(9);
    // }
    // pre = nextInt;
    StringBuilder tail = new StringBuilder(".");
    for (int i = 0; i < 8; i++) {
      tail.append(random.nextInt(9));
    }


    return nextInt+tail.toString();
  }

  static String randomTime(String beginDate, String endDate) {
    long startTime =
        LocalDate.parse(beginDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()
            .toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    long endTime =
        LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()
            .toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    long randomTime = startTime + (long) (Math.random() * (endTime - startTime));

    LocalDateTime localDateTime =
        Instant.ofEpochMilli(randomTime).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

  }



}
