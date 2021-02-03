package fr.fistin.api.utils;

import org.junit.Test;

public class IdentifiableTest
{
    @Test
    public void testIdentifiableOnEnum()
    {
        for (TestEnum value : TestEnum.values())
            System.out.println("Tested: " + value.getName() + " for id: " + value.getID() + " (ordinal is: " + value.ordinal() + ")");
    }

    private enum TestEnum implements IIdentifiable
    {
        NORMAL("normal", 0),
        NORMAL1("normal1", 1),
        NOT_NORMAL("not normal", 3),
        NOT_NORMAL1("not normal", 2);

        private final String name;
        private final int id;

        TestEnum(String name, int id)
        {
            this.name = name;
            this.id = id;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        @Override
        public int getID()
        {
            return this.id == this.ordinal() ? this.ordinal() : -this.id;
        }
    }
}
