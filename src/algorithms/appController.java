/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex
 */
public class appController {

    private ActionListener actionListener;
    private ItemListener itemListener;
    private appModel model;
    private appView view;
    private int selectedIndex;

    public appController(appModel model, appView view) {

        this.model = model;
        this.view = view;

        newTown();
        btnInsert();
        townListener();
        btnNew();
        btnClear();
        btnDelete();
        delTown();
        btnCompare();
        btnPredict();
        btnOrderR();
        updateTable();
    }

    appController() {

    }

    //Action Listeners 
    private void btnNew() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!townExists()) {
                     errorMessage("Town not set","Please create a town first!");
                } else {

                    DefaultTableModel tempMod = model.getSelectedTownModel();
                    tempMod.addRow(model.addNewTableRow());
                    tempMod.fireTableDataChanged();

                }
            }

        };

        view.getInsertBtn().addActionListener(actionListener);

    }

    private void townListener() {
        itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (!townExists()) {
                     errorMessage("Town not set","Please create a town first!");
                } else {
                    if (e.getSource() == view.getTowns()) {
                        DefaultTableModel tab = model.getSpecificTown(view.getTowns().getSelectedItem().toString());
                        model.setSelectedTownModel(tab);
                        view.getTable().setModel(tab);
                        updateSecondaryTables();
                        view.getComboBox().setSelectedIndex(selectedIndex);
                        initFX(view.getGraphPanel(), 0, false);
                        propMenu();

                    }

                }

            }

        };

        view.getTowns().addItemListener(itemListener);

    }

    private void propMenu() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 view.xToPredict("Number of " + view.getComboBox().getSelectedItem().toString());
                if (!townExists()) {
                     errorMessage("Town not set","Please create a town first!");
                } else {
                    if (view.getTable().getModel().getRowCount() < 2) {

                    } else {

                        if (view.getTable().getModel().getRowCount() >= 2) {
                            try {
                                int i = view.getComboBox().getSelectedIndex();
                                selectedIndex = i;
                                model.intiSumTable(i, (DefaultTableModel) view.getTable().getModel());
                                initFX(view.getGraphPanel(), 0, false);
                            } catch (Exception x) {
                                errorMessage("Invalid Data", "Invalid Data");
                            }
                        } else {

                            updateSecondaryTables();
                           
                        }
                    }
                }

            }
        };
        view.getComboBox().addActionListener(actionListener);
    }

    private void btnPredict() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!townExists()) {
                    errorMessage("Town not set","Please create a town first!");
                } else {
                    if (view.getTable().getModel().getRowCount() > 0) {
                        String valueString = view.getValueToPredict();

                        try {
                            double value = Double.parseDouble(valueString);
                            initFX(view.getGraphPanel(), value, false);
                        } catch (Exception p) {
                            errorMessage("Invalid Input", "Please enter a correct value.");
                        }

                    } else {
                        errorMessage("Empty Table", "You need to add some values to the table first");
                    }
                }

            }
        };
        view.getPredButton().addActionListener(actionListener);
    }

    private void btnCompare() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!townExists()) {
                    errorMessage("Town not set","Please create a town first!");
                } else {

                    model.getCorrelationCoefficientTable().setRowCount(0);
                    model.displayR((DefaultTableModel) view.getTable().getModel());
                    view.getComboBox().setSelectedIndex(findIndex());
                    initFX(view.getGraphPanel(), 0, true);
                }
            }
        };
        view.getCompare().addActionListener(actionListener);
    }

    private void btnOrderR() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(townExists())
                {
                model.getCorrelationCoefficientTable().setRowCount(0);
                model.displayR((DefaultTableModel) view.getTable().getModel());
                }else 
                {
                errorMessage("Town not set","Please create a town first!");
                }
                       
            }
        };
        view.getOrderR().addActionListener(actionListener);
    }

    private void newTown() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String m = JOptionPane.showInputDialog("Enter Town Name", null);
                try {
                    if (!m.isEmpty()) {

                        int number = model.numberOfNames(m);

                        if (number >= 1) {
                            errorMessage("Town Name", "Town Name Exists");
                        } else {

                            model.createNewTown(m);
                            view.setTownCopy(m);
                            view.setTown(m);
                            view.getTowns().setSelectedIndex(0);

                        }

                    } else {
                        errorMessage("Town Name","Town name cannot be empty.");
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

        };
        view.getNewTown().addActionListener(actionListener);
    }

    private void btnInsert() {
        actionListener = new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                if (!townExists()) {
                   errorMessage("Town not set","Please create a town first!");
                } else {
                    try {
                        model.readTextFile(view.getTowns().getSelectedItem().toString());

                        String townName = view.getTowns().getSelectedItem().toString();

                        DefaultTableModel tab = model.getSpecificTown(townName);
                        view.getTable().setModel(tab);
                        view.getTable().validate();
                        view.getComboBox().setSelectedIndex(0);
                        view.getComboBox().setSelectedIndex(selectedIndex);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(appController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        };
        view.getFileImportBtn().addActionListener(actionListener);
    }

    private void btnDelete() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tableHasData(view.getTable())) {
                    errorMessage("Table is empty", "Empty Table");
                } else {
                    DefaultTableModel dataModel = model.getSpecificTown(view.getTowns().getSelectedItem().toString());
                    int[] row = view.getTable().getSelectedRows();
                    for (int i = 0; i < row.length; i++) {
                        dataModel.removeRow(view.getTable().getSelectedRow());
                    }
                    int temp = view.getComboBox().getSelectedIndex();
                    view.getComboBox().setSelectedIndex(temp);
                }
            }
        };
        view.getRemoveBtn().addActionListener(actionListener);
    }

    private void updateTable() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!townExists()) {
                   errorMessage("Town not set","Please create a town first!");
                } else {
                    if (!(view.getTable().getRowCount() == 0)) {
                        try {
                            int i = view.getComboBox().getSelectedIndex();

                            selectedIndex = i;

                            initFX(view.getGraphPanel(), 0, false);
                        } catch (Exception x) {
                            errorMessage("Invalid Data", "Invalid Data");
                        }
                    }
                }
            }
        };
        view.getUpdateBtn().addActionListener(actionListener);
    }

    private void delTown() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getTownList().size() <= 1) {
                    errorMessage("Last Town", "You cannot delete the last town");
                } else {
                    int itemIndex = view.getTowns().getSelectedIndex();
                    String townName = view.getTowns().getSelectedItem().toString();
                    view.getTowns().removeItemAt(itemIndex);
                    view.getTownsCopy().removeItemAt(itemIndex);
                    model.deleteTown(townName);

                    view.getComboBox().setSelectedIndex(0);

                    townList();

                }
            }
        };

        view.getDelTown().addActionListener(actionListener);
    }

    private void btnClear() {
        actionListener = new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                if (!(view.getTable().getRowCount() == 0)) {
                    model.getSelectedTownModel().setRowCount(0);

                } else {
                    errorMessage("Table Error", "The table is empty");
                }

            }

        };

        view.getClearBtn().addActionListener(actionListener);

    }

    private void townList() {
        actionListener = new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                String townName = view.getTowns().getSelectedItem().toString();
                if (view.getTowns().getItemCount() == 0) {

                } else if (view.getTowns().getItemCount() > 0 && model.getSpecificTown(townName) != null) {

                } else {

                }
            }
        ;
        };
      view.getTowns().addActionListener(actionListener);
    }

    //Load data from model to the view 
    private void sumTableInit() {
        model.dataSumTable(view.getDataSumTable());
    }

    private void linearRegTableInit() {
        model.linearRegTable(view.getlinearRegTable());
    }

    private void linearSumTableInit() {
        model.linearSumTable(view.getlinearSumTable());
    }

    private void errorTableInit() {
        model.errorTable(view.getErrorTable());
    }

    private void comboBoxinit() {
        model.menuBox(view.getComboBox());
    }

    private void townsComboBox() {
        model.townsComboBox(view.getTowns());
    }

    private void initCorrelationCoef() {
        model.correlationCoefficient(view.getCorrelationCoefficient());

    }

    private void errotTableInit() {

        model.errorTable(view.getErrorTable());

    }

    //Graph Setup
    private void initFX(JFXPanel fxPanel, double value, boolean val) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene(value, val);

        fxPanel.setScene(scene);
    }

    private double getGraphMaxValue(int x) {
        //here we set the max value of the graph instead of hardcoding
        double maxValueX = 0;
        double comparedValue;

        for (int i = 0; i < model.getSelectedTownModel().getRowCount(); i++) {
            String value = (String) model.getSelectedTownModel().getValueAt(i, x);
            comparedValue = Double.parseDouble(value);
            if (comparedValue >= maxValueX) {
                maxValueX = comparedValue;
            }

        }
        return maxValueX;
    }

    private Scene createScene(double value, boolean compare) {

        //Defining the axes
        int selectedValue = selectedIndex;
        double maxValueX = getGraphMaxValue(selectedValue);
        DefaultTableModel tempMod = model.getSelectedTownModel();
        NumberAxis xAxis = new NumberAxis();

        xAxis.setAutoRanging(true);
        xAxis.setLabel(view.getComboBox().getSelectedItem().toString());

        NumberAxis yAxis = new NumberAxis();

        yAxis.setAutoRanging(true);
        yAxis.setLabel(view.getComboBox().getItemAt(0).toString());
        LineChart<String, Number> lineChart = new LineChart(xAxis, yAxis);
        XYChart.Series ForecastedValues = new XYChart.Series();
         ForecastedValues.setName("Forecasted Values");
        XYChart.Series XYValues = new XYChart.Series();
        XYValues.setName(view.getComboBox().getSelectedItem().toString() + "Values");

        XYChart.Series line2 = new XYChart.Series();
        line2.setName("Regression Line");

        XYChart.Series line3 = new XYChart.Series();
        line3.setName("Forecasted Values");

        XYChart.Series estimatedVal = new XYChart.Series();
        estimatedVal.setName(view.getComboBox().getSelectedItem().toString() + " Estimated Values");

        XYChart.Series predictedVal = new XYChart.Series();
        predictedVal.setName(view.getComboBox().getSelectedItem().toString() + " Predicted Values");

        XYChart.Series comparedTowns = new XYChart.Series();
        comparedTowns.setName(view.getTownsCopy().getSelectedItem().toString() + " Town Values");

        int selectedCol;
        selectedCol = view.getComboBox().getSelectedIndex();
        for (int i = 0; i < tempMod.getRowCount(); i++) {

            double yVal = Double.parseDouble((String) tempMod.getValueAt(i, selectedCol));
            double xVal = Double.parseDouble((String) tempMod.getValueAt(i, 0));

            XYValues.getData().add(new XYChart.Data(yVal, xVal));

        }
        // Make the linear regresion line longer
        double val1 = model.calLinearRegression(view.getComboBox().getSelectedIndex(), 0.00, tempMod);
        if ((!view.getValueToPredict().isEmpty())) {
            maxValueX = maxValueX + Double.parseDouble(view.getValueToPredict());
        } else {
            maxValueX = maxValueX + 2;
        }
        double val2 = model.calLinearRegression(view.getComboBox().getSelectedIndex(), maxValueX, tempMod);

        int rowNumber = tempMod.getRowCount();
        double errorSum = 0;
        for (int i = 0; i < rowNumber; i++) {
            int index = view.getComboBox().getSelectedIndex();
            String values = (String) tempMod.getValueAt(i, index);
            double yValues = Double.parseDouble((String) tempMod.getValueAt(i, 0));
            double rowVal = Double.parseDouble(values);
            double forecasted = model.calLinearRegression(index, rowVal, tempMod);
            estimatedVal.getData().add(new XYChart.Data(rowVal, forecasted));
            double difference = forecasted - yValues;

            errorSum = errorSum + (difference * difference);

        }
        

        line2.getData().add(new XYChart.Data(0, val1));
        line2.getData().add(new XYChart.Data(maxValueX, val2));

        if (value == 0) {
        } else {
            double yValue = model.calcEstimate(view.getComboBox().getSelectedIndex(), value, tempMod);
            predictedVal.getData().add(new XYChart.Data(value, yValue));
            view.setPredValue(model.roundDec(yValue));
        }

        if (compare == true) {
           
            line3.setName("Regression Line");
            String name;
            DefaultTableModel townToCompare;
            double[] x;
            double[] y;
           
           
            model.getErrorTableModel().setRowCount(0);
           
            for (int i = 0; i < model.getTowns().size(); i++) {

                if (view.getTownToCompare().equals(model.myTowns.get(i).getTownName())) {

                    townToCompare = model.myTowns.get(i).getTableData();
                    name = model.testingTheR(tempMod);
                     double Syy =model.calSyy(0, townToCompare);
                    for (int j = 0; j < townToCompare.getColumnCount(); j++) {
                        if (townToCompare.getColumnName(j).equals(name)) {

                            x = model.getColData(j, townToCompare);
                            y = model.getColData(0, townToCompare);
                            
                            for (int k = 0; k < x.length; k++) {
                                comparedTowns.getData().add(new XYChart.Data(x[k], y[k]));
                                double forecasted = model.calLinearRegression(j, x[k], townToCompare);
                                 ForecastedValues.getData().add(new XYChart.Data(x[k], forecasted));
                                 double errorY = Math.pow(y[k] - forecasted,2);
                                 errorY = Math.sqrt(errorY);
                                 double greekS = Math.sqrt( Syy / townToCompare.getRowCount());
                                 model.displayError(x[k], y[k],  model.roundDec(forecasted), model.roundDec(errorY));
                                
                            }
                            
                           
                            
                        }

                    }
                }
            }
        }

        lineChart.setTitle("House Pricing Graph"); //title
        lineChart.getData().addAll(XYValues, line2, estimatedVal, predictedVal, comparedTowns,ForecastedValues);
        lineChart.setPrefSize(view.getGraphPanel().getWidth(), view.getGraphPanel().getHeight());
        Group root = new Group(lineChart);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/CSS/GraphCSS.css");

        return (scene);

    }

    // Utillity Function
    private boolean townExists() {
        return view.getTowns().getItemCount() != 0;

    }

    private void errorMessage(String errorTitle, String errorMessage) {

        JOptionPane.showMessageDialog(null,
                errorMessage,
                errorTitle,
                JOptionPane.WARNING_MESSAGE);

    }

    private boolean tableHasData(JTable table) {
        return table.getModel().getRowCount() != 0;

    }

    private void updateSecondaryTables() {

        sumTableInit();
        sumTableInit2();
        linearRegTableInit();
        initCorrelationCoef();
        errorTableInit();
        comboBoxinit();
    }

    private void sumTableInit2() {
        model.dataSumTable2(view.getDataSumTable2());
    }

    
    private int findIndex() {

        int bestCorrelationValue = model.getCorrelationCoefficientTable().getRowCount() - 2;
        System.out.println(bestCorrelationValue);
        String correlationValueName = model.getCorrelationCoefficientTable().getValueAt(bestCorrelationValue, 0).toString();
        System.out.println(correlationValueName);
        for (int i = 0; i < view.getComboBox().getItemCount(); i++) {
            if (correlationValueName.equals(view.getComboBox().getItemAt(i).toString())) {

                return i;
            }

        }

        return 0;
    }
}
