import java.util.Objects;

public class Gutschein {
    private String name;
    private double preisProStueck;

    public Gutschein(String name, double preisProStueck) {
        this.name = name;
        this.preisProStueck = preisProStueck;
    }

    public String getName() {
        return name;
    }

    public double getPreisProStueck() {
        return preisProStueck;
    }

    @Override
    public String toString() {
        return "Gutschein{" +
                "name='" + name + '\'' +
                ", preisProStueck=" + preisProStueck +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gutschein gutschein = (Gutschein) obj;
        return Double.compare(gutschein.preisProStueck, preisProStueck) == 0 &&
                Objects.equals(name, gutschein.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, preisProStueck);
    }
}
