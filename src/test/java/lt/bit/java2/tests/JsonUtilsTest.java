package lt.bit.java2.tests;

import lt.bit.java2.Figura;
import lt.bit.java2.JsonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTest {

    @Test
    void testFigura2Json() {
        Figura f = new Figura(123, "Name", true);
        String result = JsonUtils.figura2json(f);
        assertEquals("{\"a\":123,\"name\":\"Name\",\"x\":true}", result);

        assertEquals("", JsonUtils.figura2json(null));
        assertEquals("{\"a\":0,\"x\":false}", JsonUtils.figura2json(new Figura()));
    }

    @Test
    void testFiguros2Json() {
        assertEquals("", JsonUtils.figuros2json(null));

        Collection<Figura> figuros = new ArrayList<>();
        assertEquals("", JsonUtils.figuros2json(figuros));

        figuros.add(new Figura());
        figuros.add(new Figura(123, "Name", true));
        assertEquals(
                "[{\"a\":0,\"x\":false},{\"a\":123,\"name\":\"Name\",\"x\":true}]",
                JsonUtils.figuros2json(figuros));
    }

    @Test
    void testFullName() {
        assertEquals("", JsonUtils.fullName(null, null));
        assertEquals("", JsonUtils.fullName("", ""));
        assertEquals("", JsonUtils.fullName(null, ""));
        assertEquals("", JsonUtils.fullName("", null));
        assertEquals("A", JsonUtils.fullName("", "A"));
        assertEquals("A", JsonUtils.fullName(null, "A"));
        assertEquals("A", JsonUtils.fullName("A", ""));
        assertEquals("A", JsonUtils.fullName("A", null));
        assertEquals("A B", JsonUtils.fullName("A", "B"));
    }

    @Test
    void testFuture() {
        //TODO reikia pabaigti
        Assertions.fail("reikia parasyti");
    }

}
