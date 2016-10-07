package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.task.ReadOnlyDatedTask;
import seedu.address.model.task.ReadOnlyTask;

public class PersonCard extends UiPart{

    private static final String FXML = "PersonListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label priority;
    @FXML
    private Label information;
    @FXML
    private Label datetime;
    @FXML
    private Label length;
    @FXML
    private Label recurrance;
    @FXML
    private Label tags;

    private ReadOnlyTask person;
    private int displayedIndex;

    public PersonCard(){

    }

    public static PersonCard load(ReadOnlyTask person, int displayedIndex){
        PersonCard card = new PersonCard();
        card.person = person;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(person.getName().fullName);
        id.setText(displayedIndex + ". ");
        priority.setText(person.getPriority().toString());
        information.setText(person.getInformation().fullInformation);
        if(person.isDated()){
            ReadOnlyDatedTask datedTask = (ReadOnlyDatedTask) person;
            datetime.setText(datedTask.getDateTime().toString());
            length.setText(datedTask.getLength().toString());
            recurrance.setText(datedTask.getRecurrance().toString());
        }else{
            datetime.setText("");
            length.setText("");
            recurrance.setText("");
        }
        tags.setText(person.tagsString());
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
