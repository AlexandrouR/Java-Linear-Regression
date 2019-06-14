/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex
 */
public class appModel {

    // This is the row that is used when a table is initialiased
    private final Object[][] initRow = null;
    //This is used for Mean, Population,Variance ... etc table
    private final Object[][] sumInitRow = {{" "}, {"N"}, {"Mean"}, {"Variance"}, {"Std. Dev"}};
    //Table Column Names
    private final String[] columnNames = {"Price - 100 000’s",
        "Bathrooms",
        "Site Area - 1000’s Sf",
        "Living Space - 1000’s Sf",
        "Garages",
        "Rooms",
        "Bedrooms",
        "Age"
    };
    private final Object[] sumColNames2 = {
        "Sx",
        "Sx2",
        "Sy",
        "Sy2",
        "Sxy"
    };

    private final Object[] sumColNames = {" ",
        "Xi",
        "Price"
    };
    private final Object[] errorColNames = {"Xi",
        "Y",
        "Forecasted Y",
        "Std. Err. of Estimate"
    };
    private final Object[] linearColName = {"R", "R2", "Slope", "Intercept"};
    private final Object[] linearSumCol = null;
    private final Object[][] linearSumRow = {{"∑X"}, {"∑X2"}, {"∑Y"}, {"∑Y2"}, {"∑XY"}};

    // Table Models 
    // Big table model
    private DefaultTableModel modelTable;
    // Mean, Population, Variace Table etc.....
    private DefaultTableModel dataSumTable;

    private DefaultTableModel dataSumTable2;

    private DefaultTableModel linearRegTable;

    private DefaultTableModel selectedTownModel;

    private DefaultTableModel linearSumTable;

    private DefaultTableModel errorTable;

    private DefaultComboBoxModel comboModel;

    private DefaultTableModel correlationCoefficient;

    private JFileChooser jfc;
    ArrayList<town> myTowns = new ArrayList<>();
    LinkedList<String> linearValues = new LinkedList<>();

    // Get Functions 
    public ArrayList<String> getTownNames() {

        town tempTown;
        ArrayList<String> townNames = new ArrayList();
        if (!myTowns.isEmpty()) {

            for (int i = 0; i < myTowns.size(); i++) {
                tempTown = myTowns.get(i);
                townNames.add(tempTown.getTownName());
            }
            return townNames;

        } else {

            return null;
        }

    }

    public ArrayList getTownList() {

        return this.myTowns;
    }

    public String[] getColumnNames() {

        return columnNames;
    }

    public Object[][] getTableRow() {

        return initRow;
    }

    public String getTownName() {
        town theTown = new town();
        for (int i = 0; i < myTowns.size(); i++) {
            theTown = myTowns.get(i);
        }
        return theTown.getTownName();
    }

    public DefaultTableModel getSelectedTownModel() {
        return this.selectedTownModel;
    }

    public DefaultTableModel getDataSumModel() {
        return this.dataSumTable;

    }

    public DefaultTableModel getLinearTableModel() {
        return this.linearRegTable;
    }

    public DefaultTableModel getErrorTableModel() {
        return this.errorTable;
    }

    public DefaultTableModel getDataSumTable2() {
        return this.dataSumTable2;

    }

    public DefaultTableModel getCorrelationCoefficientTable() {
        return this.correlationCoefficient;

    }

    public DefaultTableModel getSpecificTown(String tName) {
        town theTown;
        for (int i = 0; i < myTowns.size(); i++) {
            theTown = myTowns.get(i);
            if (theTown.getTownName().equals(tName)) {
                return theTown.getTableData();

            }
        }
        return null;
    }

    public DefaultTableModel getTable() {
        return this.modelTable;
    }

    public ArrayList getTowns() {
        return this.myTowns;
    }

    //Calculation Function
    public double calOrderReg(int length,DefaultTableModel table) {
       
        double val3 = 0;
        for (int i = 0; i < length; i++) {
            val3 = val3 + calLinearRegression(i, 00,table);

        }
        return val3;
    }

    public double calVariance(int data , DefaultTableModel table) {
        System.out.println("Invoked");
        DefaultTableModel tempMod = selectedTownModel;
        double variance = 0;

        for (int i = 0; i < tempMod.getRowCount(); i++) {
            String value = (String) tempMod.getValueAt(i, data);

            double x = calMean(data,table);
            
            variance = variance + Math.pow(Double.parseDouble(value) - x, 2);
            
        }
        double finalVal = variance / tempMod.getRowCount();

        finalVal = roundDec(finalVal);

        return finalVal;

    }

    public double calStandardDeviation(int data , DefaultTableModel table) {

        double standardDeviation = Math.sqrt(calVariance(data,table));

        standardDeviation = roundDec(standardDeviation);

        return standardDeviation;
    }

    public double calMean(int data,DefaultTableModel table) {

        double mean = 0;

      
        if (table.getRowCount() >= 2) {
            for (int i = 0; i < table.getRowCount(); i++) {
                mean = mean + Double.parseDouble((String) table.getValueAt(i, data));

            }

            mean = mean / table.getRowCount();
            mean = roundDec(mean);

            return mean;
        } else {
            return 0;
        }
    }

    public double calLinearB1(double sxy, double sxx) {
        double b1 = sxy / sxx;

        return b1;
    }

    public double calSxx(int data,DefaultTableModel table) {
        double Sxx = 0;
        double xmsum ;
       
        for (int i = 0; i < table.getRowCount(); i++) {

            xmsum = (Double.parseDouble((String) table.getValueAt(i, data)) - calMean(data,table));

            Sxx = Sxx + xmsum * xmsum;
        }

        return Sxx;
    }

    public double calSxy(int data,DefaultTableModel table) {
        double Sxy = 0;
        double ymsum ;
        double yi ;
        
        for (int i = 0; i < table.getRowCount(); i++) {

            ymsum = (Double.parseDouble((String) table.getValueAt(i, data)) - calMean(data,table));
            yi = (Double.parseDouble((String) table.getValueAt(i, 0)) - calMean(0,table));

            Sxy = Sxy + ymsum * yi;
        }
        return Sxy;
    }

    public double calSyy(int data,DefaultTableModel table) {
        double Syy = 0;
        double ymsum;
        double mean_of_Y = calMean(0,table);
       
        for (int i = 0; i < table.getRowCount(); i++) {

            ymsum = (Double.parseDouble((String)table.getValueAt(i, 0)) - mean_of_Y );
            ymsum *= ymsum;
            Syy = Syy + ymsum;
        }
        return Syy;
    }

    public double calr2(double data, double data1, double data2) {
        double r2 = (data * data) / (data1 * data2);
        return r2;
    }

    public double calculateR(int data,DefaultTableModel table) {
        double Sxx = calSxx(data,table);
        double Sxy = calSxy(data,table);
        double Syy = calSyy(data,table);
        double r2 = calr2(Sxy, Sxx, Syy);
        double r = Math.sqrt(r2);
        
        return r;
    }

    public double calcEstimate(int data, double point, DefaultTableModel table) {

       
        double Sxx = calSxx(data,table);
        double Sxy = calSxy(data,table); 
        double b1 = calLinearB1(Sxy, Sxx);
        double ym = calMean(0,table);
        double xm = calMean(data,table);
        double b0 = ym - b1 * xm;
       

         double y = b1 * point + b0;

        return y;
    }

    public String testingTheR(DefaultTableModel table) {
        DefaultTableModel testModel = getSelectedTownModel();
        ArrayList<correlationCoefficient> coefficient = new ArrayList<>();

        for (int i = 0; i < testModel.getColumnCount(); i++) {
            correlationCoefficient temp = new correlationCoefficient(testModel.getColumnName(i), calculateR(i,table));
            coefficient.add(temp);

        }

        //Sort The array List
        for (int i = 0; i < coefficient.size(); i++) {
            for (int j = 0; j < coefficient.size() - 1; j++) {
                if (coefficient.get(j).getCoefficient() > coefficient.get(j + 1).getCoefficient()) {
                    correlationCoefficient temp;
                    temp = coefficient.get(j);
                    coefficient.set(j, coefficient.get(j + 1));
                    coefficient.set(j + 1, temp);

                }

            }

        }
        //Fill the table model

        return coefficient.get(coefficient.size() - 2).getCategoryName();
    }

    public double calSx(int data) {
        double Sx = 0;

        DefaultTableModel tempMod = selectedTownModel;
        for (int i = 0; i < tempMod.getRowCount(); i++) {

            Sx = Sx + (Double.parseDouble((String) tempMod.getValueAt(i, data)));

        }

        return Sx;
    }

    public double calSy(int data) {
        double Sy = 0;

        DefaultTableModel tempMod = selectedTownModel;
        for (int i = 0; i < tempMod.getRowCount(); i++) {

            Sy = Sy + (Double.parseDouble((String) tempMod.getValueAt(i, 0)));

        }
        return Sy;
    }

    public double calLinearRegression(int columnNumber, double point,DefaultTableModel table) {

        double Sxx = calSxx(columnNumber,table);
        double Sxy = calSxy(columnNumber,table);
        double Syy = calSyy(columnNumber,table);
        double r2 = calr2(Sxy, Sxx, Syy);
        double r = Math.sqrt(r2);
        double b1 = calLinearB1(Sxy, Sxx);
        double ym = calMean(0,table);
        double xm = calMean(columnNumber,table);
        double b0 = ym - b1 * xm;
        double Sx = calSx(columnNumber);
        double Sy = calSy(columnNumber);

        double y = b1 * point + b0;

       
        return y;
    }

    //Initialiazing and Support Function SEARCH DELETE ETC....
    public void intiSumTable(int index,DefaultTableModel table) {
          DefaultTableModel tempMod = selectedTownModel;
        dataSumTable.setValueAt(tempMod.getRowCount(), 1, 1);
        dataSumTable.setValueAt(tempMod.getRowCount(), 1, 2);
        
        dataSumTable.setValueAt(calMean(index,table), 2, 1);
        dataSumTable.setValueAt(calMean(0,table), 2, 2);
       
        dataSumTable.setValueAt(calVariance(index,table), 3, 1);
        dataSumTable.setValueAt(calVariance(0,table), 3, 2);
        
        dataSumTable.setValueAt(calStandardDeviation(index,table), 4, 1);
        dataSumTable.setValueAt(calStandardDeviation(0,table), 4, 2);
        double Sxx = calSxx(index,table);
        double Sxy = calSxy(index,table);
        double Syy = calSyy(index,table);
        double r2 = calr2(Sxy, Sxx, Syy);
        double r = Math.sqrt(r2);
        double b1 = calLinearB1(Sxy, Sxx);
        double ym = calMean(0,table);
        double xm = calMean(index,table);
        double b0 = ym - b1 * xm;
        double Sx = calSx(index);
        double Sy = calSy(index);

        linearRegTable.setValueAt( roundDec(r), 0, 0);
        linearRegTable.setValueAt(roundDec(r2), 0, 1);
        linearRegTable.setValueAt(roundDec(b1), 0, 2);
        linearRegTable.setValueAt(roundDec(b0), 0, 3);
        dataSumTable2.setValueAt( roundDec(Sx), 0, 0);
        dataSumTable2.setValueAt(roundDec(Sxx), 0, 1);
        dataSumTable2.setValueAt(roundDec(Sy), 0, 2);
        dataSumTable2.setValueAt(roundDec(Syy), 0, 3);
        dataSumTable2.setValueAt(roundDec(Sxy), 0, 4);

    }

    public Object[] addNewTableRow() {

        Object[] empty = new Object[columnNames.length];

        for (int i = 0; i < 8; i++) {
            empty[i] = "0";

        }
        return empty;
    }

    public void readTextFile(String townName) throws FileNotFoundException {

        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a TEXT file");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {

            try {
                File file = new File(jfc.getSelectedFile().getPath());
                BufferedReader br = new BufferedReader(new FileReader(file));
                Object[] tableLines = br.lines().toArray();
                for (int k = 0; k < myTowns.size(); k++) {
                    if (townName.equals(myTowns.get(k).getTownName())) {
                        for (int i = 0; i < tableLines.length; i++) {
                            String line = tableLines[i].toString();

                            String[] dataRow = line.split("\\s");

                            this.myTowns.get(k).setTableData(dataRow);

                        }
                    }
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(AlgorithmsCoursework.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Something went wrong when reading the file" + JOptionPane.ERROR_MESSAGE);

        }

    }

    public double[] getColData(int colIndex, DefaultTableModel townName) {
        double[] colData = new double[townName.getRowCount()];

        for (int i = 0; i < townName.getRowCount(); i++) {

            colData[i] = colData[i] + Double.parseDouble(townName.getValueAt(i, colIndex).toString());
        }

        return colData;

    }

    public void dataSumTable(JTable table) {
        dataSumTable = new DefaultTableModel(sumInitRow, sumColNames);
        table.setModel(dataSumTable);

    }

    public void linearRegTable(JTable table) {
        linearRegTable = new DefaultTableModel(new Object[][]{{""}}, linearColName);
        table.setModel(linearRegTable);
    }

    public void linearSumTable(JTable table) {
        linearSumTable = new DefaultTableModel(linearSumRow, linearSumCol);
        table.setModel(linearSumTable);
    }

    public void errorTable(JTable table) {
        errorTable = new DefaultTableModel(new Object[][]{{""}}, errorColNames);
        table.setModel(errorTable);
    }

    public void menuBox(JComboBox item) {
        comboModel = new DefaultComboBoxModel(columnNames);
        item.setModel(comboModel);
    }

    public String displayR( DefaultTableModel table) {
        DefaultTableModel testModel = getSelectedTownModel();
        ArrayList<correlationCoefficient> coefficient = new ArrayList<>();
        // Fill in the array List
        for (int i = 0; i < testModel.getColumnCount(); i++) {
            correlationCoefficient temp = new correlationCoefficient(testModel.getColumnName(i), calculateR(i,table));
            coefficient.add(temp);

        }

        //Sort The array List
        for (int i = 0; i < coefficient.size(); i++) {
            for (int j = 0; j < coefficient.size() - 1; j++) {
                if (coefficient.get(j).getCoefficient() > coefficient.get(j + 1).getCoefficient()) {
                    correlationCoefficient temp;
                    temp = coefficient.get(j);
                    coefficient.set(j, coefficient.get(j + 1));
                    coefficient.set(j + 1, temp);

                }

            }

        }
        //Fill the table model
        for (int k = 0; k < coefficient.size(); k++) {
            double x = coefficient.get(k).getCoefficient();
            x = Math.round(x * 1000.0) / 1000.0;
            correlationCoefficient.addRow(new Object[]{coefficient.get(k).getCategoryName(), x});

        }

        return coefficient.get(coefficient.size() - 2).getCategoryName();
    }

    public void displayR(double[] data, int i) {
        double value = data[i];
        linearRegTable.setValueAt(roundDec(value), i + 1, 0);

    }

    public void displayError(double xi, double y, double forec, double dif) {
        errorTable.addRow(new Object[]{xi,y,forec,dif});
        
       
        

    }

    public double roundDec(double value) {
       DecimalFormat df = new DecimalFormat(".######");
       df.setRoundingMode(RoundingMode.CEILING);
        value = Double.parseDouble(df.format(value));
       return  value;
    }

    public void createNewTown(String townName) {

        town newTown = new town(townName);
        myTowns.add(newTown);

    }

    public void setSelectedTownModel(DefaultTableModel x) {
        selectedTownModel = x;
    }

    public void townsComboBox(JComboBox townBox) {
        String tNames[] = null;
        for (int i = 0; i < getTownNames().size(); i++) {
            tNames[i] = getTownNames().get(i);

        }
        comboModel = new DefaultComboBoxModel(tNames);
        townBox.setModel(comboModel);

    }

    public void dataSumTable2(JTable table) {
        dataSumTable2 = new DefaultTableModel(new Object[][]{{""}}, sumColNames2);
        table.setModel(dataSumTable2);

    }

    public void correlationCoefficient(JTable table) {
        correlationCoefficient = new DefaultTableModel(new Object[][]{{""}}, new Object[]{"Category Name", "R"});
        table.setModel(correlationCoefficient);
    }

    public void deleteTown(String townName) {
        for (int i = 0; i < myTowns.size(); i++) {
            if (myTowns.get(i).getTownName().equals(townName)) {

                myTowns.remove(i);

            }
        }

    }

    public boolean searchFunc(String townName) {
        boolean value = false;

        for (int i = 0; i < myTowns.size(); i++) {
            if (myTowns.get(i).getTownName().equals(townName)) {

                value = true;
            }
        }

        return value;
    }

    public int numberOfNames(String name) {
        int number = 0;

        for (int i = 0; i < myTowns.size(); i++) {

            if (myTowns.get(i).getTownName().equals(name)) {
                number++;
            }
        }

        return number;
    }

}
