package com.example.calculator2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    /*
     * counter for "(" & ")"
     * */
    private var opened = 0
    private var closed = 0

    /*Zaehlt alle "." wichtig falls geloescht wird.
    * Keeps track of "." if some of them get deleted.
    */
    private var countcomma = 0

    /*Ist ein "." im aktuellen Ausdruck?
    *  Is there a "." in current expression?
    */
    private var currentcomma:Boolean = false

    /*Kommt generell ein "." vor?
     *  Is there a "." at all?
     */
    private var allcomma:Boolean = false

    /* War davor ein Operator?
     *   Was previous entry an operator?
     * */
    private var operator:Boolean = false

    /* Setzt Bedingung fest, ob eine weiter 0 Eingabe legitim ist.
   *   checks if further 0 input is allowed
   * */
    private var discontinue:Boolean = false

    /* Ist Vorkommastelle nur 0? wichtig bei delete()-Methode!
    *   checks if pre decimal is zero only. important for delete()-method afterwards!
    * */
    private var notazero = StringBuilder()

    private var input = StringBuilder()

    var result: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       result = findViewById<TextView>(R.id.textView) as TextView


    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_land)
            result= findViewById(R.id.textView) as TextView
            if(result!=null)
                result!!.setText(input)

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_main)
            result= findViewById(R.id.textView)  as TextView
            if(result!=null)
                result!!.setText(input)
        }
    }

    fun delete_all(view: View) {
        input.delete(0, input.length)
        if(result!=null)
            result!!.setText(input)
        opened = 0.also { closed = it }
        currentcomma = false.also { allcomma = it }
        countcomma = 0
        operator = false
        discontinue = false
        notazero.delete(0, notazero.length)

    }

    fun delete(view: View) {

        if (input.length != 0) {
            val current = input[input.length - 1]

            input.deleteCharAt(input.length - 1)
            result!!.text = input

                /* Ueberpruefung nach dem Loeschen.
                 *   Check conditions after delete.
                 */
                if ((current == '+' || current == '-' || current == '*' || current == '/') && operator)
                    operator = false
                else {
                    if (current == '(') {
                        if (opened > 0) opened--
                }
                if (current == ')') if (closed > 0) closed--
                if (current == '.') {
                    currentcomma = false
                    --countcomma
                    if (countcomma <= 0) allcomma = false
                    /*Da eine Vorkommastelle existiert, wird diese geprueft
                     *   Checks the pre-comma value
                     * */
                    val check = false
                    // StringBuilder predecimal = new StringBuilder();
                    val predecimal: Stack<Char?> = Stack()
                    for (i in input.length - 1 downTo 0) {
                        val opcheck = input[i]
                        if (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/' || opcheck == '(') { //Abbruchbedingung
                            break
                        } else { //if(opcheck!=')')
                            predecimal.push(opcheck)
                        }
                    }

                        if (predecimal.size > 1) //Vorkommastellen > 1, zB. "10" / "56" -> weitere Nullen erlaubt
                            discontinue = false
                        else {
                            if (predecimal.pop() == '0') {
                                discontinue = true //Einerstelle ist "0" -> weitere Nullen nicht erlaubt
                            } else discontinue = false //Einerstelle != "0" -> weitere Nullen erlaubt
                        }
                } else {


                    if(allcomma) {

                        currentcomma = false // Annahme, dass aktueller Ausdruck keine Kommazahl ist


                        for (i in input.length - 1 downTo 0) {
                            val opcheck = input[i]

                            if (opcheck == '.')
                                currentcomma = true

                            if (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/' || opcheck == '(')// aktueller Operand abgelesen -> Abbruchbedingung
                                break

                        }
                    }

                    /* Wenn nach dem Loeschen der aktuelle Index ein Operator ist.
                    *   if deletion leads back to an operator index -> operator = true
                    * */

                    if (input.length >= 2) {
                        val opcheck = input[input.length - 1]
                        if (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/') {
                            operator = true
                        }
                    }

               }
            }
            result!!.text = input
        }

    }

    fun Klammer_auf(view: View) {
         /* Falls etwas vor "(" steht.
         *   Checks entries before "("
         * */
        if (input.length > 0) {
            val current = input[input.length - 1]
            /* "(" nur unter bestimmten Bedingungen einfuegen.
             *   Only add "(" under these conditions.
             * */
            if (current == '(' || operator) {
                input.append('(')

                opened++ // "(" added -> increment
                operator = false

                if(result!=null)
                    result!!.setText(input)
            }
        } else { //1st index
            if (input.length == 0) {
                input.append('(')

                opened++

                if(result!=null)
                    result!!.setText(input)
            }
        }

    }
    fun Klammer_zu(view: View) {

         /* Steht vor ")" ein offener Ausdruck?
         *   Is there an unclosed expression?
         * */
        if (opened > 0 && opened != closed) {
            val current = input[input.length - 1]
            /* ")" nur zulassen, falls kein Operator oder "(" direkt davor.
             *   Only close ")" without operator or "(" directly in front.
             * */if (!operator && current != '(') {
                input.append(")")

                closed++

                if(result!=null)
                    result!!.setText(input)
            }
        }

    }

    fun click_zero(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (currentcomma) {
                input.append('0')
                if(result!=null)
                    result!!.setText(input)
            } else {
                if (!discontinue) {
                    if (operator) { // e.g. "0" input @ "5+" -> "5+0"
                        input.append('0')
                        if(result!=null)
                            result!!.setText(input)


                        discontinue = true
                    } else {
                        if (current == '(') { // e.g. "0" input @ "5+(" -> "5+(0"
                            input.append('0')

                            if(result!=null)
                                result!!.setText(input)

                            discontinue = true
                        } else { // after a non-zero digit e.g."100" or "545000"
                            input.append('0')
                            if(result!=null)
                                result!!.setText(input)
                        }
                    }
                }
            }
        } else {
            input.append('0')
            if(result!=null)
                result!!.setText(input)
            discontinue = true
        }

        if (operator) operator = false

    }

    fun click_one(view: View) {

        /* checkt, ob aktueller Index einstellige "0" ist. Falls ja -> mit aktueller Eingabe ersetzen, ansonsten einfach dazugeben.
        *    Is current index a "0"? if so -> replace with new given input, else just add it to the String.
        * */
        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('1')
            } else input.append('1')
        } else input.append('1')


        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false


    }

    fun click_two(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('2')
            } else input.append('2')
        } else input.append('2')


        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun click_three(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('3')
            } else input.append('3')
        } else input.append('3')



        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun click_four(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('4')
            } else input.append('4')
        } else input.append('4')



        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun click_five(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('5')
            } else input.append('5')
        } else input.append('5')



        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun click_six(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('6')
            } else input.append('6')
        } else input.append('6')




        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun click_seven(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('7')
            } else input.append('7')
        } else input.append('7')



        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun click_eight(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('8')
            } else input.append('8')
        } else input.append('8')



        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun click_nine(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (current == '0' && discontinue) {
                input.deleteCharAt(input.length - 1)
                discontinue = false
                input.append('9')
            } else input.append('9')
        } else input.append('9')



        if(result!=null)
            result!!.setText(input)

        if (!currentcomma) {
            discontinue = false
        }

        if (operator) operator = false

    }

    fun multiply(view: View) {

        if (input.length > 0) {
            if (operator) {
                input.deleteCharAt(input.length - 1)
                input.append('*')
                if(result!=null)
                    result!!.setText(input)
                // operator = false;
            } else {
                if (input[input.length - 1] == '.') {
                    input.append(0)
                }
                input.append('*')

                if(result!=null)
                    result!!.setText(input)

                operator = true
                currentcomma = false
                discontinue = false
            }
        }

    }

    fun plus(view: View) {

        if (input.length > 0) {

            /* bestehenden Operator ersetzen
             *   replaces existing operator with this one
             * */
            if (operator) {
                input.deleteCharAt(input.length - 1)
                input.append('+')
                if(result!=null)
                    result!!.setText(input)
                // operator = false;

            } else {

                /* fehlende Ziffer auffuellen zB. "0.+" -> "0.0+"
                 *  autofills missing input e.g. "0.+" -> "0.0+"
                 * */
                if (input[input.length - 1] == '.') {
                    input.append(0)
                }
                input.append('+')

                if(result!=null)
                    result!!.setText(input)

                operator = true
                currentcomma = false
                discontinue = false
            }
        }

    }

    fun minus(view: View) {

        if (input.length > 0) {
            val current = input[input.length - 1]
            if (operator) {
                input.deleteCharAt(input.length - 1)
                input.append('-')
                if(result!=null)
                    result!!.setText(input)

            } else {
                if (input[input.length - 1] == '.') {
                    input.append(0)
                }
                input.append('-')

                if(result!=null)
                    result!!.setText(input)

                operator = true
                currentcomma = false
                discontinue = false
            }
        }

    }

    fun divide(view: View) {

        if (input.length > 0) {
            if (operator) {
                input.deleteCharAt(input.length - 1)
                input.append('/')
                if(result!=null)
                    result!!.setText(input)
                // operator = false;
            } else {
                if (input[input.length - 1] == '.') {
                    input.append(0)
                }
                input.append('/')
                if(result!=null)
                    result!!.setText(input)

                operator = true
                currentcomma = false
                discontinue = false
            }
        }

    }

    fun Comma(view: View) {


        if (input.length > 0) {
            val test = input[input.length - 1]
            if (test != '.') {
                /*  Falls ein "." bereits eingegeben wurde.
                 *    Was "." already entered before?
                 * */
                if (!currentcomma) {
                    /* Fehlender Eintrag vor Komma ergaenzen -> zB. "1+." -> "1+0."
                     *   Insert missing digit in advance e.g. "1+." -> "1+0."
                     * */
                    if (test == '+' || test == '-' || test == '/' || test == '*' || test == '(' || test == ')') input.append(
                        0
                    )
                    input.append('.')

                    if(result!=null)
                        result!!.setText(input)

                    currentcomma = true
                    allcomma = true
                    countcomma++
                    discontinue = false
                }
            }
        } else { // "." -> "0."
            input.append(0).append('.')

            if(result!=null)
                result!!.setText(input)

            currentcomma = true
            allcomma = true
            countcomma++
            discontinue = false
        }

        /* Eintrag war kein Operator.
         *   entry was no operator, ergo false.
         * */
        if (operator) operator = false

    }

    fun result(view: View) {

        // test(view);
        while (opened != closed) {
            input.append(')')
            closed++
        }

        var done = false

        val aaa: List<String> = infix(input.toString())



        val res: List<String> = rpn(aaa)

        val it = res.listIterator()

        input.delete(0, input.length)

        val queue: Stack<String?> = Stack()

        /*
        while(it.hasNext()){
            String current = it.next();
            input.append(current);

        }*/while (!done) {
            if (it.hasNext()) {
                val current = it.next()
                when (current) {
                    "+" -> {
                        val op2 = queue.pop()
                        val op1 = queue.pop()
                        if (isNumeric(op2) && isNumeric(op1)) {
                            val operand2: Double = op2!!.toDouble()
                            val operand1: Double = op1!!.toDouble()
                            val result = operand1 + operand2
                            queue.push(java.lang.Double.toString(result))
                        }
                    }
                    "-" -> {
                        val op2 = queue.pop()
                        val op1 = queue.pop()
                        if (isNumeric(op2) && isNumeric(op1)) {
                            val operand2: Double = op2!!.toDouble()
                            val operand1: Double = op1!!.toDouble()
                            val result = operand1 - operand2
                            queue.push(java.lang.Double.toString(result))
                        }
                    }
                    "*" -> {
                        val op2 = queue.pop()
                        val op1 = queue.pop()
                        if (isNumeric(op2) && isNumeric(op1)) {
                            val operand2: Double = op2!!.toDouble()
                            val operand1: Double = op1!!.toDouble()
                            val result = operand1 * operand2
                            queue.push(java.lang.Double.toString(result))
                        }
                    }
                    "/" -> {
                        val op2 = queue.pop()
                        val op1 = queue.pop()
                        if (isNumeric(op2) && isNumeric(op1)) {
                            val operand2: Double = op2!!.toDouble()
                            val operand1: Double = op1!!.toDouble()
                            val result = operand1 / operand2
                            /* falls Division durch 0 erfolgt ist
                                if x/0 occured


                            if(Float.isInfinite(result))
                                isinfinite=true;
                            else {

                                if (Float.isNaN(result))
                                    isNan = true;
                            }*/queue.push(java.lang.Double.toString(result))
                        }
                    }
                    else -> queue.push(current)
                }
            } else done = true
            // input.append(current+" ");
        }


        if (queue.size > 0) {
            /* if(isinfinite){
                result.setText("inf");
            }

            if(isNan){
                result.setText("Nan");
            }

            else {*/
            if (allcomma) { //if result is double
                input.append(queue.pop())

                if(result!=null)
                    result!!.setText(input.toString())

            } else { //result might be an int
                val wholenumber = queue.pop()
                //conversion from double to int
                val temp = wholenumber!!.toDouble()
                if (temp % 1 == 0.0) { //is result really an int?
                    val number = Math.round(temp)
                    input.append(number)
                } else {
                    input.append(temp)
                }
                if(result!=null)
                    result!!.setText(input.toString())
            }
        } else {
            if(result!=null)
                result!!.setText("Error")
        }


    }

    private fun isNumeric(str: String?): Boolean {
        if (str != null) {
            return str.matches(Regex("-?\\d+(\\.\\d+)?"))
        }  //match a number with optional '-' and decimal.
        else
            return false
    }

    private fun isFloat(str: String): Boolean {
        return str.matches(Regex("-?\\d+\\.{1}\\d+"))
    }

    /**
     * @params in
     * Jedes Element (Klammer, Operand, Operator) gespeichert in einer List<String>.
     * Contains every component (parentheses, numeric, operation) in a List<String>.
     *
     *
     * @return List<String>
     * List<String> in RPN(Reverse Polish Notation) Reihenfolge.
     * List<String> containing every component, but in RPN(Reverse Polish Notation) order.
     * 2 + 5 -> 2 5 +
     *
     *
     * source: https://digitalcommons.unl.edu/cgi/viewcontent.cgi?article=1045&context=mathmidexppap
     */
    fun rpn(`in`: List<String>): List<String> {
        val result = 0.0

        /* beinhaltet nur Klammern & Operatoren.
         *   contains parentheses & operators only.
         * */
        val operator: Stack<String> = Stack()
        val postfix: MutableList<String> = ArrayList() //output
        val parts = `in`.listIterator()

        // float temporary=0;

        var current: String?
        var compare: String
        while (parts.hasNext()) {
            current = parts.next()
            when (current) {
                "+", "-" -> if (operator.size == 0) { //1st entry
                                operator.push(current)
                            } else {
                                var condition = true
                                if (operator.peek() != "(") condition = false

                                else operator.push(current)
                                /* Momentaner Operator wird mit Operator Stack verglichen, bis Bedingung erfuellt oder Stack leer ist.
                                *  compares current operator +/- with operator stack until condition is met or end of stack reached.
                                * */
                                while (operator.size > 0 && !condition) {
                                    compare = operator.peek()
                                    if (compare == "(" || compare == ")") { //operator.pop();
                                        operator.push(current)
                                        condition = true
                                    } else {
                                        compare = operator.pop()


                                        postfix.add(compare)
                                    }
                                }
                                    /* Ende des Stacks erreicht, aber aktueller Operator noch nicht eingesetzt.
                                     *  end of stack reached but current not inserted yet.
                                     * */
                                    if (operator.size == 0 && !condition) //
                                    operator.push(current)
                            }

                "*", "/" -> if (operator.size == 0) {
                                 operator.push(current)
                            } else {
                                var condition = true
                                if (operator.peek() != "(") condition = false else operator.push(current)
                                //check operator stack contents
                                while (operator.size > 0 && !condition) {
                                    compare = operator.peek()
                                    if (compare == "(" || compare == ")") {
                                        operator.push(current)
                                        condition = true
                                    } else {
                                        if (compare == "*" || compare == "/") {
                                            compare = operator.pop()
                                            postfix.add(compare)
                                        } else {
                                            operator.push(current)
                                            condition = true
                                        }
                                    }
                                }
                                /* Ende des Stacks erreicht, aber aktueller Operator noch nicht eingesetzt.
                                 *  end of stack reached but current not inserted yet.
                                 * */
                                if (operator.size == 0 && !condition) operator.push(current)
                            }
                "(" -> operator.push(current)
                ")" ->
                    {
                        var done = true
                        if (operator.peek() != "(") done = false else  //operator.pop() == (
                            operator.pop()

                        /*alles in einem Klammerausdruck zum postfix adden.
                         *
                         * add all operator within ( ) expression into the postfix
                         * */
                            while (operator.size > 0 && !done) { //
                                compare = operator.pop()
                                if (compare == "(") done = true else {
                                    postfix.add(compare)
                                }
                             }
                     }
                else -> postfix.add(current as String)
            }
        }
        /* restliche Eintraege
         *   add remaining entries
         * */

        while (operator.size > 0) {
            postfix.add(operator.pop())
        }
        return postfix
    }



    /**
     * @param raw
     * eine gueltige Eingabe als ganzer String raw gespeichert.
     * a legit expression stored as a whole String raw.
     *
     *
     *
     * @return List<String>
     * Jedes Element wird einzeln in einer List<String> gespeichert.
     * List<String> where each element (operand, operator, parentheses) is listed seperately.
    </String></String></String> */
    private fun infix(raw: String): List<String> {
        val output: MutableList<String> = ArrayList()
        val operator: List<Char?> = ArrayList()
        var tester: Char
        val opexist = false
        var parentheses = false
        var opened = 0
        var closed = 0
        val number = java.lang.StringBuilder()
        /*
        for(int i=0;i<raw.length();i++){
           if(raw.charAt(i)=='(')
                parentheses=true;

        }
        */
        for (i in 0 until raw.length) {
                val current = raw[i]
                if (current == '+' || current == '-' || current == '*' || current == '/') {
                    if (number.length != 0) output.add(number.toString())
                    output.add(current.toString())
                    // operator.add('+');
                    // tester = '+';
                   //  opexist = true;
                    number.delete(0, number.length) //reset the operand variable
                    //number = new StringBuilder();
                } else {
                    if (current == '(') {
                        if (number.length != 0) output.add(number.toString())
                        output.add(current.toString())
                        number.delete(0, number.length) //reset the operand variable
                        //number = new StringBuilder();
                        parentheses = true
                        opened++
                    } else {
                        if (current == ')') {
                            ++closed
                            if (number.length != 0) output.add(number.toString())
                            output.add(current.toString())
                            number.delete(0, number.length) //reset the operand variable
                            //number = new StringBuilder();
                            if (closed == opened) parentheses = false
                        } else {
                            number.append(current)
                        }
                    }
                }
            }
            if (number.length != 0) //
                output.add(number.toString())
            return output
        }


}
