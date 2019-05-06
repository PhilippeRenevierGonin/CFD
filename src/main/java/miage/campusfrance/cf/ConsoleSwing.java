package miage.campusfrance.cf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ConsoleSwing implements Console {

    public static final float FONT_SIZE = 20f;


    JFrame retoursVisuels;
    ColorPane console;
    JProgressBar progressBar;
    JButton quitter;
    private QuitListener listener;

    public ConsoleSwing() {
        retoursVisuels = new JFrame("téléchargement antarès");
        retoursVisuels.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        retoursVisuels.setSize(920, 480);

        console = new ColorPane();
        console.setFont(console.getFont().deriveFont(FONT_SIZE));
        console.setEditable(false);
        JScrollPane pane = new JScrollPane(console);
        retoursVisuels.add(pane);

        progressBar = new JProgressBar();
        progressBar.setFont(progressBar.getFont().deriveFont(FONT_SIZE));
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        retoursVisuels.add("North", progressBar);

        quitter = new JButton("Quitter (si des téléchargements sont en cours, ils seront abandonnés)");
        quitter.setFont(quitter.getFont().deriveFont(FONT_SIZE));
        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) listener.quitter();
                System.exit(0);
            }
        });

        retoursVisuels.add("South", quitter);
        retoursVisuels.setVisible(true);
    }

    @Override
    public void marquerÉtape(int étape, int maximum) {
        progressBar.setMaximum(maximum);
        progressBar.setValue(étape);

        if (étape == maximum) {
            quitter.setText("Quitter (c'est fini)");
        }
    }

    @Override
    public void ajouterTexteANSI(String msg) {
        if (msg != null) console.appendANSI(msg);
    }

    @Override
    public void erreurConnexion() {
        quitter.setText("Quitter (Problème de Connexion)");
    }

    @Override
    public void setQuitListener(QuitListener ql) {
        listener = ql;
    }
}
