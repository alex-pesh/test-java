package misc;

public class MatrixRotate {

	public static void test() {

		int[][] matrix = generateMatrix();

		printMatrix(matrix);

		matrix = rotateMatrix(matrix);
		System.out.println();

		printMatrix(matrix);

	}



	private static int[][] generateMatrix() {
		int[][] matrix = new int[4][4];

		matrix[0][0] = 1;
		matrix[0][1] = 2;
		matrix[0][2] = 3;
		matrix[0][3] = 4;
		matrix[1][0] = 5;
		matrix[1][1] = 6;
		matrix[1][2] = 7;
		matrix[1][3] = 8;
		matrix[2][0] = 9;
		matrix[2][1] = 10;
		matrix[2][2] = 11;
		matrix[2][3] = 12;
		matrix[3][0] = 13;
		matrix[3][1] = 14;
		matrix[3][2] = 15;
		matrix[3][3] = 16;

/*		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				int value = (int) (Math.random() * 100);
				matrix[i][j] = (i+1)*(j+1);
			}
		}*/

		return matrix;
	}

	private static int[][] rotateMatrix(int[][] matrix) {

		int rows = matrix.length;
		int columns = matrix[0].length;

		int[][] result = new int[columns][rows];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				result[columns - 1 - j][rows - 1 - i] = matrix[i][j];
			}
		}

		return result;
	}


	private static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

}