/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

/**
 *
 * @author rngod
 */
public class correlationCoefficient extends appModel {

    String categoryName;
    double correlationCoefficient;

    correlationCoefficient(String name, double data) {
        categoryName = name;
        correlationCoefficient = data;
    }

    correlationCoefficient() {
    }

    public double getCoefficient() {
        return this.correlationCoefficient;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

}
