import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RandomJsonTest {

  @org.junit.jupiter.api.Test
  void randomValue() {
    for (int i = 0; i < 100; i++) {
      System.out.println(RandomJson.randomValue());
    }
  }

  // @org.junit.jupiter.api.Test
  // void randomAddr() throws Exception {
  //   // String s = RandomJson.randomAddr(0);
  //   // System.out.println(s);
  //
  //   System.out.println(RandomJson.generateAddr2());
  //
  // }

  @org.junit.jupiter.api.Test
  void randomArray() {
    for (int i = 0; i < 100; i++) {
      System.out.println(RandomJson.randomArray().size());

    }
  }


}
