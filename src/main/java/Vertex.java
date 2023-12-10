/*
 * At one point the Vertex was supposed to know much more about itself, in hindsight I would have just used a String
 * and not bothered creating a class.
 */

import java.nio.ByteBuffer;

public class Vertex {
    // instance variables
    private String name;

    // constructor
    public Vertex(String name) {
        this.name = name;
    }

    // simple getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // toString and compare
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Vertex))
            return false;
        Vertex other = (Vertex) o;
        boolean nameEquals = (this.name == null && other.name == null)
                || (this.name != null && this.name.equals(other.name));
        return nameEquals;
    }

    @Override
    public int hashCode() {
        ByteBuffer bytes = ByteBuffer.wrap(name.getBytes());
        return bytes.hashCode();
    }
}
