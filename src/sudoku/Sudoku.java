package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import sun.misc.Queue;

public class Sudoku {

    static int [][]board;
    static Node []nodes;
   
    public Sudoku() {
        board = new int[9][9];
        
    }
    
    static class Node implements Comparable<Object>{
        int x, y;
        int value;
        int number;
        Node parent;

        @Override
        public String toString() {
            return x + " " + y + " " + value;
        }

        @Override
        public int compareTo(Object o) {
            return this.value - ((Node)o).value;
        }
    }
    
    static void generateBoard() {
        board =  new int[][]{{8,1,2,0,5,0,6,4,9}
                            ,{0,4,3,6,8,2,1,7,0}
                            ,{6,7,5,4,9,1,2,8,3}
                            ,{1,5,0,2,3,0,8,9,6}
                            ,{3,6,9,8,4,5,7,2,1}
                            ,{2,8,7,1,6,9,5,0,4}
                            ,{5,2,0,9,7,4,3,6,8}
                            ,{4,3,8,5,2,0,9,1,7}
                            ,{7,9,6,3,0,8,4,0,2}};
    }
    
    static void printBoard() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    static void findNodes() {
        nodes = new Node[1000];
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0)
                    counter ++;
            }
        }
        counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    if (counter == 0) {
                        for (int k = 1; k < 10; k++) {
                            nodes[counter] = new Node();
                            nodes[counter].x = i;
                            nodes[counter].y = j;
                            nodes[counter].number = counter;
                            nodes[counter].value = k;
                            counter++;
                        }
                    } else {
                        for (int l = 1; l < 10; l++) {
                            for (int k = 1; k < 10; k++) {
                                nodes[counter] = new Node();
                                nodes[counter].x = i;
                                nodes[counter].y = j;
                                nodes[counter].number = counter;
                                nodes[counter].value = k;
                                nodes[counter].parent = nodes[(counter/9)-1+l];
                                counter++;
                            }
                        }
                    }
                }
            }
        }
    }
    
    static int checkBoardState() {
        boolean done = true;
        
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 8; j++) {
                if(board[i][j]==0)
                    done = false;
                for(int k = j + 1; k < 9; k++)
                    if(board[i][j]==board[i][k] && board[i][j]!=0) {
                        return 0;
                    }
            }

        for(int j = 0; j < 9; j++)
            for(int i = 0; i < 8; i++)
                for(int k = i + 1; k < 9; k++)
                    if(board[i][j]==board[k][j] && board[i][j]!=0)
                        return 0;

        for(int i = 0; i < 9; i += 3)
            for(int j = 0; j < 9; j += 3)
              // row, col is start of the 3 by 3 grid
                for(int pos = 0; pos < 8; pos++)
                    for(int pos2 = pos + 1; pos2 < 9; pos2++)
                        if(board[i + pos%3][j + pos/3]==board[i + pos2%3][j + pos2/3] && board[i + pos%3][j + pos/3]!=0)
                            return 0;
        if (done)
            return 2;
        return 1;
    }
    
    static void dfs() {
        int steps = 0;
        findNodes();
        boolean []flag = new boolean[nodes.length];
        Node cur = new Node();
        boolean done = false;
        Stack<Node> stack = new Stack<>();
        for (int i = 0; i < 9; i++) {
            stack.push(nodes[i]);
        }
        while(!stack.isEmpty()) {
            steps ++;
            if(done) {
                System.out.println("Number of steps : " + steps);
                break;
            }
            
            cur = stack.pop();
            if(!flag[cur.number]) {
                board[cur.x][cur.y] = cur.value;
                switch (checkBoardState()) {
                    case 2:
                        done = true;
                        System.out.println("done");
                        break;
                    case 1:
                        System.out.println("value " + cur.value + " in (" + cur.x + ", " + cur.y + ") is valid");
                        flag[cur.number] = true;
                        int childAddress;
                        if (cur.number < 9)
                            childAddress = cur.value * 9;
                        else
                            childAddress = 8 + ((cur.number-9)/81 + 1) * 81 + ((cur.value - 1)*9);
                        for (int i = 0; i < 9; i++) {
                            nodes[i + childAddress].parent = cur;
                            stack.push(nodes[i + childAddress]);
                            System.out.println("pushed : " + nodes[i + childAddress]);
                        }
                        break;
                    case 0:
                        System.out.println("oops value " + cur.value + " in (" + cur.x + ", " + cur.y + ") is not valid");
                        break;
                }                
            }
            printBoard();
        }
    }
    
    static void bfs() throws InterruptedException {
        int steps = 0;
        findNodes();
        boolean []flag = new boolean[nodes.length];
        Node cur = new Node();
        boolean done = false;
        Queue<Node> queue = new Queue<>();
        for (int i = 0; i < 9; i++) {
            queue.enqueue(nodes[i]);
        }
        while(!queue.isEmpty()) {
            steps ++;
            if(done) {
                System.out.println("Number of steps : " + steps);
                break;
            }
            
            cur = queue.dequeue();
            if(!flag[cur.number]) {
                board[cur.x][cur.y] = cur.value;
                Node temp = cur.parent;
                while (temp != null) {
                    board[temp.x][temp.y] = temp.value;
                    temp = temp.parent;
                }
                switch (checkBoardState()) {
                    case 2:
                        done = true;
                        System.out.println("done");
                        break;
                    case 1:
                        System.out.println("value " + cur.value + " in (" + cur.x + ", " + cur.y + ") is valid");
                        flag[cur.number] = true;
                        int childAddress;
                        if (cur.number < 9)
                            childAddress = cur.value * 9;
                        else
                            childAddress = 8 + ((cur.number-9)/81 + 1) * 81 + ((cur.value - 1)*9);
                        for (int i = 0; i < 9; i++) {
                            nodes[i + childAddress].parent = cur;
                            queue.enqueue(nodes[i + childAddress]);
                            System.out.println("queued : " + nodes[i + childAddress]);
                        }
                        break;
                    case 0:
                        System.out.println("oops value " + cur.value + " in (" + cur.x + ", " + cur.y + ") is not valid");
                        break;
                }
            }
            printBoard();
        }
    }
    
    static void ucs() throws InterruptedException {
        int steps = 0;
        findNodes();
        boolean []flag = new boolean[nodes.length];
        Node cur = new Node();
        boolean done = false;
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (int i = 0; i < 9; i++) {
            queue.add(nodes[i]);
        }
        while(!queue.isEmpty()) {
            steps ++;
            if(done) {
                System.out.println("Number of steps : " + steps);
                break;
            }
            cur = queue.remove();
            if(!flag[cur.number]) {
                board[cur.x][cur.y] = cur.value;
                Node temp = cur.parent;
                while (temp != null) {
                    board[temp.x][temp.y] = temp.value;
                    temp = temp.parent;
                }
                switch (checkBoardState()) {
                    case 2:
                        done = true;
                        System.out.println("done");
                        break;
                    case 1:
                        System.out.println("value " + cur.value + " in (" + cur.x + ", " + cur.y + ") is valid");
                        flag[cur.number] = true;
                        int childAddress;
                        if (cur.number < 9)
                            childAddress = cur.value * 9;
                        else
                            childAddress = 8 + ((cur.number-9)/81 + 1) * 81 + ((cur.value - 1)*9);
                        for (int i = 0; i < 9; i++) {
                            nodes[i + childAddress].parent = cur;
                            queue.add(nodes[i + childAddress]);
                            System.out.println("queued : " + nodes[i + childAddress]);
                        }
                        break;
                    case 0:
                        System.out.println("oops value " + cur.value + " in (" + cur.x + ", " + cur.y + ") is not valid");
                        break;
                }
            }
            printBoard();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Scanner s = new Scanner(System.in);
        int input = 0;
        while (input != 4) {       
            System.out.println("Please enter the algorithm you prefer : ");
            System.out.println("1. DFS");
            System.out.println("2. BFS");
            System.out.println("3. UCS");
            System.out.println("4. EXIT");
            input = s.nextInt();
            switch (input) {
                case 1:
                    generateBoard();
                    printBoard();
                    dfs();
                    break;
                case 2:
                    generateBoard();
                    printBoard();
                    bfs();
                    break;
                case 3:
                    generateBoard();
                    printBoard();
                    ucs();
                    break;
                case 4:
                    break;
            }
        }
        
    }
    
}