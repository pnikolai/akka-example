package example.akka.persistence.commands;

import example.akka.persistence.forms.Form;

public class CreateForm extends Command {

  private final Form form;

  public CreateForm(Form form) {
    this.form = form;
  }

  public Form getForm() {
    return form;
  }

  @Override
  public String toString() {
    return "CreateForm{" + "form=" + form + '}';
  }

}
