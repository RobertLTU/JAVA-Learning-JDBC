package lt.bit.java2;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("YES");

        List<Figura> figuros = new ArrayList<>();
        figuros.add(new Figura(123, "Vardas 1", true));
        figuros.add(new Figura(456, "Vardas 2", false));
        figuros.add(new Figura(789, "Vardas 3", true));

        /*
            [{"a":123, "name":"Vardas 1", "x":true}, {...}]
         */
        System.out.println(JsonUtils.figuros2json(figuros));
    }
}


