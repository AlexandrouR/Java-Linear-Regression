/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rngod
 */
public class town extends appModel {

    private DefaultTableModel modelTable;
    private String townName;

    public town(String name) {
        this.townName = name;
        this.modelTable = new DefaultTableModel(getTableRow(), getColumnNames());

    }

    public town() {

    }

    public DefaultTableModel getTableData() {
        return this.modelTable;
    }

    public String getTownName() {
        return this.townName;

    }

    public void setTableData(String[] rowData) {
        this.modelTable.addRow(rowData);
    }

}
