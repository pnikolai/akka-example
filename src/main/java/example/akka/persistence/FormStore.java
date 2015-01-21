package example.akka.persistence;

import example.akka.persistence.forms.Form;
import java.util.ArrayList;

public class FormStore {

  ArrayList<Form> forms;

  public FormStore() {
    this(new ArrayList<>());
  }

  public FormStore(ArrayList<Form> forms) {
    this.forms = forms;
  }

  public void add(Form f) {
    forms.add(f);
  }

  public void remove(int id) {
    for (Form form : forms) {
      if (form.getId() == id) {
        forms.remove(form);
        break;
      }
    }
  }

  public FormStore copy() {
    return new FormStore(forms);
  }

  @Override
  public String toString() {
    return "FormStore{" + "forms=" + forms.toString() + '}';
  }
}
