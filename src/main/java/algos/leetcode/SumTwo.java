package algos.leetcode;

/**
 * https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/description/
 * <p>
 * <h4>167. Two Sum II - Input Array Is Sorted</h4>
 * Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers such that they add up to a specific target number. Let these two numbers be numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.
 * <p>
 * Return the indices of the two numbers index1 and index2, each incremented by one, as an integer array [index1, index2] of length 2.
 * <p>
 * The tests are generated such that there is exactly one solution. You may not use the same element twice.
 * <p>
 * Your solution must use only constant extra space.
 * <p>
 * Constraints:
 * <li> 2 <= numbers.length <= 3 * 104
 * <li> -1000 <= numbers[i] <= 1000
 * <li> numbers is sorted in non-decreasing order.
 * <li> -1000 <= target <= 1000
 * <li> The tests are generated such that there is exactly one solution.
 */
class SumTwo {

	public static void main(String[] args) {
		int[] result = new SumTwo().twoSum(new int[]{2,7,11,15}, 9);
		System.out.printf("%d, %d%n", result[0], result[1]);
	}

	public int[] twoSum(int[] numbers, int target) {

		if (numbers.length < 2 || numbers.length > 30_000) {
			throw new java.lang.IllegalArgumentException("Wrong array length");
		}

		if (target < -1000 || target > 1000) {
			throw new java.lang.IllegalArgumentException("Wrong target value");
		}

		int left = 0;
		int right = numbers.length - 1;

		while (left < right) {
			int sum = numbers[left] + numbers[right];
			if (sum == target) {
				return new int[]{left + 1, right + 1};
			}

			if (sum < target) {
				left++;
			} else {
				right--;
			}
		}

		return new int[]{left + 1, right + 1};
	}
}