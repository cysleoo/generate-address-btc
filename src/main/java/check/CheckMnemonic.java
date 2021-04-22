package check;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListTransducedAccessorImpl;

import sun.rmi.runtime.Log;

/**
 * 用来检查助记词是否合法
 * @author ssliu
 * @date 2021-04-22
 */
public class CheckMnemonic {

  public static void main(String[] args) throws MnemonicException, IOException {
    List<String> wordList = Files.readAllLines(Paths.get(args[0]));

    List<List<String>> prepareLists = wordList.parallelStream().map(word1 -> {
      List<String> mnemonicWord = new ArrayList<>(16);
      mnemonicWord.add("camera");
      mnemonicWord.add("first");
      mnemonicWord.add("soon");
      mnemonicWord.add("melody");
      mnemonicWord.add("violin");
      mnemonicWord.add("dutch");
      mnemonicWord.add(word1);
      mnemonicWord.add("y");
      mnemonicWord.add("z");
      mnemonicWord.add("blade");
      mnemonicWord.add("click");
      mnemonicWord.add("deposit");
      return mnemonicWord;
    }).collect(Collectors.toList());

    prepareLists.parallelStream().forEach(list -> {
      for (String word7 : wordList) {
        list.set(7, word7);
        for (String word8 : wordList) {
          list.set(8, word8);
          try {
            MnemonicCode.INSTANCE.check(list);


            System.out.println(list);
          } catch (MnemonicException e) {
          }
        }
      }
    });
  }
}
