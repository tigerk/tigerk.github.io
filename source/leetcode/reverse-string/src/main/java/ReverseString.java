/**
 * 给定一个字符串 s 和一个整数 k，你需要对从字符串开头算起的每隔 2k 个字符的前 k 个字符进行反转。
 * <p>
 * 如果剩余字符少于 k 个，则将剩余字符全部反转。
 * <p>
 * 如果剩余字符小于 2k 但大于或等于 k 个，则反转前 k 个字符，其余字符保持原样。
 * <p>
 * 示例:
 * <p>
 * 输入: s = "abcdefg", k = 2
 * 输出: "bacdfeg"
 *
 * @author tigerkim
 */

public class ReverseString {
    public static String reverse(String s, int k) {
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i += 2 * k) {
            int start = i;

            int end = Math.min(chars.length - 1, start + k - 1);

            while (start < end) {
                chars[start] ^= chars[end];
                chars[end] ^= chars[start];
                chars[start] ^= chars[end];

                start++;
                end--;
            }
        }

        return new String(chars);
    }

    public static void main(String[] args) {
        String s = "abcdefg";
        // 打印 bacdfeg
        System.out.println(ReverseString.reverse(s, 2));
    }
}
