package example.akka.persistence.forms;

import example.akka.persistence.Color;
import java.io.Serializable;

public abstract class Form implements Serializable {

  private int id;
  private Color color;

  public Form(int id, Color color) {
    this.id = id;
    this.color = color;
  }

  public int getId() {
    return id;
  }

  public Color getColor() {
    return color;
  }

}
