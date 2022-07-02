package myPackge;


/*
* 编写七个不同的形状--I形状，每个形状占了四个格子
*
* */
public class I extends Tetromino {

    public I() {
        cells[0] = new Cell(0,4,Tetris.I);
        cells[1] = new Cell(0,3,Tetris.I);
        cells[2] = new Cell(0,5,Tetris.I);
        cells[3] = new Cell(0,6,Tetris.I);

        //I有两种旋转状态
        states = new State[2];

        //初始化两种状态的相对坐标
        states[0] = new State(0,0,0,-1,0,1,0,2);
        states[1] = new State(0,0,-1,0,1,0,2,0);

    }
}
