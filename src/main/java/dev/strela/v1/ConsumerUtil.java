package dev.strela.v1;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConsumerUtil {
  public static <T> Consumer<T> empty() {
    return t -> {
    };
  }

  public static <T> BiConsumer<T, T> emptyBi() {
    return (t, u) -> {
    };
  }
}
