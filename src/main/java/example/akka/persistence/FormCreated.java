package example.akka.persistence;

import example.akka.persistence.forms.Form;
import java.io.Serializable;

public class FormCreated implements Serializable {

  private Form form;

  public FormCreated(Form form) {
    this.form = form;
  }

  public Form getForm() {
    return form;
  }

  @Override
  public String toString() {
    return "FormCreated{" + "form=" + form + '}';
  }
}
