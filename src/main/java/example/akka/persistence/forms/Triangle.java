package example.akka.persistence.forms;

import example.akka.persistence.Color;

public class Triangle extends Form {

  public Triangle(int id, Color color) {
    super(id, color);
  }

  @Override
  public String toString() {
    return "Triangle{" + "id=" + getId() + ", color=" + getColor() + '}';
  }

}
