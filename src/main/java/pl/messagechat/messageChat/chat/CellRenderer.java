package pl.messagechat.messageChat.chat;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import pl.messagechat.messageChat.messages.User;

import java.util.Objects;

/**
 * A Class for Rendering users images / name on the userlist.
 */
public class CellRenderer implements Callback<ListView<User>, ListCell<User>> {
    @Override
    public ListCell<User> call(ListView<User> p) {

        ListCell<User> cell = new ListCell<User>(){

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();
                    Text name = new Text(user.getUserName());

                    ImageView statusImageView = new ImageView();
                    Image statusImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/" + user.getStatus().toString().toLowerCase() + ".png")), 16, 16,true,true);
                    statusImageView.setImage(statusImage);

                    ImageView pictureImageView = new ImageView();
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pictures/" + user.getPicture().toLowerCase() + ".png")),50,50,true,true);
                    pictureImageView.setImage(image);

                    hBox.getChildren().addAll(statusImageView, pictureImageView, name);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}
