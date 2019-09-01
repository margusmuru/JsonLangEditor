package layouts;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import mainPackage.models.MessageType;

public class MessageBar implements MyLayout {
    private HBox messageBoxLayout;
    private static Label messageBox;

    public MessageBar() {
        setupMessageBox();
    }

    public void setMessage(String message, MessageType messageType) {
        messageBox.setText(message);
        switch (messageType) {
            case DEFAULT:
                messageBox.setStyle("-fx-text-fill: white");
                break;
            case SUCCESS:
                messageBox.setStyle("-fx-text-fill: #00ff00");
                break;
            case ERROR:
                messageBox.setStyle("-fx-text-fill: red");
                break;
        }
    }

    public HBox getLayout() {
        return messageBoxLayout;
    }

    private void setupMessageBox() {
        messageBoxLayout = new HBox();
        messageBox = new Label("");
        messageBoxLayout.getChildren().add(messageBox);
    }
}
