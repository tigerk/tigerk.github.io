package leetcode;

/**
 * 最长回文子串:
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 * 如果字符串的反序与原始字符串相同，则该字符串称为回文字符串。
 * <p>
 * https://leetcode.cn/problems/longest-palindromic-substring/
 * <p>
 * 解题思路：
 */

public class LongestPalindrome {
    public static void main(String[] args) {
        String s = "a";
        // 输出："bab"
        String s1 = LongestPalindrome.longestPalindrome(s);
        System.out.println(s1);
    }

    public static String longestPalindrome(String s) {
        if (s.length() < 2) {
            return s;
        }

        int maxLength = 0;
        int begin = 0;
        for (int i = 0; i < s.length(); ) {
            int left = i;
            int right = i;
            while (right < s.length() - 1 && s.charAt(right) == s.charAt(right + 1)) {
                right++;
            }

            i = right + 1;

            while (left > 0 && right < s.length() - 1 && s.charAt(left - 1) == s.charAt(right + 1)) {
                left--;
                right++;
            }

            int length = right - left + 1;
            if (length > maxLength) {
                maxLength = length;
                begin = left;
            }
        }

        return s.substring(begin, begin + maxLength);
    }
}
