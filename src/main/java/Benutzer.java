public class Benutzer {

    private Anrede anrede;
    private String name;
    private long mitarbeiternummer;
    private long marktnummer;


    public Benutzer(Anrede anrede ,String name, long mitarbeiternummer,long marktnummer) {
        this.anrede = anrede;
        this.name = name;
        this.mitarbeiternummer = mitarbeiternummer;
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

    public long getMitarbeiternummer() {
        return mitarbeiternummer;
    }

    public void setMitarbeiternummer(long mitarbeiternummer) {
        this.mitarbeiternummer = mitarbeiternummer;
    }

    public long getMarktnummer() {
        return marktnummer;
    }

    public void setMarktnummer(long marktnummer) {
        this.marktnummer = marktnummer;
    }
}
