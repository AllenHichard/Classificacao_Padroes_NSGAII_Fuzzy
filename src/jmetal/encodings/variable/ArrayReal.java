//  ArrayReal.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
// 
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package jmetal.encodings.variable;

import SistemaGenético.OtimizacaoRegras;
import jmetal.core.Problem;
import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * Class implementing a decision encodings.variable representing an array of
 * real values. The real values of the array have their own bounds.
 */
public class ArrayReal extends Variable {

    /**
     * Problem using the type
     */
    private Problem problem_;
    /**
     * Stores an array of real values
     */
    public Double[] array_;
    private int[] lowerBounds_ ;
    private int[] upperBounds_ ;

    /**
     * Stores the length of the array
     */
    private int size_;

    /**
     * Constructor
     */
    public ArrayReal() {
        problem_ = null;
        size_ = 0;
        array_ = null;
    } // Constructor

    /*
     Construtor semente
     */
    public ArrayReal(int tamanhoInt, Problem problem, double[] semente) { // size seria a quantidade de inteiros
        
        if (OtimizacaoRegras.codSemente) {
            System.out.println("Semeando");
            problem_ = problem;
            size_ = problem.getNumberOfVariables();
            array_ = new Double[size_];
            lowerBounds_ = new int[size_];
            upperBounds_ = new int[size_];
            for (int i = 0; i < tamanhoInt; i++) {
                lowerBounds_[i] = (int) problem_.getLowerLimit(i);
                upperBounds_[i] = (int) problem_.getUpperLimit(i);
                array_[i] = semente[i];
            }

            for (int i = tamanhoInt; i < size_; i++) {
                lowerBounds_[i] = (int) problem_.getLowerLimit(i);
                upperBounds_[i] = (int) problem_.getUpperLimit(i);
                array_[i] = semente[i];
            }
            OtimizacaoRegras.codSemente = false;
        } else {
            problem_ = problem;
            size_ = problem.getNumberOfVariables();
            array_ = new Double[size_];
            lowerBounds_ = new int[size_];
            upperBounds_ = new int[size_];

            for (int i = 0; i < tamanhoInt; i++) {
                lowerBounds_[i] = (int) problem_.getLowerLimit(i);
                upperBounds_[i] = (int) problem_.getUpperLimit(i);
                array_[i] = (double) PseudoRandom.randInt(lowerBounds_[i], upperBounds_[i]);
            }

            for (int i = tamanhoInt; i < size_; i++) {
                lowerBounds_[i] = (int) problem_.getLowerLimit(i);
                upperBounds_[i] = (int) problem_.getUpperLimit(i);
                array_[i] = PseudoRandom.randDouble() * (problem_.getUpperLimit(i)
                        - problem_.getLowerLimit(i))
                        + problem_.getLowerLimit(i);
            } // for
        }
        
    } // Constructor

    /**
     * Constructor antes da semente
     *
     * @param size Size of the array
     */
    public ArrayReal(int tamanhoInt, Problem problem) { // size seria a quantidade de inteiros
        problem_ = problem;
        size_ = problem.getNumberOfVariables();
        array_ = new Double[size_];
        lowerBounds_ = new int[size_];
        upperBounds_ = new int[size_];

        for (int i = 0; i < tamanhoInt; i++) {
            lowerBounds_[i] = (int) problem_.getLowerLimit(i);
            upperBounds_[i] = (int) problem_.getUpperLimit(i);
            array_[i] = (double) PseudoRandom.randInt(lowerBounds_[i], upperBounds_[i]);
        }

        for (int i = tamanhoInt; i < size_; i++) {
            array_[i] = PseudoRandom.randDouble() * (problem_.getUpperLimit(i)
                    - problem_.getLowerLimit(i))
                    + problem_.getLowerLimit(i);
        } // for

    } // Constructor

    public void redefinindoIntervalo(int[] novoVetorInt) {

        problem_.setLowerUpLimit(novoVetorInt);
        lowerBounds_ = new int[problem_.getNumberOfVariables()];
        upperBounds_ = new int[problem_.getNumberOfVariables()];
        for (int i = 0; i < novoVetorInt.length; i++) {
            //if (lowerBounds_ != null) {
                lowerBounds_[i] = (int) problem_.getLowerLimit(i);
                upperBounds_[i] = (int) problem_.getUpperLimit(i);
                array_[i] = (double) novoVetorInt[i];
            //}
        }
        // for
    }

    /**
     * Copy Constructor
     *
     * @param arrayReal The arrayReal to copy
     */
    private ArrayReal(ArrayReal arrayReal) {
        problem_ = arrayReal.problem_;
        size_ = arrayReal.size_;
        array_ = new Double[size_];

        System.arraycopy(arrayReal.array_, 0, array_, 0, size_);
    } // Copy Constructor

    @Override
    public Variable deepCopy() {
        return new ArrayReal(this);
    } // deepCopy

    /**
     * Returns the length of the arrayReal.
     *
     * @return The length
     */
    public int getLength() {
        return size_;
    } // getLength

    /**
     * getValue
     *
     * @param index Index of value to be returned
     * @return the value in position index
     */
    public double getValue(int index) throws JMException {
        if ((index >= 0) && (index < size_)) {
            return array_[index];
        } else {
            Configuration.logger_.severe(jmetal.encodings.variable.ArrayReal.class + ".getValue(): index value (" + index + ") invalid");
            throw new JMException(jmetal.encodings.variable.ArrayReal.class + ".ArrayReal: index value (" + index + ") invalid");
        } // if
    } // getValue

    /**
     * setValue
     *
     * @param index Index of value to be returned
     * @param value The value to be set in position index
     */
    public void setValue(int index, double value) throws JMException {
        if ((index >= 0) && (index < size_)) {
            array_[index] = value;
        } else {
            Configuration.logger_.severe(jmetal.encodings.variable.ArrayReal.class + ".setValue(): index value (" + index + ") invalid");
            throw new JMException(jmetal.encodings.variable.ArrayReal.class + ": index value (" + index + ") invalid");
        } // else
    } // setValue

    /**
     * Get the lower bound of a value
     *
     * @param index The index of the value
     * @return the lower bound
     */
    public double getLowerBound(int index) throws JMException {
        if ((index >= 0) && (index < size_)) {
            return problem_.getLowerLimit(index);
        } else {
            Configuration.logger_.severe(jmetal.encodings.variable.ArrayReal.class + ".getLowerBound(): index value (" + index + ") invalid");
            throw new JMException(jmetal.encodings.variable.ArrayReal.class + ".getLowerBound: index value (" + index + ") invalid");
        } // else
    } // getLowerBound

    /**
     * Get the upper bound of a value
     *
     * @param index The index of the value
     * @return the upper bound
     */
    public double getUpperBound(int index) throws JMException {
        if ((index >= 0) && (index < size_)) {
            return problem_.getUpperLimit(index);
        } else {
            Configuration.logger_.severe(jmetal.encodings.variable.ArrayReal.class + ".getUpperBound(): index value (" + index + ") invalid");
            throw new JMException(jmetal.encodings.variable.ArrayReal.class + ".getUpperBound: index value (" + index + ") invalid");
        } // else
    } // getLowerBound

    /**
     * Returns a string representing the object
     *
     * @return The string
     */
    public String toString() {
        String string;

        string = "";
        for (int i = 0; i < (size_ - 1); i++) {
            string += array_[i] + " ";
        }

        string += array_[size_ - 1];
        return string;
    } // toString
} // ArrayReal
