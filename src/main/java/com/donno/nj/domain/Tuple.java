package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Optional;


public abstract class Tuple  implements Serializable
{

    public abstract <A> Optional<A> _1();

    public abstract <B> Optional<B> _2();

    public abstract <C> Optional<C> _3();

    public abstract <D> Optional<D> _4();

    public abstract <E> Optional<E> _5();

    public static <A, B> Tuple of(A a, B b) {
        return new Tuple2(a, b);
    }

//    public static <A, B, C> Tuple of(A a, B b, C c) {
//        return new Tuple3(a, b, c);
//    }
//
//    public static <A, B, C, D> Tuple of(A a, B b, C c, D d) {
//        return new Tuple4(a, b, c, d);
//    }
//
//    public static <A, B, C, D, E> Tuple of(A a, B b, C c, D d, E e) {
//        return new Tuple5(a, b, c, d, e);
//    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }


}