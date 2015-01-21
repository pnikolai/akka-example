package example.akka;

import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.SnapshotOffer;
import example.akka.persistence.commands.CreateForm;
import example.akka.persistence.FormCreated;
import example.akka.persistence.FormDeleted;
import example.akka.persistence.FormStore;
import example.akka.persistence.commands.Command;
import example.akka.persistence.commands.DeleteForm;
import example.akka.persistence.commands.MakeSnapshot;
import example.akka.persistence.commands.Show;
import example.akka.persistence.commands.ThrowException;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class PersistenceActor extends AbstractPersistentActor {

  private FormStore store = new FormStore();

  @Override
  public PartialFunction<Object, BoxedUnit> receiveRecover() {
    return ReceiveBuilder.
        match(SnapshotOffer.class, ss -> {
          System.out.println("Snapshot " + ss);
          store = (FormStore) ss.snapshot();
            })
        .match(FormCreated.class, e -> {
          System.out.println("Add form event: " + e.getForm());
          store.add(e.getForm());
        })
        .match(FormDeleted.class, e -> {
          System.out.println("Remove form event: " + e.getId());
          store.remove(e.getId());
            })
        .build();
  }

  @Override
  public PartialFunction<Object, BoxedUnit> receiveCommand() {
    return ReceiveBuilder
        .match(Command.class, this :: handleCommand)
        .build();
  }

  private void handleCommand(Command cmd) {
    if (cmd instanceof CreateForm) {
      FormCreated created = new FormCreated(((CreateForm) cmd).getForm());
      persist(created, evt -> store.add(evt.getForm()));
    } else if (cmd instanceof DeleteForm) {
      FormDeleted deleted = new FormDeleted(((DeleteForm) cmd).getId());
      persist(deleted, evt -> store.remove(evt.getId()));
    } else if (cmd instanceof Show) {
      System.out.println(store);
    } else if (cmd instanceof ThrowException) {
      throw new RuntimeException("Excepted!");
    } else if (cmd instanceof MakeSnapshot) {
      saveSnapshot(store.copy());
    } else {
      throw new RuntimeException("Unknown command!");
    }
  }

  @Override
  public String persistenceId() {
    return "PersistenceActor-id1";
  }

}
