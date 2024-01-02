public class Benutzer {

    private Anrede anrede;
    private String name;
    private long marktnummer;


    public Benutzer(Anrede anrede ,String name, long marktnummer) {
        this.anrede = anrede;
        this.name = name;
        this.marktnummer = marktnummer;
    }

    public Anrede getAnrede() {
        return anrede;
    }

    public void setAnrede(Anrede anrede) {
        this.anrede = anrede;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMarktnummer() {
        return marktnummer;
    }

    public void setMarktnummer(long marktnummer) {
        this.marktnummer = marktnummer;
    }
}
