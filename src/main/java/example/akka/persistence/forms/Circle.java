package example.akka.persistence.forms;

import example.akka.persistence.Color;

public class Circle extends Form {

  public Circle(int id, Color color) {
    super(id, color);
  }

  @Override
  public String toString() {
    return "Circle{" + "id=" + getId() + ", color=" + getColor() + '}';
  }

}
