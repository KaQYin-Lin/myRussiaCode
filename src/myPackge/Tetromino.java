package myPackge;


/*
*
* 编写四方格父类
*  属性:Cell数组创建4个小方块
*  方法：左移，右移，下移 变形
*   静态方法：随机生成四方格
*
* */
public class Tetromino {

    protected Cell[] cells = new Cell[4];
    //创建旋转状态对象
    protected State[] states;

    //旋转的次数
    private int count = 10000;

    //编写顺时针旋转四方格的方法
    public void rotateRight(){

//        如果是田字格块，没必要转
        if (states.length == 0){

            return;
        }
        //旋转次数+1
        count++;
        //这个是状态数组，根据索引求出指定的状态，然后获取状态
        //中的参数来得到与0点的相对位置
        State s = states[count % states.length];
        Cell cell = cells[0];
        int row = cell.getRow();    //第零个小方块的行和列
        int col = cell.getCol();

        //设置其他小方块的行和列的相对位置
        cells[1].setRow(row + s.row1);
        cells[1].setCol(col + s.col1);

        cells[2].setRow(row + s.row2);
        cells[2].setCol(col + s.col2);

        cells[3].setRow(row + s.row3);
        cells[3].setCol(col + s.col3);


    }

    //逆时针旋转四方格的方法
    public void rorateLeft(){
        count--;
        State s = states[count % states.length];
        Cell cell = cells[0];
        int row = cell.getRow();    //第零个小方块的行和列
        int col = cell.getCol();

        //设置其他小方块的行和列的相对位置
        cells[1].setRow(row + s.row1);
        cells[1].setCol(col + s.col1);

        cells[2].setRow(row + s.row2);
        cells[2].setCol(col + s.col2);

        cells[3].setRow(row + s.row3);
        cells[3].setCol(col + s.col3);

    }



    //编写四方格旋转状态的内部类
    class State{
        //每个四方格都有四个格子，每个格子都有行和列
        //存储四方格各元素的相对的位置
        int row0,col0,row1,col1,row2,col2,row3,col3;

        public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3) {
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }

        public int getRow0() {
            return row0;
        }

        public void setRow0(int row0) {
            this.row0 = row0;
        }

        public State() {
        }

        @Override
        public String toString() {
            return "State{" +
                    "row0=" + row0 +
                    ", col0=" + col0 +
                    ", row1=" + row1 +
                    ", col1=" + col1 +
                    ", row2=" + row2 +
                    ", col2=" + col2 +
                    ", row3=" + row3 +
                    ", col3=" + col3 +
                    '}';
        }
    }



    //左移方法

    public void moveLeft(){
        //让四个小方块都向左移动一格
        for (Cell cell : cells) {
            cell.left();
        }

    }


    //右移方法
    public void moveRight(){
        //四个小方块都向右移动一格
        for (Cell cell :cells) {
            cell.right();
        }

    }

    //下移方法

    public void softDrop(){
        for (Cell cell :cells) {
            cell.drop();
        }

    }

    //随机生成7种不同形状的四方格
    public static Tetromino randomOne(){

        int num = (int)(Math.random() *7);
        Tetromino tetromino = null;

        switch (num){
            case 0:
                tetromino = new I();
                break;
            case 1:
                tetromino = new J();
                break;
            case 2:
                tetromino = new L();
                break;
            case 3:
                tetromino = new O();
                break;
            case 4:
                tetromino = new S();
                break;
            case 5:
                tetromino = new T();
                break;
            case 6:
                tetromino = new Z();
                break;


        }
        return tetromino;
    }

}
