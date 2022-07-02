package myPackge;

/*
 * 编写七个不同的形状--L形状 每个形状占了四个格子
 *
 * */
public class L  extends Tetromino{

    public L() {
        cells[0] = new Cell(0,4,Tetris.L);
        cells[1] = new Cell(0,3,Tetris.L);
        cells[2] = new Cell(0,5,Tetris.L);
        cells[3] = new Cell(1,3,Tetris.L);

        //L有四种旋转状态
        states = new State[4];
        states[0] = new State(0,0,0,-1,0,1,1,-1);
        states[1] = new State(0,0,-1,0,1,0,-1,-1);
        states[2] = new State(0,0,0,1,0,-1,-1,1);
        states[3] = new State(0,0,1,0,-1,0,1,1);
    }
}
