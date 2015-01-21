package example.akka.persistence.commands;

public class DeleteForm extends Command{

  private int id;

  public DeleteForm(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @Override
  public String toString() {
    return "DeleteForm with id: " + id;
  }

}
