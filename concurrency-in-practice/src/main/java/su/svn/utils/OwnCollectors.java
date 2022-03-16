package su.svn.utils;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface OwnCollectors {

    static <T, R> Collector<T, ?, Optional<R>>
    minMax(Comparator<? super T> cmp, BiFunction<? super T, ? super T, ? extends R> finisher) {
        class Accumulator {
            T min;
            T max;
            boolean present;

            void add(T t) {
                if (present) {
                    if (cmp.compare(t, min) < 0) min = t;
                    if (cmp.compare(t, max) > 0) max = t;
                } else {
                    min = max = t;
                    present = true;
                }
            }

            Accumulator combine(Accumulator other) {
                if (!other.present) return this;
                if (!present) return other;
                if (cmp.compare(other.min, min) < 0) min = other.min;
                if (cmp.compare(other.max, max) > 0) max = other.max;
                return this;
            }
        }
        return Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                a -> a.present
                        ? Optional.of(finisher.apply(a.min, a.max))
                        : Optional.empty()
        );
    }
}

interface MemoOnTheContractCollector<T, A, R> {
    /**
     * Создать накопитель
     *
     * @return a function which returns a new, mutable result container
     */
    Supplier<A> supplier();

    /**
     * Добавить в накопитель
     *
     * @return a function which folds a value into a mutable result container
     */
    BiConsumer<A, T> accumulator();

    /**
     * Склеить два накопителя
     *
     * @return a function which combines two partial results into a combined
     * result
     */
    BinaryOperator<A> combiner();

    /**
     * Преобразовать накопитель в результат
     *
     * @return a function which transforms the intermediate result to the final
     * result
     */
    Function<A, R> finisher();

    /**
     * Флаги (ерунда всякая)
     *
     * @return an immutable set of collector characteristics
     */
    Set<java.util.stream.Collector.Characteristics> characteristics();
}