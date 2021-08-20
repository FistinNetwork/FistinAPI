package fr.fistin.api.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilsTest
{
    private static class TestClass
    {
        private final String str;

        public TestClass(String str)
        {
            this.str = str;
        }

        private String getStr()
        {
            return this.str + "/TEST/";
        }
    }

    private TestClass testClass;

    @BeforeEach
    public void setup()
    {
        this.testClass = new TestClass("test");
    }

    @Test
    public void shouldReturnStrField()
    {
        final String str = Utils.unsafeGet(TestClass.class, this.testClass, "str", Utils.TypeGet.FIELD);
        assertEquals(str, "test");
    }

    @Test
    public void shouldReturnGetStrMethodResult()
    {
        final String str = Utils.unsafeGet(TestClass.class, this.testClass, "getStr", Utils.TypeGet.METHOD);
        assertEquals(str, "test/TEST/");
    }

    @Test
    public void shouldThrowExceptionBecauseUnknownField()
    {
        assertThrows(FistinAPIException.class, () -> Utils.unsafeGet(TestClass.class, this.testClass, "unknown", Utils.TypeGet.FIELD));
    }

    @Test
    public void shouldThrowExceptionBecauseUnknownMethod()
    {
        assertThrows(FistinAPIException.class, () -> Utils.unsafeGet(TestClass.class, this.testClass, "unknown", Utils.TypeGet.METHOD));
    }
}
