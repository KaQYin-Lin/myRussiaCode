package myPackge;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Filter;

//编写俄罗斯方块的主类
public class Tetris extends JPanel {

    //声明正在下落的方块

    private Tetromino currentOne = Tetromino.randomOne();

    //声明将要下落的方块
    private Tetromino nextOne = Tetromino.randomOne();


    //声明游戏的主区域

    private Cell[][] wall = new Cell[18][9];

    //声明单元格的值为48像素
    private static final int CELL_SIZE = 48;

    //声明游戏分数池
    int[] score_pool = {0,1,2,5,10};
    //当前游戏分数
    private int totalScore = 0;
    //当前已消除的行数
    private int totalLine = 0;

     //声明游戏的三种状态:游戏中，暂停，游戏结束
    public static final int PLAYING = 0;
    public static final int PAUSE = 1;
    public static final int GAMEOVER =2;
    //存放当前游戏的状态值
    private int game_state;
    //声明一个数组，显示游戏状态
    String[] show_state = {"P[pause]","C[continue]","S[replay]"};





    //载入方块图片
    public static BufferedImage I;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage O;
    public static BufferedImage S;
    public static BufferedImage T;
    public static BufferedImage Z;
    //载入背景图片
    public static BufferedImage backImage;

    //方块路径
    static {
        try {
            I = ImageIO.read(new File("images/I.png"));
            J = ImageIO.read(new File("images/J.png"));

            L = ImageIO.read(new File("images/L.png"));

            O = ImageIO.read(new File("images/O.png"));

            S = ImageIO.read(new File("images/S.png"));

            T = ImageIO.read(new File("images/T.png"));

            Z = ImageIO.read(new File("images/Z.png"));
            backImage = ImageIO.read(new File("images/background.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
//        super.paint(g);

        //绘制背景图片
        g.drawImage(backImage,0,0,null);

        //平移坐标轴
        g.translate(22,15);

        //绘制左边的游戏主区域
        paintWall(g);

        //绘制正在下落的四方格
        paintCurrentOne(g);

        //绘制下一个将要下落的四方格
        paintNextOne(g);

        //绘制游戏得分
        paintScore(g);

        //绘制游戏当前状态
        paintState(g);
        
    }


    //封装逻辑和监听
    public void start() {
        //开始游戏
        game_state = PLAYING;

        //键盘按下的监听事件
        KeyListener l = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //获取按下的是那个按键
                int code = e.getKeyCode();
                switch (code) {
                    //按向下的键
                    case KeyEvent.VK_DOWN:
                        sortDropAction(); //下落一格
                        break;
                    //按左键
                    case KeyEvent.VK_LEFT:  //左移动
                        moveLeftAction();
                        break;
                    //按右键
                    case KeyEvent.VK_RIGHT: //右移动
                        moveRightAction();
                        break;
                    //按上键
                    case KeyEvent.VK_UP:  //顺时针旋转
                        rorateRightAction();
                        break;
                    //空格触发瞬间下落
                    case KeyEvent.VK_SPACE: //瞬间下落
                        handDropAction();
                        break;
                    //暂停
                    case KeyEvent.VK_P:
                        //判断游戏状态
                        if (game_state == PLAYING) {

                            game_state = PAUSE;
                        }
                        break;
                    //继续游戏
                    case KeyEvent.VK_C:
                        //判断游戏状态
                        if (game_state == PAUSE) {
                            game_state = PLAYING;
                        }

                        break;
                    //重新开始游戏，墙壁重新开始
                    case KeyEvent.VK_S:

                        game_state = PLAYING;
                        wall = new Cell[18][9];
                        currentOne = Tetromino.randomOne();
                        nextOne = Tetromino.randomOne();
                        totalScore = 0;
                        totalLine = 0;

                        break;

                }
            }
        };

        //将俄罗斯方块窗口设置为焦点
        this.addKeyListener(l);
        this.requestFocus();

      while (true){
          //判断，当前游戏状态在游戏中时，每隔0.5秒下落
          if (game_state == PLAYING) {
              try {
                  Thread.sleep(500);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }


              //判断能否下落
              if (canDrop()) {
                  currentOne.softDrop();


              } else {

                  //嵌入墙中

                  landToWall();
                  //消行
                  destroyLine();
                  //游戏是否结束
                  if (isGameOver()) {
                      game_state = GAMEOVER;
                  } else {
                      currentOne = nextOne;
                      nextOne = Tetromino.randomOne();

                  }


              }
          }

          //重绘游戏画面
          repaint();
      }



    }



    //顺时针旋转
  public void  rorateRightAction(){
        currentOne.rotateRight();


        //判断越界或者重合
      if (outOfBounds() || concide()){
          //如果满足越界或者重合，那就回退到原来的形状
          currentOne.rorateLeft();
      }
  }


    //瞬间下落
   public void handDropAction(){
        //判断四方格能否下落

       while (true){
           if (canDrop()){

               currentOne.softDrop();
           }else {
                    break;
           }

       }

       //瞬间下落完毕后，把四方格嵌入到墙壁上
       landToWall();
       //消行
       destroyLine();
       //游戏是否结束
       if (isGameOver()){
            game_state = GAMEOVER;
       }else{
                //继续生成新的四方格
           currentOne = nextOne;
           nextOne = Tetromino.randomOne();
       }

   }


    //按一次下落一格
    public void sortDropAction(){
        //判断能否下落

        if (canDrop()){

            //当前的四方格下落一格
            currentOne.softDrop();
        }else {
            //将四方格嵌入到墙中
            landToWall();
            //然后判断能否消行
            destroyLine();
            //判断游戏是否结束
            if (isGameOver()){
                game_state = GAMEOVER;
            }else {
                //没有结束生成新的四方格
                currentOne = nextOne;
                //重新生成新的四方格的在再下一个四方格
                nextOne = Tetromino.randomOne();

            }
        }
    }

    //将四方格嵌入到墙中
    private void landToWall() {
        
        Cell[] cells = currentOne.cells; //获取当前的四方格

        //获取四方格的行和列，嵌入到墙中
        for (Cell cell : cells) {
            int row = cell.getRow();
            int col = cell.getCol();
            wall[row][col]=cell;


        }
        
    }


    //判断当前的四方格是否能下落

    public boolean canDrop(){
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int row  = cell.getRow();
            int col = cell.getCol();
            //判断是否到达底部
            if (row == wall.length -1){
                return false;
                //判断下面有没有方块
            }else if (wall[row +1][col] != null){
                return false;
            }
        }
        //没到达底部，而且而没有方块了
        return true;
    }

     //消行的方法
public void destroyLine(){
        //声明变量，统计当前消除的总行数
        int line = 0;

        Cell[] cells = currentOne.cells;//获取目前正在下落的四方格
    for (Cell cell : cells) {
        //获取四方格所在的行
        int row = cell.getRow();
        //判断行是否已满
        if(isFullLine(row)){
            line++;

            //消除行后，当前行到第0行中的方块到下落一下
            for (int i = row;i > 0;i--){
            System.arraycopy(wall[i-1],0,wall[i],0,wall[0].length);
            }
            //把第0行重置，掉落完毕了，最上面已经没有东西了
            wall[0] = new Cell[9];

        }

    }
    //根据消除的行数，来拿到消除行数所对应的积分
    totalScore +=score_pool[line];
    //统计一下消除的行数
    totalLine +=line;

}






    //判断当前行是否已满，满了就消，消了就记录行数
  public boolean isFullLine(int row){

        Cell[] cells = wall[row];

      for (Cell cell : cells) {
          if (cell == null) return  false;
      }
      //整行的单元格都不为空，返回true
      return true;
        
  }



    //判断游戏是否结束

    public boolean isGameOver(){
        Cell[] cells = nextOne.cells; //下一次要下落的方块


        //遍历每一个方块

        //获取小方块的行和列，在下一次
        //小方块生成的时候每一个小方块
        //的行和列在墙中的位置如果已经有元素了的话
        //也就是已经到顶的话，那么就说明游戏快结束了
        for (Cell cell : cells) {
            int row = cell.getRow();    //每一个方块的行
            int col = cell.getCol();    //每一个方块的列

            if (wall[row][col] != null){

                return true;
            }
            
        }
        //如果在行和列对应的位置没有任何方块，说明游戏还没有结束
        return false;
    }



    //绘制游戏当前状态
    private void paintState(Graphics g) {

            //游戏中，显示暂停
        if (game_state == PLAYING){

            g.drawString(show_state[PLAYING],500,660);
        }else if(game_state == PAUSE){

            g.drawString(show_state[PAUSE],500,660);

        }else if(game_state == GAMEOVER){
            g.drawString(show_state[GAMEOVER],500,660);

            g.setColor(Color.red);

            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,60));
            g.drawString("GAMEOVER!",30,400);
        }
    }


    //绘制游戏得分
    private void paintScore(Graphics g) {
        g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
        //游戏分数
        g.drawString("SCORES:"+totalScore,500,248);
        //消除的行数
        g.drawString("LINES:"+totalLine,500,430);
    }

    private void paintNextOne(Graphics g) {
        //获取下一个下落的四方格
        Cell[] cells = nextOne.cells;

        for (Cell cell : cells) {
            int x= cell.getCol() *CELL_SIZE +370;
            int y = cell.getRow() * CELL_SIZE +25;
            g.drawImage(cell.getImage(),x,y,null);

        }
    }

    private void paintCurrentOne(Graphics g) {
        //获取当前随机的四方格
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int x= cell.getCol() *CELL_SIZE;
            int y = cell.getRow() * CELL_SIZE;
            g.drawImage(cell.getImage(),x,y,null);
            
        }
    }

    //绘制游戏主区域的方法
    private void paintWall(Graphics g) {

        for(int i = 0;i < wall.length;++i){
            for(int j = 0;j < wall[i].length;++j){
                int x = j*CELL_SIZE;
                int y = i* CELL_SIZE;
                Cell cell = wall[i][j];//判断当前位置是否有小方块
                //没有则绘制，有则嵌入
                if(cell == null){
                    g.drawRect(x,y,CELL_SIZE,CELL_SIZE);

                }else {
                    g.drawImage(cell.getImage(),x,y,null);
                }
            }
        }
    }

//判断游戏是否出界
    public boolean outOfBounds(){
        Cell[] cells = currentOne.cells;

        for (Cell cell : cells) {
            int col = cell.getCol();

            int row = cell.getRow();

            if (row < 0 || row > wall.length -1 || col < 0 || col > wall[0].length -1){
                return true;
            }
        }

        return false;
    
    }

    //判断方块是否重合

    public boolean concide(){
        Cell[] cells = currentOne.cells;
        for (Cell cell : cells) {
            int col = cell.getCol();

            int row = cell.getRow();

            if(wall[row][col] !=null){

                return true;
            }
        }

        return false;
    }

    //单击按键后四方格左移
   public void moveLeftAction(){

        currentOne.moveLeft();

        //判断是否越界或者重合
       if(outOfBounds() || concide()){
           currentOne.moveRight();
       }
   }

   //按键一次四方格右移一次

    public void moveRightAction(){
        currentOne.moveRight();

        if(outOfBounds() || concide()){
            currentOne.moveLeft();
        }
    }


    public static void main(String[] args) {

        //创建窗口对象
        JFrame frame = new JFrame("russiaTube");
        //创建游戏界面,也就是面板
        Tetris panel = new Tetris();
        //将面板嵌入到窗口中
        frame.add(panel);

        //设置可见
        frame.setVisible(true);
        //窗口尺寸
        frame.setSize(810,940);
        //窗口居中
        frame.setLocationRelativeTo(null);
        //窗口关闭，程序终止
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        //把游戏主要逻辑封装在方法中

        panel.start();

    }
}
