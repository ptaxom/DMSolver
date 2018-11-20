package Model;

/**
 * Created by ptaxom on 20.11.2018.
 */
public enum EquivalentType {
        CLEARLY(1.0),
        CLEARLY_NOT(0.0),
        UNDEFINED(0.5);

    public final double type;


    EquivalentType(double type) {
        this.type = type;
    }

    public double getType() {
        return type;
    }

    public static EquivalentType typeFactory(double type)
    {
        if (type == 0.0) return EquivalentType.CLEARLY_NOT;
        if (type == 1.0) return EquivalentType.CLEARLY;
        return EquivalentType.UNDEFINED;
    }

}
