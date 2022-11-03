package enums;

public enum Rank {
    DWA(2), TRZY(3), CZTERY(4), PIĘĆ(5), SZEŚĆ(6),
    SIEDEM(7), OSIEM(8), DZIEWIĘĆ(9), DZIESIĘĆ(10), WALET(11),
    DAMA(12), KRÓL(13), AS(14);

    public final int value;
    Rank(int value) {
        this.value = value;
    }
}
