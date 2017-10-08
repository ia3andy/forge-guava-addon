package org.ia3andy.forge.addon.java.guava.test;

public class GuavaVersionsFinder {
    private final int toto;
    private final Integer titi;
    private final String tutu;

    public GuavaVersionsFinder(final int toto, final Integer titi, final String tutu){
        this.toto = toto;
        this.titi = titi;
        this.tutu = tutu;
    }

    public String methodWithBody(final Integer p1, final String p2){
        String var = p1 + p2 + tutu;
        return p1 + p2;
    }

    public int methodWithOnlyPrimArg(final int p1){
        String var = p1 + tutu;
        return p1;
    }

    public void noBodyMethod(final Integer p1) {}

    private int privateMethod(final Integer p1){
        return p1 + toto;
    }

    protected int protectedMethod(final Integer p1){
        return p1 + toto;
    }

    int packageProtectedMethod(final Integer p1){
        return p1 + toto;
    }

    public static void testStaticMethod(final int p1, final Integer p2, final String p3){

    }

}
