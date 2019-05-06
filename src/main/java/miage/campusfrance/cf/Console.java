package miage.campusfrance.cf;


public interface Console {

    public void marquerÉtape(int étape, int maximum) ;
    public void ajouterTexteANSI(String msg);
    public void erreurConnexion();

    void setQuitListener(QuitListener ql);
}
