package myPackge;


import java.awt.image.BufferedImage;
import java.util.Objects;

/*
*
* 编写一个小方块类
*  属性：行，列，每个小方块的图片
*  方法：左移，右移，下移一格
*
*
*
*
*
*
* */
public class Cell {

    private int row;           //行

        private  int col;                   //列
       private BufferedImage image;     //小方块图片

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Cell() {
    }

    public Cell(int row, int col, BufferedImage image) {

        this.row = row;
        this.col = col;
        this.image = image;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                col == cell.col &&
                Objects.equals(image, cell.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, image);
    }

    //左移一格的方法

    public void left(){
        col--;
    }

    //右移方法

    public void right(){
        col++;

    }

    //下移
   public void drop(){
        row++;
    }

}
