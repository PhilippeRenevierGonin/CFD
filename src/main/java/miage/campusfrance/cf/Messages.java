package miage.campusfrance.cf;


import javax.swing.*;

public class Messages {

    // source :
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static final void messageErreur(String msg) {
        afficher(ANSI_RED_BACKGROUND + msg + ANSI_RESET);
    }
    public static final void messageOk(String msg) {
        afficher(ANSI_GREEN_BACKGROUND + msg + ANSI_RESET);
    }
    public static final void message(String msg) {
        afficher(msg);
    }
    public static final void messageWarning(String msg) {
        afficher(ANSI_YELLOW_BACKGROUND + msg + ANSI_RESET);
    }


    public static final void messageMixte(String ok, String pb, String erreur) {
        afficher(ANSI_GREEN_BACKGROUND+ok+ANSI_RESET+ANSI_YELLOW_BACKGROUND+pb+ANSI_RESET+ANSI_RED_BACKGROUND + erreur + ANSI_RESET);
    }



    public static void addQuitListener(QuitListener ql) {
        if (retoursVisuels == null) {
            créerFenêtre();
        }
        retoursVisuels.setQuitListener(ql);
    }

    public static final void erreurConnexion(String msg) {
        Messages.messageErreur(msg);
        if (retoursVisuels == null) {
            créerFenêtre();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                retoursVisuels.erreurConnexion();
            }
        });
    }


    /* afficahge de la progression */
    static Console retoursVisuels;


    private static final void afficher(String msg) {
        System.out.println(msg);

        if (retoursVisuels == null) {
            créerFenêtre();
        }


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                retoursVisuels.ajouterTexteANSI(msg);
            }
        });

    }

    private static void créerFenêtre() {
        if (retoursVisuels == null) retoursVisuels = new ConsoleSwing();
    }

    public final static void progresser(int étape, int max) {
        if (retoursVisuels == null) créerFenêtre();



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                retoursVisuels.marquerÉtape(étape, max);
            }
        });

    }
}
