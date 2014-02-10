import java.awt.Color;
import javax.swing.JPanel;


public class Block extends JPanel {
  final int row, column;
  private boolean lit=false;

  public Block(int column, int row){
    this.column=column;
    this.row=row;
  }

  public void light(){
    lit=true;
    this.setBackground(Color.GREEN);
  }

  public void unLight(){
    lit=false;
    this.setBackground(Color.LIGHT_GRAY);
  }

  public boolean isLit(){
    return lit;
  }
}
