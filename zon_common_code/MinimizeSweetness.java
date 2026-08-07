// Q1
// Given two boxes of A[] , B[] with size n , where each elements represents the sweetness , 
// Given the M students ,distribute the sweetness of A , B to each children 
// and you should minimize the total sweetness.



import java.util.*;
public class MinimizeSweetness {
    public int minSweetness(int[] a, int[] b, int m){
        return 0;
    }
    // Q2
    // Q2 - Find the unoccupied seat position with the maximum distance to the occupied site .
    // seats[] = {'O','U','U','U','O','O'};
    // Answer - 2nd indexed seat

    public int farthestUnoccupiedSeat(char[] seats){
        // traverse foreward & backward
        // update the distance from the current seat to the nearest  occupies seat
        int occupiedIdx = -1;
        // if there is no occupied seat ahead, record the distance as seat.length
        // otherwise, update the current unoccupied seat's distance as cur dix - last occupied idx
        int[] distance = new int[seats.length];
        // Arrays.fill(distance, -1);
        for(int i = 0; i < seats.length; i++){
            if(seats[i] == 'O'){
                occupiedIdx = i;
            }else{
                if(occupiedIdx == -1){
                    distance[i] = seats.length;
                }else{
                    distance[i] = i-occupiedIdx;
                }
            }
        }
        occupiedIdx = seats.length;
        int ans = -1;
        int ansIdx =-1;
        for(int i = seats.length-1; i >= 0; i--){
            if(seats[i] == 'O'){
                occupiedIdx = i;
            }else{
                if(occupiedIdx != seats.length){
                    distance[i] = Math.min(distance[i],occupiedIdx-i);
                }
                if(distance[i] != seats.length && distance[i] >= ans){
                    ans = distance[i];
                    ansIdx = i;
                }
            }
        }
        return ansIdx;
    }
    public int farthestUnoccupiedSeatO1(char[] seats) {
        int n = seats.length;
        int left = -1;   // 上一个 O 的位置,初始 -1 表示"左边还没出现过 O"
        int ans = 0;

        for (int right = 0; right < n; right++) {
            if (seats[right] == 'O') {
                if (left == -1) {
                    // 情况1:数组开头这一段全是 U,右边第一次遇到 O
                    ans = Math.max(ans, right - left - 1);
                } else {
                    // 情况3:被 left 和 right 两个 O 夹住的空隙,取中点距离
                    ans = Math.max(ans, (right - left) / 2);
                }
                left = right;
            }
        }

        // 情况2:数组结尾这一段全是 U(right 跑到底都没再遇到 O)
        if (seats[n - 1] == 'U') {
            ans = Math.max(ans, n - 1 - left);
        }

        return ans;
    }
    
    // Q3
    // Given the 2 D array with 2 colours validate if it is a valid chessborad .
    // Given a two-dimensional array containing exactly two colors, determine whether it forms a valid chessboard.

    // A board is valid when every horizontally or vertically adjacent cell has the opposite color.

    // One O(rows × columns) approach is to compare every cell with the expected color determined by the parity of row + column.

    // 0 - color1; 1 - color2
    public boolean checkChessboard(int[][] chessBoard){
        // 1.edge case: if chess board is valid, are there only three colors
        if(chessBoard == null || chessBoard.length == 0 || chessBoard[0]==null||chessBoard[0].length == 0) return false;
        // check each row -> should be two colors alternatively
        for(int i = 0; i < chessBoard.length; i++){
            int lastColor = chessBoard[i][0];
            for(int j = 1; j < chessBoard[i].length; j++){
                int curColor = chessBoard[i][j];
                if(lastColor == curColor) return false;
                lastColor = curColor;
            }
        }
        // check each col
        for(int j = 0; j < chessBoard[0].length; j++){
            int lastColor = chessBoard[0][j];
            for(int i = 1; i < chessBoard.length; i++){
                int curColor = chessBoard[i][j];
                if(lastColor == curColor) return false;
                lastColor = curColor;
            }
        }
        return true;
    }
    public boolean checkChessboardOptimized(int[][] chessBoard){
        // 1.edge case: if chess board is valid, are there only three colors
        if(chessBoard == null || chessBoard.length == 0 || chessBoard[0].length == 0) return false;
        // check the left cell and the upper cell of the current cell
        for(int i = 0; i < chessBoard.length; i++){
            if (chessBoard[i] == null || chessBoard[i].length != chessBoard[0].length) {
                return false;
            }
            for(int j = 0; j < chessBoard[0].length; j++){
                if(i > 0 && chessBoard[i][j] == chessBoard[i-1][j]) return false;
                if(j > 0 && chessBoard[i][j] == chessBoard[i][j-1]) return false;
            }
        }
        return true;
    }
}
