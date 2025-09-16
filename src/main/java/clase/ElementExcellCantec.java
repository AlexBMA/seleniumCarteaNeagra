package clase;

public class ElementExcellCantec {

    private String titlu;

    private String linkVersuri;
    private String versuri;
    private String linkVideo;
    private String mentiune;

    public ElementExcellCantec() {
    }

    public ElementExcellCantec(String titlu) {
        this.titlu = titlu;
    }

    public ElementExcellCantec(String titlu, String linkVersuri) {
        this.titlu = titlu;
        this.linkVersuri = linkVersuri;
    }

    public ElementExcellCantec(String titlu, String linkVersuri, String versuri) {
        this.titlu = titlu;
        this.linkVersuri = linkVersuri;
        this.versuri = versuri;
    }

    public ElementExcellCantec(String titlu, String linkVersuri, String versuri, String linkVideo) {
        this.titlu = titlu;
        this.linkVersuri = linkVersuri;
        this.versuri = versuri;
        this.linkVideo = linkVideo;
    }

    public ElementExcellCantec(String titlu, String linkVersuri, String versuri, String linkVideo, String mentiune) {
        this.titlu = titlu;
        this.linkVersuri = linkVersuri;
        this.versuri = versuri;
        this.linkVideo = linkVideo;
        this.mentiune = mentiune;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getLinkVersuri() {
        return linkVersuri;
    }

    public void setLinkVersuri(String linkVersuri) {
        this.linkVersuri = linkVersuri;
    }

    public String getVersuri() {
        return versuri;
    }

    public void setVersuri(String versuri) {
        this.versuri = versuri;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public String getMentiune() {
        return mentiune;
    }

    public void setMentiune(String mentiune) {
        this.mentiune = mentiune;
    }
}
