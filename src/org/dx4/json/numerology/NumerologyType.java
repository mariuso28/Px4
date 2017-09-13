package org.dx4.json.numerology;

import java.util.HashMap;
import java.util.Map;

/* LATIN
1 = a, j, s,
2 = b, k, t,
3 = c, l, u,
4 = d, m, v,
5 = e, n, w,
6 = f, o, x,
7 = g, p, y,
8 = h, q, z,
9 = i, r,
*/

/* INDIAN
1 = A, I, J, Q, Y
2 = B, K, R
3 = C, G, L, S
4 = D, M, T
5 = E, H, N, X
6 = U, V, W
7 = O, Z
8 = F, P // no 9 (holy #)
 */

public enum NumerologyType {
	LATIN(new char[][]{{'a','j','s'},{'b','k','t'},{'c','l','u'},{'d','m','v'},{'e','n','w'},{'f','o','x'},{'g','p','y'},{'h','q','z'},{'i','r'}}),
	INDIAN(new char[][]{{'a','i','j','q','y'},{'b','k','r'},{'c','g','l','s'},{'d','m','t'},{'e','h','n','x'},{'u','v','w'},{'o','z'},{'f','p'}});
	
	private Map<Character,Integer> map = new HashMap<Character,Integer>();
	
	NumerologyType(char[][] chars)
	{
		for (int number = 0; number<chars.length; number++)
		{
			char[] group = chars[number];
			for (char c : group)
				map.put(c,number+1);
		}
	}
	
	public int getNumberForChar(char c)
	{
		return map.get(c);
	}
}
