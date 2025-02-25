package br.com.lab.desafiolabequalizador.utils;

import java.util.List;
import java.util.function.Supplier;

public class PedidoUtils {

    public static <T> T singleOrThrow(List<T> list, Supplier<? extends RuntimeException> exceptionSupplier) {
        return list.size() > 1
                ? throwException(exceptionSupplier)
                : list.stream().findFirst().orElse(null);
    }

    private static <T> T throwException(Supplier<? extends RuntimeException> exceptionSupplier) {
        throw exceptionSupplier.get();
    }

}
