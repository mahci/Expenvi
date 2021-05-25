package envi.tools;

import java.util.Objects;

public class Pair<F, S> {
    public final F first;
    public final S second;

    // ===============================================================================

    /**
     * Constructor
     * @param first First elemnt
     * @param second Second elements
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Equals
     * @param o Object
     * @return Boolean
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
    }

    /**
     * Check hashCode
     * @return Integer
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    /**
     * Create a Pair
     * @param a First obj.
     * @param b Second obj.
     * @param <A> Generic
     * @param <B> Generic
     * @return New Pair
     */
    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<>(a, b);
    }
}
