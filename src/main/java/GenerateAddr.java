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

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;

/**
 * @author ssliu
 * @date 2020-11-02
 */
public class GenerateAddr {

  public static void main(String[] args) {
    List<String> strList = Arrays.asList(args);

    long start= System.currentTimeMillis();
    int i = 1;
    while (i < 5000) {
      generateAddr(strList);
      i++;
    }
    long end = System.currentTimeMillis();

    long total = end - start;
    System.out.println("total cost: "+total);

    // System.out.println(new Gson().toJson(strings));

  }

  static List<String> generateAddr(List<String> wordsList) {
    ArrayList<String> resultList = new ArrayList<String>();

    DeterministicSeed deterministicSeed = new DeterministicSeed(wordsList, null, "", 0);
    DeterministicKeyChain deterministicKeyChain =
        DeterministicKeyChain.builder().seed(deterministicSeed).build();

    ArrayList<ChildNumber> childNumbers = new ArrayList<ChildNumber>();
    for (int i = 0; i < 10; i++) {
      String path = "m/44'/0'/0'/0/" + i;
      DeterministicKey keyByPath = deterministicKeyChain.getKeyByPath(parsePath(path), true);
      BigInteger privKey = keyByPath.getPrivKey();
      ECKey ecKey = ECKey.fromPrivate(privKey);
      String publicKey = ecKey.getPublicKeyAsHex();

      NetworkParameters networkParameters = MainNetParams.get();
      String privateKey = ecKey.getPrivateKeyEncoded(networkParameters).toString();
      String address = ecKey.toAddress(networkParameters).toString();

      resultList.add(address);
    }

    return  resultList;
  }

  public static List<ChildNumber> parsePath(@Nonnull String path) {
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
