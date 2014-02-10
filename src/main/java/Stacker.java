
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import java.io.*;


public class Stacker extends javax.swing.JFrame {
  final static int columns=7;
  final static int rows=15;
  static GridLayout layout = new GridLayout(rows,columns);
  final static Block[][] blocks = new Block[rows][columns];
  final static JPanel mainPanel = new JPanel();
  final static StatsWriter stats = new StatsWriter();
  static JButton button;
  static JTextArea textBox, winBox, loseBox;
  static int curRow;
  static int numBlocks;
  static Thread t;
  static boolean running=false;
  static boolean newRow=false;
  static final int WAIT=200;
  static int wait=WAIT;

  public Stacker( String name){
    super(name);
    setResizable(false);
  }


   /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        Stacker frame = new Stacker("Jeff's Stacker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addcomponents(frame.getContentPane());
        //Display the window.
//        frame.setSize(600, 500);
        frame.pack();
        frame.setVisible(true);
    }

  private static void addcomponents(final Container pane) {
    mainPanel.setLayout(layout);
    for(int i=0;i<blocks.length;i++){
      for(int j=0; j<blocks[i].length;j++){
        blocks[i][j]= new Block(i,j);
        blocks[i][j].setPreferredSize(new Dimension(40,40));
        blocks[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.add(blocks[i][j]);
      }
    }
    //Setup Major prize line
    for(int i=0;i<columns;i++){
      blocks[0][i].setBorder(BorderFactory.createLineBorder(Color.RED));
      blocks[5][i].setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    final JPanel controls = new JPanel();
      button = new JButton("start");
      textBox= new JTextArea("Welcome!");
      winBox= new JTextArea("Wins: "+stats.getWins());
      loseBox= new JTextArea("Loses: "+stats.getLosses());
      textBox.setEditable(false);
      winBox.setEditable(false);
      loseBox.setEditable(false);

      textBox.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        newRow=true;
                curRow++;
      }
    });

      //run button
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               if(!running){
                 running=true;
                 unLightBoard();
                 start();
               }
               else{
//                 newRow=true;
//                 curRow++;
               }
            }
        });
        controls.setLayout(new GridLayout(2, 2));
    controls.add(button);
    controls.add(textBox);
    controls.add(winBox);
    controls.add(loseBox);

    pane.add(mainPanel, BorderLayout.NORTH);
        pane.add(controls, BorderLayout.SOUTH);

  }

  protected static void start() {
    button.setEnabled(false);
    textBox.setFocusable(true);
    curRow=1;
    numBlocks=3;
    textBox.setText("Good Luck!");
    winBox.setText("Wins "+stats.getWins());
    loseBox.setText("Loses "+stats.getLosses());
    t=new Thread(new Rotate());
    t.start();
  }


  private static class Rotate implements Runnable {
     public void run() {
      Block block1, block2,block3;
     int loc1= (columns/2)-1;
     int loc2= columns/2;
     int loc3=(columns/2)+1;
     int rot1=+1; //+1 or -1
     int rot2=+1; //+1 or -1
     int rot3=+1; //+1 or -1

     System.out.println("Starting");

     try {
       while(numBlocks>0 && running){
         block1= blocks[rows-curRow][loc1];
         block2= blocks[rows-curRow][loc2];
         block3= blocks[rows-curRow][loc3];
         if(numBlocks>=0){
           block1.light();
           if(numBlocks >1){
             block2.light();
             if(numBlocks>2)
               block3.light();
           }
         }

         try {
           Thread.sleep(wait);
         } catch (InterruptedException e) {
           e.printStackTrace();
         }

         //if the row changed, do not unlight the blocks
         if(!newRow){
           block1.unLight();
           if(numBlocks >1){
             block2.unLight();
             if(numBlocks>2)
               block3.unLight();
           }
         }
         else{
           if(curRow>2)
             validateStack();

         }

         newRow=false;

         if(loc1+1 <= columns-1 && loc1-1>=0){
           loc1+=rot1;
         }
         else{
           rot1=toggleRotation(rot1);
           loc1+=rot1;
         }

         if(loc2+1 <= columns-1 && loc2-1>=0)
           loc2+=rot2;
         else{
           rot2=toggleRotation(rot2);
           loc2+=rot2;
         }

         if(loc3+1 <= columns-1 && loc3-1>=0)
           loc3+=rot3;
         else{
           rot3=toggleRotation(rot3);
           loc3+=rot3;
         }
       }
       }catch(Exception e){
         System.out.println("I wasn't done!");
         System.out.println(e);
       }
     }

     private void validateStack() {
      int rowNum=rows-curRow;
      for(int i=0; i<blocks[0].length;i++){
        if(blocks[rowNum+1][i].isLit() && !blocks[rowNum+2][i].isLit()){
          dropBlock(rowNum+1,i );
        }
      }


      if(curRow>5  && numBlocks>2){
        numBlocks=2;
      }
      else if(curRow>8  && numBlocks>1){
        numBlocks=1;
      }
      if(curRow==rows+1){
        endGame("Winner!!!");
      }
      else if(numBlocks==0){
         endGame("Game Over, You Lose!");
      }

      wait-=wait*0.05;
     }

     private void endGame(String status) {
      textBox.setText(status);
      button.requestFocusInWindow();
    running=false;
     curRow=rows-1;
     numBlocks=3;
     wait=WAIT;
     if(status.equals("Winner!!!"))
       stats.addWin();
     else
       stats.addLoss();

     winBox.setText("Wins "+stats.getWins());
    loseBox.setText("Loses "+stats.getLosses());
    button.setEnabled(true);

  }

  protected void dropBlock(int row, int column) {
     System.out.println("Droping block row: "+row+" column: "+column);
     numBlocks--;
     for(;row<=rows-1;row++){
       System.out.println("row: "+ row);
       blocks[row][column].unLight();
       if(row+1<=rows-1){
         blocks[row+1][column].light();
         System.out.println("Lighting block row: "+ (row+1));
       }
       try {
         Thread.sleep(50);
       } catch (InterruptedException e) {
         System.out.print(e);
       }

     }
   }

  private int toggleRotation(int rot) {
    if(rot>0)
      return -1;
    else
      return+1;

  }
 }

   //This method will unlight all of the blocks on the board
   public static void unLightBoard(){
     for(int i=0;i<blocks.length;i++)
       for(int j=0;j<blocks[0].length;j++)
         blocks[i][j].unLight();
   }


  /**
   * @param args
   */
  public static void main(String[] args) {
    createAndShowGUI();

  }

}

