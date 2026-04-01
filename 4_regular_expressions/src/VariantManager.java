
public class VariantManager {
    public static String[] getVariant(int number) {
        return switch (number) {
            case 1 -> new String[] { "(a|b)(c|d)E+G?", "P(Q|R|S)T(UV|W|X)*Z+", "1(0|1)*2(3|4)^536" };
            case 2 -> new String[] { "M?N^2(O|P)^3Q*R+", "(X|Y|Z)^3 8+(9|0)", "(H|i)(J|K)L*N?" };
            case 3 -> new String[] { "O(P|Q|R)^+ 2(3|4)", "A*B(C|D|E)F(G|H|i)^2", "J+K(L|M|N)*O?(P|Q)^3" };
            case 4 -> new String[] { "(S|T)(U|V)w*y+24", "L(M|N)O^3p*q(2|3)", "R*S(T|U|V)w(x|y|z)^2" };
            default -> throw new IllegalArgumentException("Invalid variant number (1-4)");
        };
    }
}