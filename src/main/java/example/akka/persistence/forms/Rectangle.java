package example.akka.persistence.forms;

import example.akka.persistence.Color;

public class Rectangle extends Form {

  public Rectangle(int id, Color color) {
    super(id, color);
  }

  @Override
  public String toString() {
    return "Rectangle{" + "id=" + getId() + ", color=" + getColor() + '}';
  }

}
