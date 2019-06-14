/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javafx.embed.swing.JFXPanel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

/**
 *
 * @author Alex
 */
public class appView extends JFrame {

    private final JButton insert = new JButton("New Row");
    private final JButton remove = new JButton("Remove Row");
    private final JButton update = new JButton("Update Table");
    private final JButton clear = new JButton("Clear Table");
    private final JButton fileImport = new JButton("Import from FIle");
    private final JButton compare = new JButton("Compare Towns");
    private final JButton orderR = new JButton("Order R");
    private final JFXPanel graph = new JFXPanel();
    private final JComboBox<String> menuBox = new JComboBox<>();
    private final JComboBox<String> towns = new JComboBox<>();
    private final JComboBox<String> townsCopy = new JComboBox<>();
    private final JTable table;
    private final JTable dataSummary;
    private final JTable dataSummary2;
    private final JTable linearRegressionTable;
    private JTable linearSumTable;
    private final JTable errorTable;
    private final JTable correlationCoefficient;
    private final JButton newTown = new JButton("New Town");
    private final JButton delTown = new JButton("Delete Town");
    private final JButton btnCons = new JButton("Predict Price");
    private final JTextField valueToPredict = new JTextField(15);
    private JLabel predValue = new JLabel();
    private final JLabel townList = new JLabel("Town List:");
    private final JLabel addNum = new JLabel("Number of X ");
    {
        // FRAME SETTINGS 
        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("House Market");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        System.out.println(width + "x" + height);
        //JTABLE SETTUP
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setPreferredSize(new Dimension(width / 2, height / 2));
        table.setFillsViewportHeight(true);

        dataSummary = new JTable();

        dataSummary2 = new JTable();
        JScrollPane sumPane = new JScrollPane(dataSummary);
        JScrollPane sumPane2 = new JScrollPane(dataSummary2);

        correlationCoefficient = new JTable();
        JScrollPane corrCoefficient = new JScrollPane(correlationCoefficient);
        correlationCoefficient.setFillsViewportHeight(true);

        dataSummary.setFillsViewportHeight(true);

        dataSummary2.setFillsViewportHeight(true);

        linearRegressionTable = new JTable();
        JScrollPane linearPane = new JScrollPane(linearRegressionTable);
        linearRegressionTable.setFillsViewportHeight(true);

        errorTable = new JTable();
        JScrollPane errorPane = new JScrollPane(errorTable);
        errorTable.setFillsViewportHeight(true);

        JPanel topRight = new JPanel();

        JPanel topLeft = new JPanel();

        JPanel bottomLeft = new JPanel();

        JPanel bottomRight = new JPanel();

        // Frame Constraints 
        GridBagConstraints fCo = new GridBagConstraints();
        fCo.weightx = 1.0;
        fCo.weighty = 1.0;
        fCo.fill = GridBagConstraints.BOTH;
        fCo.gridx = 0;
        fCo.gridy = 0;
        frame.add(topLeft, fCo);
        fCo.gridx = 1;
        fCo.gridy = 0;
        frame.add(topRight, fCo);
        fCo.gridx = 0;
        fCo.gridy = 1;
        frame.add(bottomLeft, fCo);
        fCo.gridx = 1;
        fCo.gridy = 1;
        frame.add(bottomRight, fCo);
        // Setting Up TOP LEFT;
        topLeft.setLayout(new BorderLayout());
        JPanel buttonsTop = new JPanel();
        buttonsTop.setLayout(new FlowLayout());
        buttonsTop.add(townList);
        buttonsTop.add(towns);
        buttonsTop.add(newTown);
        buttonsTop.add(delTown);
        buttonsTop.add(fileImport);

        topLeft.add(buttonsTop, BorderLayout.NORTH);
        topLeft.add(scrollPane, BorderLayout.CENTER);

        // Setting up BottomLeft
        bottomLeft.setLayout(new BorderLayout());

        JPanel buttonsDown = new JPanel();
        JLabel depVar = new JLabel("Independent  Variable :");
        buttonsDown.setLayout(new FlowLayout());
        buttonsDown.add(depVar);
        buttonsDown.add(menuBox);
        buttonsDown.add(insert);
        buttonsDown.add(update);
        buttonsDown.add(clear);
        buttonsDown.add(remove);

        JPanel tables = new JPanel();
        tables.setLayout(new GridLayout(1, 3));
        tables.add(sumPane);
        
        tables.add(sumPane2);
        tables.add(linearPane);
        tables.add(errorPane);
        bottomLeft.add(buttonsDown, BorderLayout.NORTH);
        bottomLeft.add(tables, BorderLayout.CENTER);
        //Setting up Top Right
        graph.setPreferredSize(screenSize);
        topRight.setLayout(new BorderLayout());
        topRight.add(graph, BorderLayout.CENTER);
        //Setting up Bottom Right
        JPanel bottomRightBar = new JPanel();

        bottomRightBar.add(orderR);
        JLabel compTown = new JLabel("Compare town to:");
        bottomRightBar.add(compTown);
        bottomRightBar.add(townsCopy);
        bottomRightBar.add(compare);
       
        JPanel bottomRightTables = new JPanel();
        bottomRightTables.setLayout(new GridLayout(0, 2));
        bottomRightTables.add(corrCoefficient);
        JPanel bottomRightPrediction = new JPanel();
        SpringLayout layout = new SpringLayout();

        bottomRightPrediction.setLayout(layout);
        

        SpringLayout.Constraints labelCons = layout.getConstraints(addNum);
        labelCons.setX(Spring.constant(5));
        labelCons.setY(Spring.constant(5));

        SpringLayout.Constraints textFieldCons
                = layout.getConstraints(valueToPredict);
        textFieldCons.setX(Spring.sum(
                Spring.constant(0),
                labelCons.getConstraint(SpringLayout.WEST)));
        textFieldCons.setY(Spring.constant(25));

        SpringLayout.Constraints btnConstrains
                = layout.getConstraints(btnCons);
        btnConstrains.setX(Spring.sum(
                Spring.constant(0),
                labelCons.getConstraint(SpringLayout.WEST)));
        btnConstrains.setY(Spring.constant(50));

        SpringLayout.Constraints predCons
                = layout.getConstraints(predValue);
        predCons.setX(Spring.sum(
                Spring.constant(0),
                labelCons.getConstraint(SpringLayout.WEST)));
        predCons.setY(Spring.constant(75));

        bottomRightPrediction.add(addNum);
        bottomRightPrediction.add(valueToPredict);
        bottomRightPrediction.add(btnCons);
        bottomRightPrediction.add(predValue);

        bottomRightTables.add(bottomRightPrediction);
        bottomRightTables.add(errorPane);
        bottomRight.setLayout(new BorderLayout());
        bottomRight.add(bottomRightBar, BorderLayout.NORTH);
        bottomRight.add(bottomRightTables, BorderLayout.CENTER);

        frame.setBackground(Color.RED);
        frame.setVisible(true);

    }

    public JButton getInsertBtn() {
        return insert;
    }

    public JButton getRemoveBtn() {
        return remove;
    }

    public JButton getClearBtn() {
        return clear;
    }

    public JButton getUpdateBtn() {
        return update;
    }

    public JButton getFileImportBtn() {
        return fileImport;
    }

    public JButton getNewTown() {
        return newTown;
    }

    public JButton getCompare() {
        return compare;
    }

    public JButton getOrderR() {
        return orderR;
    }

    public JButton getDelTown() {
        return delTown;
    }

    public JComboBox getTowns() {
        return this.towns;
    }

    public JComboBox getTownsCopy() {
        return this.townsCopy;
    }

    public void setTown(String name) {
        this.towns.addItem(name);
    }

    public void setTownCopy(String name) {
        this.townsCopy.addItem(name);
    }

    public JTable getTable() {
        return this.table;
    }

    public JTable getDataSumTable() {
        return this.dataSummary;
    }

    public JTable getlinearRegTable() {
        return this.linearRegressionTable;
    }

    public JTable getlinearSumTable() {
        return this.linearSumTable;
    }

    public JTable getErrorTable() {
        return this.errorTable;
    }

    public JComboBox getComboBox() {
        return this.menuBox;
    }

    public JFXPanel getGraphPanel() {
        return this.graph;

    }

    public JTable getDataSumTable2() {

        return this.dataSummary2;
    }

    public JTable getCorrelationCoefficient() {
        return this.correlationCoefficient;

    }

    public JButton getPredButton() {

        return this.btnCons;
    }

    public String getValueToPredict() {

        return this.valueToPredict.getText();
    }

    public void setPredValue(double value) {
        predValue.setText("The predicted Value is :" + value);

    }

    public String getTownToCompare() {
        return this.townsCopy.getSelectedItem().toString();
    }
    public void xToPredict(String text)
    {
    addNum.setText(text);
    }

}
