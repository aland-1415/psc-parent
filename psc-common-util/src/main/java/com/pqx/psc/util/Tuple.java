package com.pqx.psc.util;

/**
 * @author quanxing.peng
 * @date 2021/3/29
 */
public class Tuple {
    public static <A, B> Two<A, B> tuple(A a, B b) {
        return new Two<>(a, b);
    }

    public static <A, B, C> Three<A, B, C> tuple(A a, B b, C c) {
        return new Three<>(a, b, c);
    }

    public static <A, B, C, D> Four<A, B, C, D> tuple(A a, B b, C c, D d) {
        return new Four<>(a, b, c, d);
    }

    public static class Two<A, B> {
        public final A first;
        public final B second;

        public Two(A a, B b) {
            first = a;
            second = b;
        }
    }

    public static class Three<A, B, C> extends Two<A, B> {
        public final C third;

        public Three(A a, B b, C c) {
            super(a, b);
            third = c;
        }
    }

    public static class Four<A, B, C, D> extends Three<A, B, C> {
        public final D forth;

        public Four(A a, B b, C c, D d) {
            super(a, b, c);
            forth = d;
        }
    }
}
