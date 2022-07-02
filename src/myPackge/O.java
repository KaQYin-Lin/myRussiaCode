package myPackge;

/*
 * 编写七个不同的形状--O形状
 *
 * */
public class O extends Tetromino {

    public O() {
        cells[0] = new Cell(0,4,Tetris.O);
        cells[1] = new Cell(0,5,Tetris.O);
        cells[2] = new Cell(1,4,Tetris.O);
        cells[3] = new Cell(1,5,Tetris.O);

        //1种状态
        states = new State[0];


    }
}
