package com.example.tictactoactivity;
public class Model {
    private int[][] grid;
    private final int computerShape = 2;
    private final int playerShape = 1;
    private boolean isGameOver;
    private int lastComputerTurnColumn;
    private int lastComputerTurnRow;
    private int winner; //0 - no one, 1 - player, 2 - computer
    private boolean easyDifficulty;
    private int[][] gridToPlay; //הלוח שבסוף יכיל את השינויים שבתור של המחשב שיתעדכנו בלוח הסופי
    public Model(boolean difficultyEasy) {
        easyDifficulty = difficultyEasy;
        gridToPlay = new int[3][3];
        winner = 0;
        lastComputerTurnColumn = -1; // -1 because the computer didnt play yet
        lastComputerTurnRow = -1;
        isGameOver = false;
        grid = new int[3][3]; // 0 - nothing, 1 - x, 2 - O
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }
    }
    public void playTurn() {
        if(easyDifficulty) {
            boolean found = false;
            while (!found) {
                int column = (int)(Math.random()*3);
                int row = (int)(Math.random()*3);
                if(isEmpty(column,row,grid)) {
                   found = true;
                   setShape(column,row,computerShape);
                   lastComputerTurnColumn = column;
                   lastComputerTurnRow = row;
                }
            }
        }
        else {
            int[][] gridCopy = new int[3][3];
            copyGridToOtherGrid(grid,gridCopy);
            calcMove(gridCopy,0);
            updateByDifference(grid,gridToPlay);
        }
        winner = checkForWinners(grid);
    }
    private void updateByDifference(int[][] grid, int[][] copy) {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(grid[i][j] != copy[i][j]) {
                    lastComputerTurnColumn = i;
                    lastComputerTurnRow = j;
                    grid[i][j] = copy[i][j];
                }
            }
        }
    }
    private int[] calcMove(int[][] arr, int stepCount) { //פעולה רקורסיבית שמחשבת את כל הקומבינציות האפשריות ובוחרת את הטובה מבינהם

        int step = stepCount + 1;
        int[] maxResult = {-100,0}; //very low number
        int[][] bestGrid = null;
        int win = checkForWinners(arr);
        if(win == computerShape) {
            return new int[]{10,step};
        }
        else if(win == playerShape) {
            return new int[]{-10,step};
        }
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                if(isEmpty(i,j,arr)) {
                    int[][] copy = new int[3][3];
                    copyGridToOtherGrid(arr,copy);
                    copy[i][j] = computerShape;
                    int[] result = predictPlayerMove(copy,step);
                    if(result[0] > maxResult[0]) {
                        maxResult = new int[]{result[0],result[1]};
                        bestGrid = copy;
                    }
                    else if(result[0] == maxResult[0]) {
                        if(result[1] < maxResult[1]) {
                            maxResult = new int[]{result[0],result[1]};
                            bestGrid = copy;
                        }
                    }
                }
            }
        }

//        gridToPlay = bestGrid;
        if(bestGrid != null) {
            copyGridToOtherGrid(bestGrid,gridToPlay);
        }
        else {
            return new int[]{0,step};
        }

        return new int[]{maxResult[0],maxResult[1]};
    }
    private int[] predictPlayerMove(int[][] arr, int stepCount) {
        int step = stepCount + 1;
        int minResult = 100; //very high number
        int[][] bestGrid = null;
        int win = checkForWinners(arr);
        if(win == computerShape) {
            return new int[]{10,step};
        }
        else if(win == playerShape) {
            return new int[]{-10,step};
        }
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                if(isEmpty(i,j,arr)) {
                    int[][] copy = new int[3][3];
                    copyGridToOtherGrid(arr,copy);
                    copy[i][j] = playerShape;
                    int[] result = calcMove(copy,step);
                    if(result[0] < minResult) {
                        minResult = result[0];
                        bestGrid = copy;
                    }

                }
            }
        }
        if(bestGrid == null) {
            return new int[]{0,step};
        }
        return new int[]{minResult,step};
    }
    private void copyGridToOtherGrid(int[][] grid1, int[][] copyOfGrid) {
        for(int i = 0; i < grid1.length; i++) {
            for(int j = 0; j < grid1[i].length; j++) {
                copyOfGrid[i][j] = grid1[i][j];
            }
        }
    }

    public void resetGrid() {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }
    }
    public boolean isEmpty(int column, int row, int[][] arr) {
        return arr[column][row] == 0;
    }
    public int[][] getGrid() {
        return grid;
    }
    private boolean isGridFull(int[][] arr) {
        boolean is = true;
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                is &= !isEmpty(i,j,arr);
            }
        }
        return is;
    }
    private int countEmptySpots() {
        int count = 0;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(isEmpty(i,j,grid)) {
                    count++;
                }
            }
        }
        return count;
    }
    public boolean setShape(int column, int row, int value) {
        boolean hasSucceeded = isEmpty(column,row,grid);
        if(isEmpty(column,row,grid)) {
            grid[column][row] = value;
            winner = checkForWinners(grid);
        }
        isGameOver |= isGridFull(grid);
        return hasSucceeded;
    }
    public int getLastComputerTurnColumn() {
        return lastComputerTurnColumn;
    }
    public int getLastComputerTurnRow() {
        return lastComputerTurnRow;
    }
    private int checkForWinners(int[][] arr) {
        if(arr[0][0] == arr[0][1] && arr[0][0] == arr[0][2] && arr[0][0] != 0) {
            return arr[0][0];
        }
        else if(arr[1][0] == arr[1][1] && arr[1][0] == arr[1][2] && arr[1][0] != 0) {
            return arr[1][0];
        }
        else if(arr[2][0] == arr[2][1] && arr[2][0] == arr[2][2] && arr[2][0] != 0) {
            return arr[2][0];
        }
        else if(arr[0][0] == arr[1][0] && arr[0][0] == arr[2][0] && arr[0][0] != 0) {
            return arr[0][0];
        }
        else if(arr[0][1] == arr[1][1] && arr[0][1] == arr[2][1] && arr[0][1] != 0) {
            return arr[0][1];
        }
        else if(arr[0][2] == arr[1][2] && arr[0][2] == arr[2][2] && arr[0][2] != 0) {
            return arr[0][2];
        }
        else if(arr[0][0] == arr[1][1] && arr[0][0] == arr[2][2] && arr[0][0] != 0) {
            return arr[0][0];
        }
        else if(arr[0][2] == arr[1][1] && arr[0][2] == arr[2][0] && arr[0][2] != 0) {
            return arr[0][2];
        }
        return 0;
    }
    public int getWinner() {
        return winner;
    }
    public boolean isGameOver() {
        return isGameOver || winner != 0;
    }
}
