/**
 * PositionTableCellRenderer.java
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class displays the position table and cells for the market display.
 *
 * @author hieldc
 * @version StockExVirtuoso.Version.1.0
 */
public class PositionTableCellRenderer extends DefaultTableCellRenderer {

    /**
     * Public Constructor method.
     */
    public PositionTableCellRenderer() {
        super();
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        String num = (String) table.getValueAt(row, 2);
        int intValue = Integer.parseInt(num);


        if (intValue <= 0) {
            setForeground(Color.black);
            setBackground(Color.red);

        } else {
            setForeground(Color.black);
            setBackground(Color.green);
        }
        if (column == 2) {
            setText("" + Math.abs(intValue));
        } else {
            setText(value.toString());
        }
        return this;
    }
}
//end of file