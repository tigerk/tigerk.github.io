# 三数之和
leetcode：https://leetcode.cn/problems/3sum/

## 题目：
给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请

你返回所有和为 0 且不重复的三元组。

注意：答案中不可以包含重复的三元组。

## 解题思路
思路就是四个字：定一找二，使用排序 + 双指针

## 实现代码
```
package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请
 * 你返回所有和为 0 且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * leetcode
 * https://leetcode.cn/problems/3sum/
 * 解题思路：
 * 思路就是四个字：定一找二
 * 排序 + 双指针
 */
public class ThreeSum {
    public static void main(String[] args) {
        int[] test1 = {-4, -1, -1, 0, 1, 2};
        List<List<Integer>> result1 = ThreeSum.threeSum(test1);
        System.out.println(result1);
//
//        int[] test2 = {0, 1, 1};
//        List<List<Integer>> result2 = ThreeSum.threeSum(test2);
//        System.out.println(result2);

//        int[] test3 = {1, -1, -1, 0};
//        List<List<Integer>> result3 = ThreeSum.threeSum(test3);
//        System.out.println(result3);
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        // 将数组进行排序，从小排序到大。
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            // 如果第一个数大于0，因为已经排序，后续所有的值都大于0
            if (nums[i] > 0) {
                break;
            }
            // 如果下一个值跟上一个值相同，则直接跳过。
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            int l = i + 1;
            int r = nums.length - 1;

            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum == 0) {
                    // 去重
                    while (r > l && nums[r] == nums[r - 1]) {
                        r--;
                    }
                    // 去重
                    while (r > l && nums[l] == nums[l + 1]) {
                        l++;
                    }

                    result.add(List.of(nums[i], nums[l], nums[r]));
                    l++;
                    r--;
                } else if (sum > 0) {
                    r--;
                } else {
                    l++;
                }
            }
        }

        return result;
    }
}

```