/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package numericalmethodsapp.methods;

import java.util.Scanner;

/**
 *
 * @author lopez
 */
public class MatrixProduct {
    public static void run (Scanner input){
        //Matrix A
        System.out.println("MATRIX A");
        System.out.print("Enter # of row/s: ");
        int arow = input.nextInt();
        System.out.print("Enter # of column/s: ");
        int acol = input.nextInt();

        //Matrix B
        System.out.println("MATRIX B");
        System.out.print("Enter # of row/s: ");
        int brow = input.nextInt();
        System.out.print("Enter # of column/s: ");
        int bcol = input.nextInt();

        if (acol != brow) {
            System.out.println("Matrix multiplication is NOT possible. Columns of Matrix A must equal rows of Matrix B.");
            return;
        }
        
        int[][] matrixA = new int[arow][acol];
        System.out.print ("Enter elements of matrix A:");
        for (int i = 0; i < arow; i++) {
            for (int j = 0; j < acol; j++) {
                matrixA[i][j] = input.nextInt();
            }
        }

        int[][] matrixB = new int[brow][bcol];
        System.out.println("Enter elements of matrix B:");
        for (int i = 0; i < brow; i++) {
            for (int j = 0; j < bcol; j++) {
                matrixB[i][j] = input.nextInt();
            }
        }

        int[][] result = new int[arow][bcol];

        // Perform matrix multiplication
        for (int i = 0; i < arow; i++) {
            for (int j = 0; j < bcol; j++) {
                for (int k = 0; k < acol; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        System.out.println("Resultant matrix after multiplication:");
        for (int i = 0; i < arow; i++) {
            for (int j = 0; j < bcol; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }
}
