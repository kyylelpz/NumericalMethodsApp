package numericalmethodsapp;

import numericalmethodsapp.gui.MainWindow;

public class App {
    public static void main(String[] args) {
        credits();
        MainWindow.launch(MainWindow.class, args);
    }

    public static void credits(){
        System.out.println("Xynapse");
        System.out.println("BSCPE-2B");
        System.out.println("Lopez, Kyle Lemuel E.");
        System.out.println("Jaspe, Leobert V.");
        System.out.println("Trinidad, Jefhanie Bianca P.");
    }
}