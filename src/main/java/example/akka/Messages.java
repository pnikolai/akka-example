package example.akka;

import java.io.Serializable;

public interface Messages {

  public static class BackendRegistration implements Serializable {
    @Override
    public String toString() {
      return "BackendRegistration";
    }
  }
}
