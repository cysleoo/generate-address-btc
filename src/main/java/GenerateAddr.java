import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;

/**
 * @author ssliu
 * @date 2020-11-02
 */
public class GenerateAddr {

  public static void main(String[] args) {
    List<String> strList = Arrays.asList(args);
    testGenerate(strList);

    // // 1.准备数据
    // int times = 1;
    // List<List<String>> batchList = new ArrayList<>(1);
    // while (times > 0) {
    //   batchList.add(strList);
    //   times--;
    // }
    //
    // long start= System.currentTimeMillis();
    //
    // batchList.parallelStream().forEach(GenerateAddr::generateAddr);
    // // batchList.stream().forEach(GenerateAddr::generateAddr);
    //
    // long end = System.currentTimeMillis();
    //
    // long total = end - start;
    // System.out.println("total cost: "+total);

    // System.out.println(new Gson().toJson(strings));

  }

  static void testGenerate(List<String> wordsList) {
    ArrayList<ChildNumber> childNumbers = new ArrayList<>();
    childNumbers.add(new ChildNumber(44,true));
    childNumbers.add(new ChildNumber(0,true));
    childNumbers.add(new ChildNumber(0,true));
    childNumbers.add(new ChildNumber(0,false));
    childNumbers.add(new ChildNumber(0,false));
    List<String> strings = generateAddr(wordsList,childNumbers );
    System.out.println(new Gson().toJson(strings));

  }




  static List<String> generateAddr(List<String> wordsList,
      List<ChildNumber> childNumberList) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    ArrayList<String> resultList = new ArrayList<String>();

    DeterministicSeed deterministicSeed = new DeterministicSeed(wordsList, null, "", 0);
    DeterministicKeyChain deterministicKeyChain =
        DeterministicKeyChain.builder().seed(deterministicSeed).build();

      String path = "m/44'/0'/0'/0/0";
      // DeterministicKey keyByPath = deterministicKeyChain.getKeyByPath(parsePath(path), true);
      DeterministicKey keyByPath = deterministicKeyChain.getKeyByPath(childNumberList, true);
      BigInteger privKey = keyByPath.getPrivKey();
      ECKey ecKey = ECKey.fromPrivate(privKey);
      String publicKey = ecKey.getPublicKeyAsHex();

      NetworkParameters networkParameters = MainNetParams.get();
      String privateKey = ecKey.getPrivateKeyEncoded(networkParameters).toString();
      String address = ecKey.toAddress(networkParameters).toString();

      resultList.add(address);

    stopwatch.stop();
    System.out.println(stopwatch.toString());

    return  resultList;
  }

   static List<ChildNumber> parsePath(@Nonnull String path) {
    String[] parsedNodes = path.replace("m", "").split("/");
    List<ChildNumber> nodes = new ArrayList<>();

    for (String n : parsedNodes) {
      n = n.replaceAll(" ", "");
      if (n.length() == 0) continue;
      boolean isHard = n.endsWith("'");
      if (isHard) n = n.substring(0, n.length() - 1);
      int nodeNumber = Integer.parseInt(n);
      nodes.add(new ChildNumber(nodeNumber, isHard));
    }

    return nodes;
  }



}
