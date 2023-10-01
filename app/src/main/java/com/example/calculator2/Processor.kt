package com.example.calculator2

import android.widget.Toast
import java.util.*

/**
 * This class is responsible for keeping track of the current state of the input (e.g. are there commas?, are there parentheses?, did an operator occur?) and also does the handling of it.
 * It is also doing all the calculations when the result button is pressed.
 * */
class Processor {
    private var input = StringBuilder() // current total input

    // various state flags to check various conditions during the input parsing
    private var opened = 0 //counter for "("
    private var closed = 0 //counter for ")"
    private var countcomma = 0 // Keeps track of "." if some of them get deleted.
    private var currentcomma:Boolean = false //  Is there a "." in current expression?
    private var allcomma:Boolean = false // Is there a "." at all?
    private var operator:Boolean = false // Was previous entry an operator?
    private var discontinue:Boolean = false // checks if further 0 input is allowed
    private var negative:Boolean = false // is the current index to be negative
    private var notazero = StringBuilder() //  checks if pre decimal is zero only. important for delete()-method afterwards!

    /**
     * Sets everything back to the initial state
     * */
    fun reset() {
        opened = 0
        closed = 0
        countcomma = 0
        currentcomma = false
        allcomma = false
        operator = false
        discontinue = false
        notazero.setLength(0)
        input.setLength(0)
        input.append("")
        negative = false
    }

    /**
     * Deletes the latest input digit and updates relevant states accordingly
     * */
    fun delete() {

        if (input.length <= 0) return

        val current = input[input.length - 1]
        input.deleteCharAt(input.length - 1)
        val wasOperator:Boolean = (current == '+' || current == '-' || current == '*' || current == '/')

        if (wasOperator && operator) { // Check conditions after delete.
            operator = false
            return
        }

        // check what was deleted and update corresponding states
        when(current) {

            '(' -> if (opened > 0) {
                opened--

                if (input.length >= 2) {
                    val opcheck2 = input[input.length - 2]
                    val wasOperator2:Boolean = (opcheck2 == '+' || opcheck2 == '-' || opcheck2 == '*' || opcheck2 == '/')

                    val opcheck1 = input[input.length - 1]
                    val wasOperator1:Boolean = (opcheck1 == '+' || opcheck1 == '-' || opcheck1 == '*' || opcheck1 == '/')

                    if (wasOperator2 && opcheck1 == '-') negative = true  // case:  +-( -> delete

                    if (wasOperator1 && !wasOperator2) operator = true // case: +( -> delete


                }

                if (input.length == 1) {
                    val opcheck = input[input.length - 1]
                    if (opcheck == '-') negative = true
                }

            }

            ')' -> if (closed > 0) {
                closed--

            }

            '.' -> {
                currentcomma = false
                --countcomma

                if (countcomma <= 0) allcomma = false

                // Checks the pre-comma value

                val check = false
                // StringBuilder predecimal = new StringBuilder();
                val predecimal: Stack<Char?> = Stack()

                for (i in input.length - 1 downTo 0) {

                    val opcheck = input[i]
                    val nonnumerical:Boolean = (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/' || opcheck == '(')

                    if (nonnumerical) break //if(opcheck!=')')
                    else predecimal.push(opcheck)
                }

                if (predecimal.size > 1) {
                    discontinue = false
                } // pre decimal indexes > 1, zB. "10" / "56" -> further zeros allowed

                else {
                    if (predecimal.pop() == '0') discontinue = true // unit position is "0" -> further zeros not allowed
                    else discontinue = false // unit position != "0" -> further zeros allowed
                }
            }

            '-' -> {

                val opcheck1 = input[input.length - 1]
                val wasOperator1:Boolean = (opcheck1 == '+' || opcheck1 == '-' || opcheck1 == '*' || opcheck1 == '/')

                if (input.length >= 2) {
                    val opcheck2 = input[input.length - 2]
                    val wasParentheseOpen:Boolean = (opcheck2 == '(')

                    if (wasParentheseOpen) {
                        operator = false
                        negative = false

                    }

                }

                if (wasOperator1) {
                    negative = false
                    operator = true
                }
                else {
                    negative = false
                    operator = false
                }

                /*
                if (input.length == 1) {
                    val opcheck = input[input.length - 1]
                    if (opcheck == '-') negative = true
                }*/

            }

            // Infinity
            'y' -> {
                reset()
                return
            }

            '+','/','*' -> {}

            else -> {

                // TODO: check for operator or negative !!!
                if (allcomma) {
                    currentcomma = false // assumption: current expression not a decimal number with comma

                    for (i in input.length - 1 downTo 0) { // check if we have an integer or real number
                        val opcheck = input[i]
                        val nonnumerical:Boolean = (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/' || opcheck == '(')

                        if (nonnumerical) break // current operand read -> break out of loop
                        if (opcheck == '.') currentcomma = true
                    }
                }

                // if deletion leads back to an operator index -> operator = true
                if (input.length >= 2) {
                    val opcheck2 = input[input.length - 2]
                    val wasOperator2:Boolean = (opcheck2 == '+' || opcheck2 == '-' || opcheck2 == '*' || opcheck2 == '/')
                    val wasParentheseOpen:Boolean = (opcheck2 == '(')

                    val opcheck1 = input[input.length - 1]
                    val wasOperator1:Boolean = (opcheck1 == '+' || opcheck1 == '-' || opcheck1 == '*' || opcheck1 == '/')

                    if (wasOperator2 && opcheck1 == '-') { // case:  +-( -> delete
                        negative = true
                        operator = false
                    }

                    if (wasOperator1 && !wasOperator2) operator = true // case: +( -> delete


                }

                if (input.length == 1) {
                    val opcheck = input[input.length - 1]
                    if (opcheck == '-') negative = true
                    if (opcheck == '(') {
                        negative = false
                        operator = false
                    }
                }
            }
        }
    }

    /**
     * Inserts the ( - parenthese into the input with respective checks, updates relevant states.
     * */
    fun openingP() { // TODO: check for negative
        if (input.length > 0) { // Checks entries before "("
            val current = input[input.length - 1]
            val openupAllowed:Boolean = (current == '(' || operator || negative)

            if (openupAllowed) {
                input.append('(')
                opened++ // "(" added -> increment
                negative = false
            }

        } else { //1st index
            if (input.length <= 0) { // if input.length <= 0
                input.append('(')
                opened++
            }
        }

        operator = false
    }


    /**
     * Inserts the ) - parenthese into the input with respective checks, updates relevant states.
     * */
    fun closingP() { //TODO: check for negative
        if (opened > 0 && opened != closed) {
            val current = input[input.length - 1]
            val closeAllowed:Boolean = (!operator && current != '(')

            // Only close ")" without operator or "(" directly in front.
            if (closeAllowed) {
                input.append(")")
                closed++
            }
        }
        operator = false
    }

    fun processzero() { // TODO: /0 -> /00

        if (input.length > 0) {

            // TODO: overwriting infinity with 0
            if (input.contains("Infinity") && !operator) {
                reset()
                input.append('0')
                return
            }

            //input.append('0')
            val current = input[input.length - 1]


            if (!currentcomma && !discontinue) {
                if (operator || current == '(') { // e.g. "0" input @ "5+" -> "5+0" or "0" input @ "5+(" -> "5+(0"
                    input.append('0')
                    discontinue = true
                } else {
                    if (current == ')') discontinue = true
                    // after a non-zero digit e.g."100" or "545000"
                }
            }
        } else {
            input.append('0')
            discontinue = true
        }

        if (!discontinue) input.append('0')

        operator = false // x + 0
        negative = false
    }

    fun inputprocess(number:Char) {

        // TODO: (0.0) -> 0 -> (0.0)0
        // TODO: (0.)
        if (input.length > 0) {

            val current = input[input.length - 1]
            val termIsOnlyAZero = (current == '0' && discontinue)

            if (termIsOnlyAZero) {
                input.deleteCharAt(input.length - 1) // replace 0 with input
                discontinue = false
            }

            if (input.contains("Infinity") && !operator) reset()

            input.append(number)
        }
        else {
            input.append(number)
        }

        if (!currentcomma) discontinue = false

        operator = false
        negative = false
    }

    fun plus() { // TODO: handling negative
        if (input.length <= 0 || negative) return


        // replaces existing operator with this one
        if (operator) {
            input.deleteCharAt(input.length - 1)
            input.append('+')

            // operator = false;
        } else {
            val previous = input[input.length - 1]

            if (previous == '.') input.append(0) // autofills missing input e.g. "0.+" -> "0.0+"

            if (previous != '(') {
                input.append('+')
                currentcomma = false
                discontinue = false

            } else return
        }
        operator = true
        negative = false
    }

    fun minus() {
        if (negative) return

        if (input.length <= 0) {
            input.append('-')
            return
        }

        val current = input[input.length - 1]
        if (operator) {

            //input.deleteCharAt(input.length - 1)

            if (!negative) {
                input.append('-')
                negative = true
                operator = false
            }
            else return // e.g. +- -> another - input should do nothing

            /*
            when (current) {

                '+','-' -> {
                    input.deleteCharAt(input.length - 1)
                    input.append('-')
                }
                '*','/' -> {
                    input.append('-')
                    negative = true
                }
            }
            // TODO: set a negative flag

             */

        } else { // number, . , ( , )
            val previous = input[input.length - 1]

            if (previous == '(') {
                input.append('-')
                negative = true
                return
            }

            if (previous == '.') input.append(0)

            input.append('-')
            operator = true
            currentcomma = false
            discontinue = false
            negative = false // TODO: what if 2*-(5-3) ???
        }
    }

    fun multiply() {
        if (input.length <= 0 || negative) return

        if (operator) {
            input.deleteCharAt(input.length - 1)
            input.append('*')
            // operator = false;
        } else {
            val previous = input[input.length - 1]

            if (previous == '.') input.append(0)

            if (previous != '(') {
                input.append('*')
                operator = true
                currentcomma = false
                discontinue = false
            }
            else return
        }

        operator = true
        negative = false
    }

    fun divide() { // TODO: check with github version
        if (input.length <= 0 || negative) return

        if (operator) {
            input.deleteCharAt(input.length - 1)
            input.append('/')
            // operator = false;
        } else {
            val previous = input[input.length - 1]

            if (previous == '.') input.append(0)

            if (previous != '(') {
                input.append('/')
                operator = true
                currentcomma = false
                discontinue = false
            }
            else return
        }

        operator = true
        negative = false
    }

    fun comma() {
        if (input.length > 0) {
            val test = input[input.length - 1]

            if (test != '.' && !currentcomma) {

                //  Was "." already entered before?

                // Insert missing digit in advance e.g. "1+." -> "1+0."

                if (test == '+' || test == '-' || test == '/' || test == '*' || test == '(' || test == ')') input.append(0)

                input.append('.')
                currentcomma = true
                allcomma = true
                countcomma++
                discontinue = false
            }
        } else { // "." -> "0."
            input.append(0).append('.')
            currentcomma = true
            allcomma = true
            countcomma++
            discontinue = false
        }

        operator = false //  entry was no operator, ergo false.


        return
    }


    fun getInput():String {
        return input.toString()
    }

    fun addInput(raw:String) {
        input.append(raw)
    }

    /**
     * Checks the term of current index for length before the input.
     * We want the numbers to have limited digits, because data types have limited value range.
     * In this case each term will have a limit of x = 14 + 1 (because we add one digit on input).
     *
     * @return length from 1 to x, if it is bigger we just output y = 15
     *
     * **/
    fun checkTermlength():Byte {
        val y:Byte = 15
        var x:Byte = 0
        var i = input.length
        var reached = false

        if (i == 0) {
            reached = true
            x++
        }

        while (!reached) {
            val isOperator = (input[i-1] == '+') ||
                    (input[i-1] == '-') ||
                    (input[i-1] == '*') ||
                    (input[i-1] == '/')

            val isParentheses = (input[i-1] == '(') ||
                    (input[i-1] == ')')

            if(isOperator || isParentheses) {
                reached = true
                x-- // we don't include this sign into the term length
            }
            if(i == 1) {
                reached = true
                x++
            }
            else {
                i--
                x++
            }
        }

        if (x <= 14) return x
        else return y
    }


    /**
     * Before calculating the result - if there are negative signs involved - this method alters the input accordingly.
     * e.g. -(x+y) will be changed to -1 * (x+y)
     * */
    fun preprocess() {
        val i = input.length
        var reached = false
        var result:StringBuilder
        var counter = 0

        if (i == 0) {
            reached = true
        }

        while (!reached) {
            var previous:Char?
            if (counter >= 2) previous = input[counter-1]
            var preprevious:Char?
            if (counter >= 3) preprevious = input[counter-2]

            val isOperator = (input[counter] == '+') ||
                    (input[counter] == '-') ||
                    (input[counter] == '*') ||
                    (input[counter] == '/')

            val isNegative = (input[counter] == '-')

            val isParentheses = (input[counter] == '(') ||
                    (input[counter] == ')')

            val isDigit = !isOperator && !isParentheses

            if (isDigit) {


            }


            if(i == 1) {
                reached = true

            }
            else {

            }
        }
        return


    }

    /**
     * closes up any remaining open parentheses in the input with ")".
     * */
    fun closeup() {
        while (opened != closed) {
            input.append(")")
            closed++
        }
    }

}