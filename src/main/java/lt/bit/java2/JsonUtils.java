package lt.bit.java2;

import com.google.gson.Gson;

import java.util.Collection;

public final class JsonUtils {

    private static Gson gson = new Gson();

    private JsonUtils() {}

    public static String figura2json(Figura f) {
        if (f == null) return "";
        return gson.toJson(f);
    }

    public static String figuros2json(Collection<Figura> figuros) {
        if (figuros == null || figuros.size() == 0) return "";
        return gson.toJson(figuros);
    }

    public static String fullName(String firstName, String lastName) {
        if ((firstName == null || firstName.isEmpty()) &&
                (lastName == null || lastName.isEmpty())) return "";

        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";

        return (firstName + " " + lastName).trim();
    }

}
