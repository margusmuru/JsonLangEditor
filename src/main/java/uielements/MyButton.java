package uielements;

import javafx.scene.control.Button;

public class MyButton extends Button {

    public MyButton(ButtonType buttonType, String label){
        super(label);
        setButtonType(buttonType);
    }

    public MyButton(ButtonType buttonType) {
        super();
        setButtonType(buttonType);
    }

    public MyButton(String label) {
        super(label);
        // setButtonType(ButtonType.DARK);
    }

    private void setButtonType(ButtonType buttonType){
        switch (buttonType) {
            case DARK:
                setDarkButtonStyles();
                break;
            case GO:
                setGoButtonStyles();
                break;
            case ALERT:
                setAlertButtonStyles();
                break;
        }
    }

    private void setDarkButtonStyles() {
        setStyle("-fx-background-color: \n" +
                "        #090a0c,\n" +
                "        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n" +
                "        linear-gradient(#20262b, #191d22),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 5,4,3,5;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Arial\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;");
    }
    private void setAlertButtonStyles() {
        setStyle("-fx-background-color: \n" +
                "        #53130f,\n" +
                "        linear-gradient(#c02a21 0%, #6f1916 20%, #691d0f 100%),\n" +
                "        linear-gradient(#892919, #531412),\n" +
                "        radial-gradient(center 50% 0%, radius 100%, rgba(192,42,33,0.9), rgba(158,31,31,0));\n" +
                "    -fx-background-radius: 5,4,3,5;\n" +
                "    -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );\n" +
                "    -fx-font-family: \"Arial\";\n" +
                "    -fx-text-fill: linear-gradient(white, #d0d0d0);\n" +
                "    -fx-font-size: 12px;\n" +
                "    -fx-padding: 10 20 10 20;");
    }
    private void setGoButtonStyles() {
        // TODO
    }
}
