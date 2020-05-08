package lt.bit.java2;

public class Figura {
    private int a;
    private String name;
    private boolean x;

    public Figura() {}

    public Figura(int a, String name, boolean x) {
        this.a = a;
        this.name = name;
        this.x = x;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isX() {
        return x;
    }

    public void setX(boolean x) {
        this.x = x;
    }
}
