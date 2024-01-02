public class Markt {
    private long marktnummer;
    private String email;
    private Benutzer anspartner;

    public Markt(long marktnummer, String email, Benutzer anspartner) {
        this.marktnummer = marktnummer;
        this.email = email;
        this.anspartner = anspartner;
    }

    public long getMarktnummer() {
        return marktnummer;
    }

    public void setMarktnummer(long marktnummer) {
        this.marktnummer = marktnummer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Benutzer getAnspartner() {
        return anspartner;
    }

    public void setAnspartner(Benutzer anspartner) {
        this.anspartner = anspartner;
    }
}
