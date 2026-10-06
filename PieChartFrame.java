import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class PieChartFrame extends JFrame {


   HashMap<String, Double> data;
   double total;
   double budget;


   public PieChartFrame(HashMap<String, Double> data, double total, double budget) {
       this.data = data;
       this.total = total;
       this.budget = budget;


       setTitle("Pie Chart");
       setSize(800, 600);
       setLocationRelativeTo(null);


       add(new ChartPanel());
       setVisible(true);
   }


   class ChartPanel extends JPanel {


       protected void paintComponent(Graphics g) {
           super.paintComponent(g);


           int width = getWidth();
           int height = getHeight();


           int size = Math.min(width, height) - 150;


           int x = (width - size) / 2;
           int y = (height - size) / 2 - 20;


           int start = 0;


           Color[] colors = {
                   Color.RED, Color.BLUE, Color.GREEN,
                   Color.ORANGE, Color.MAGENTA
           };


           int i = 0;
           int legendY = 20;


           double remaining = Math.max(budget - total, 0);
           double full = total + remaining;


           for (Map.Entry<String, Double> entry : data.entrySet()) {


               String cat = entry.getKey();
               double val = entry.getValue();


               int angle = (int) ((val / full) * 360);


               g.setColor(colors[i % colors.length]);
               g.fillArc(x, y, size, size, start, angle);


               g.fillRect(20, legendY, 15, 15);
               g.setColor(Color.BLACK);
               g.drawString(cat + " ₹" + val, 45, legendY + 12);


               start += angle;
               legendY += 25;
               i++;
           }


           // Remaining budget
           if (remaining > 0) {


               int angle = (int) ((remaining / full) * 360);


               g.setColor(Color.LIGHT_GRAY);
               g.fillArc(x, y, size, size, start, angle);


               g.fillRect(20, legendY, 15, 15);
               g.setColor(Color.BLACK);
               g.drawString("Remaining ₹" + remaining, 45, legendY + 12);
           }
       }
   }
}
