import java.util.Arrays;
import java.util.Scanner;

public class HmmMethods {
	double[][] result;
	double[][] element_wise_product;

	/* Prints the content of matrix */
	public void writeAnswer(int[][] matrix) {
		// System.out.print(matrix.length + " " + matrix[0].length);
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				System.out.print(matrix[i][j] + " ");
		System.out.println();

	}

	/**/
	public double[][] multMatrix(double[][] m1, double[][] m2) {
		int number_of_rows_m1 = m1.length;
		int number_of_columns_m1 = m1[0].length;
		int number_of_rows_m2 = m2.length;
		int number_of_columns_m2 = m2[0].length;
		if (number_of_columns_m1 != number_of_rows_m2)
			System.out
					.println("The matrix product is not defined: number_of_columns_m1 !=number_of_rows_m2 ");
		result = new double[number_of_rows_m1][number_of_columns_m2];
		for (int i = 0; i < number_of_rows_m1; ++i)
			for (int j = 0; j < number_of_columns_m2; ++j)
				for (int k = 0; k < number_of_columns_m1; ++k) {
					result[i][j] += m1[i][k] * m2[k][j];
				}
		// System.out.println(Arrays.deepToString(result));
		return result;
	}

	/*
	 * Stores the max probabilities in delta into the delta_matrix ATT! Stores
	 * the max values of delta as rows
	 */
	public void maxToMatrix(double[][] delta, double[][] delta_matrix,
			int[][] delta_state_index, int[][] delta_state_matrix,
			int column_index) {
		for (int i = 0; i < delta[0].length; i++) {
			
			delta_matrix[column_index][i] = delta[0][i];
			delta_state_matrix[column_index][i] = delta_state_index[0][i];
		}
	
		//return delta;
	}

	public double[][] multViterbiMatrix(double[][] m1, double[][] m2,
			double[][] m3, int column_number) {
		int number_of_rows_m1 = m1.length;
		int number_of_columns_m1 = m1[0].length;
		int number_of_rows_m2 = m2.length;
		int number_of_columns_m2 = m2[0].length;

		if (number_of_columns_m1 != number_of_rows_m2)
			System.out
					.println("The matrix product is not defined: number_of_columns_m1 !=number_of_rows_m2 ");
		result = new double[number_of_rows_m2][number_of_columns_m2];
		for (int i = 0; i < number_of_rows_m1; ++i) {

			for (int j = 0; j < number_of_columns_m2; ++j) {

				for (int k = 0; k < number_of_columns_m1; ++k) {

					result[j][k] = m1[i][k] * m2[k][j] * m3[j][column_number];
//					System.out.println("result" + "[" + j + "]" + "[" + k + "]"
//							+ "=" + m1[i][k] + "*" + m2[k][j] + "*"
//							+ m3[j][column_number] + "=" + result[j][k]);

				}
				// System.out.println();
			}
		}
		// System.out.println(Arrays.deepToString(result));
		return result;
	}

	/*
	 * Finds max of every row of the delta_multiplication_matrix. Stores a 1x4
	 * vector with the max of each row
	 */
//	public void findMax(double[][] delta_mult_matrix, double[][] delta_temp,
//			int[][] delta_state_index) {
//		double max;
//		int max_state_index = -1;
//		for (int i = 0; i < delta_mult_matrix.length; i++) {
//			max = -1;
//			for (int j = 0; j < delta_mult_matrix[0].length; j++) {
//				double temp = delta_mult_matrix[i][j];
//				if (temp > max) {
//					// System.out.println("temp = " + temp);
//					max = temp;
//					max_state_index = j;
//					
//				}
//				}
//			delta_temp[0][i] = max;
//			delta_state_index[0][i] = max_state_index;
//		}
//		
//
//	}
	
	public void findMax(double[][] vector, double[][] max_vector, int[][] max_state){
		int max_state_index = -1;
		for(int i = 0; i<vector.length; i++){
			double max = -1;
			
			for(int j = 0; j<vector[0].length; j++){

				double temp = vector[i][j];
				//System.out.println("temp: " + temp);
				if(temp > max){
					max = temp;
					max_state_index = j;
				}
				max_vector[0][i] = max;
				max_state[0][i] = max_state_index;
			}
		}
	}

	public void initMatrix(double[][] matrix, int row, int column, Scanner sc) {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {

				matrix[i][j] = sc.nextDouble();

				// System.out.print(matrix[i][j] +" ");
			}
			// System.out.println();

		}// System.out.println(Arrays.deepToString(matrix));
			// System.out.println();

	}

	public double[][] elementWiseProduct(double[][] matrix1,
			double[][] matrix2, int column_number) {
		int number_of_columns_m1 = matrix1.length;
		int number_of_rows_m2 = matrix2.length;
		element_wise_product = new double[number_of_columns_m1][number_of_rows_m2];
		for (int i = 0; i < matrix2.length; i++) {
			element_wise_product[0][i] = matrix1[0][i]
					* matrix2[i][column_number];
			// System.out.println(element_wise_product[0][i]);
		}
		return element_wise_product;
	}

	public int[] initVector(int[] vector, Scanner sc) {
		for (int i = 0; i < vector.length; i++) {
			vector[i] = sc.nextInt();
		}
		return vector;
	}

	/*
	 * This method finds the max value for each time. The index
	 * values,row/coulmn index, value for each time, we and update the
	 * delta_state_index corresponding index values for row/column
	 */
//	public void findMaxSolution(double[][] delta_matrix,
//			int[][] delta_state_matrix, int[][] delta_state_index) {
//		double max;
//
//		for (int i = 0; i < delta_matrix.length; i++) {
//			max = -1;
//			int max_state_index_i = -1;
//			int max_state_index_j = -1;
//			for (int j = 0; j < delta_matrix[0].length; j++) {
//				double temp = delta_matrix[i][j];
//				if (temp > max) {
//					
//					max = temp;
//					max_state_index_i = i;
//					max_state_index_j = j;
//				
//				}
//
//			}
//			delta_state_index[0][i] = delta_state_matrix[max_state_index_i][max_state_index_j];
//	
//		}
//	
//
//	}
	public void findMaxSolution(double[][] delta_matrix, int[][] delta_state_matrix, int[][] solution_vector){
		
		for(int i = 0; i<delta_matrix.length; i++){
			double max = -1;
			int max_state_index_i = -1;
			int max_state_index_j = -1;
			for(int j = 0; j<delta_matrix[0].length; j++){
				double temp = delta_matrix[i][j];
				if(temp > max){
					max = temp;
					max_state_index_i = i;
					max_state_index_j = j;
					
				}
				solution_vector[0][i] = delta_state_matrix[max_state_index_i][max_state_index_j];
				
			}
			
		}



	}

}
