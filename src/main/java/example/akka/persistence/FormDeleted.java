package example.akka.persistence;

import java.io.Serializable;

public class FormDeleted implements Serializable {

  private int id;

  public FormDeleted(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "FormDeleted{" + "id=" + id + '}';
  }

}
