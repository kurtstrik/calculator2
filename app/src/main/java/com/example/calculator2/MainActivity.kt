package com.example.calculator2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.trimmedLength
import java.math.BigDecimal

import kotlin.collections.ArrayList
import java.text.DecimalFormatSymbols
import java.util.*


//TODO: test with firebase - https://developer.android.com/studio/test/test-in-android-studio
class MainActivity : AppCompatActivity(), View.OnClickListener {


  //counter for "(" & ")"
  private var opened = 0
  private var closed = 0

  private var countcomma = 0 // Keeps track of "." if some of them get deleted.

  private var currentcomma:Boolean = false //  Is there a "." in current expression?

  private var allcomma:Boolean = false // Is there a "." at all?

  private var operator:Boolean = false // Was previous entry an operator?

  private var discontinue:Boolean = false // checks if further 0 input is allowed

  private var notazero = StringBuilder() //  checks if pre decimal is zero only. important for delete()-method afterwards!

  private var input = StringBuilder() // current total input

  var result: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      result = findViewById(R.id.textView) as TextView
  }

  override fun onClick(v: View?) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
      super.onConfigurationChanged(newConfig)

      // Checks the orientation of the screen
      if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
          setContentView(R.layout.activity_main_land)
      else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
          setContentView(R.layout.activity_main)

      result = findViewById(R.id.textView) as TextView
      result?.let { result!!.text = (input.toString()) }

  }


  /**
   * Deletes all entered inputs and resets all conditions to starting point
   * */
  fun delete_all(view: View) {
      reset()
      result?.let { result!!.text = (input.toString()) }

  }

  /**
   * Deletes the last entered letter and updates conditions accordingly
   * */
  fun delete(view: View) {

    if (input.length > 0) {

      val current = input[input.length - 1]

      input.deleteCharAt(input.length - 1)
      result!!.text = (input.toString())

      val wasOperator:Boolean = (current == '+' || current == '-' || current == '*' || current == '/')

      if (wasOperator && operator) // Check conditions after delete.
          operator = false
      else {

        when(current) {
           '(' -> if (opened > 0)
                      opened--
           ')' -> if (closed > 0)
                      closed--
           '.' -> {
               currentcomma = false
               --countcomma

               if (countcomma <= 0)
                   allcomma = false

               // Checks the pre-comma value

               val check = false
               // StringBuilder predecimal = new StringBuilder();
               val predecimal: Stack<Char?> = Stack()

               for (i in input.length - 1 downTo 0) {

                   val opcheck = input[i]
                   val nonnumerical:Boolean = (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/' || opcheck == '(')

                   if (nonnumerical) { // break condition
                       break
                   } else { //if(opcheck!=')')
                       predecimal.push(opcheck)
                   }
               }

               if (predecimal.size > 1) // pre decimal indexes > 1, zB. "10" / "56" -> further zeros allowed
                   discontinue = false
               else {
                 if (predecimal.pop() == '0')
                     discontinue = true // unit position is "0" -> further zeros not allowed
                 else
                     discontinue = false // unit position != "0" -> further zeros allowed
               }
           }

            // Infinity
           'y' -> { reset()
                    return
           }

           else -> {
                   if (allcomma) {
                     currentcomma = false // assumption: current expression not a decimal number with comma

                     for (i in input.length - 1 downTo 0) {
                         val opcheck = input[i]
                         val nonnumerical:Boolean = (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/' || opcheck == '(')

                       if (nonnumerical)// current operand read -> break
                         break

                       if (opcheck == '.')
                         currentcomma = true
                     }
                   }

                   // if deletion leads back to an operator index -> operator = true
                   if (input.length >= 2) {
                       val opcheck = input[input.length - 1]
                       val wasOperator:Boolean = (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/')


                       if (wasOperator)
                          operator = true

                   }
           }
        }

        /*
        if (current == '(') {
            if (opened > 0) opened--
        }
        if (current == ')') if (closed > 0) closed--
        if (current == '.') {
          currentcomma = false
          --countcomma
          if (countcomma <= 0) allcomma = false
          /*Da eine Vorkommastelle existiert, wird diese geprueft
           Checks the pre-comma value
         */
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
                    discontinue =
                        true //Einerstelle ist "0" -> weitere Nullen nicht erlaubt
                } else discontinue = false //Einerstelle != "0" -> weitere Nullen erlaubt
            }
          }
          if (current.equals('y')) { // we have infinity
              reset()
              return
          }
          else {


            if (allcomma) {
              currentcomma = false // Annahme, dass aktueller Ausdruck keine Kommazahl ist

              for (i in input.length - 1 downTo 0) {
                  val opcheck = input[i]

                  if (opcheck == '+' || opcheck == '-' || opcheck == '*' || opcheck == '/' || opcheck == '(')// aktueller Operand abgelesen -> Abbruchbedingung
                      break

                  if (opcheck == '.')
                      currentcomma = true
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

          } */
      }
      result?.let { result!!.text = (input.toString()) }
    }

  }

  fun Klammer_auf(view: View) {

      if (input.length > 0) { // Checks entries before "("
          val current = input[input.length - 1]
          val openupAllowed:Boolean = (current == '(' || operator)


          if (openupAllowed) {
              input.append('(')

              opened++ // "(" added -> increment
              operator = false

              result?.let { result!!.text = (input.toString()) }
          }

      } else { //1st index
          if (input.length == 0) {
              input.append('(')

              opened++

              result?.let { result!!.text = (input.toString()) }
          }
      }

      return

  }

  fun Klammer_zu(view: View) {

      // Is there an unclosed expression?
      if (opened > 0 && opened != closed) {
          val current = input[input.length - 1]
          val closeAllowed:Boolean = (!operator && current != '(')


          // Only close ")" without operator or "(" directly in front.
          if (closeAllowed) {
              input.append(")")

              closed++

              result?.let { result!!.text = (input.toString()) }
              //result?.text=(input.toString())
          }
      }

      return
  }

  fun click_zero(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.length > 0) {

              /*
               // TODO: overwriting infinity with 0
              if (input.contains("Infinity")&&!operator)
                  reset()

              input.append('0')
              */

              val current = input[input.length - 1]

              if (!currentcomma && !discontinue) {

                  if (operator || current == '(') { // e.g. "0" input @ "5+" -> "5+0" or "0" input @ "5+(" -> "5+(0"
                      input.append('0')
                      result?.let { result!!.text = (input.toString()) }
                      discontinue = true
                  } else {
                      if (current == ')')
                          discontinue = true

                      // after a non-zero digit e.g."100" or "545000"
                  }
              }
          } else {
              input.append('0')
              result?.let { result!!.text = (input.toString()) }
              discontinue = true
          }

          if (!discontinue) {
              input.append('0')
              result?.let { result!!.text = (input.toString()) }
          }

          if (operator) operator = false // x + 0

          return
      }
  }

  fun click_one(view: View) {

      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.
          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('1')

      }
     /*
      if (input.length > 0) {
          val current = input[input.length - 1]
          if (current == '0' && discontinue) {
              input.deleteCharAt(input.length - 1)
              discontinue = false
              input.append('1')
          } else input.append('1')
      } else input.append('1')



      result?.let { result!!.text = (input.toString()) }

      if (!currentcomma) {
          discontinue = false
      }

      if (operator) operator = false

      return

       */
  }

  fun click_two(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('2')

      }
  }

  fun click_three(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('3')

      }
  }

  fun click_four(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('4')

      }
  }

  fun click_five(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('5')

      }
  }

  fun click_six(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('6')

      }
  }

  fun click_seven(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('7')

      }
  }

  fun click_eight(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('8')

      }
  }

  fun click_nine(view: View) {
      if (checkTermlength() > 14 )
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      else {
          // TODO: how to handle combinations with "Infinity"

          if (input.contains("Infinity")&&!operator)
              reset()

          inputprocess('9')

      }
  }


  /**
   * Checks the term of current index for length before the input.
   * We want the numbers to have limited digits, because data types have limited value range.
   * In this case each term will have a limit of x = 14 + 1 (because we add the number input).
   *
   * @return length from 1 to x, if it is bigger we just output y = 15
   *
   * **/
  private fun checkTermlength():Byte {
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

      if (x <= 14)
          return x
      else
          return y
  }

  /**
   * Processes the given input and change corresponding conditions/parameters accordingly
   *
   * @param number input as Char Type
   * **/
  private fun inputprocess(number: Char) {
      if (input.length > 0) {
          val current = input[input.length - 1]
          val termIsOnlyAZero = (current == '0' && discontinue)


          if (termIsOnlyAZero) {
              input.deleteCharAt(input.length - 1) // replace 0 with input
              discontinue = false
          }

          input.append(number)
      } else input.append(number)

      result?.let { result!!.text = (input.toString()) }

      if (!currentcomma)
        discontinue = false

      if (operator)
        operator = false

      return
  }

  fun multiply(view: View) {

      if (input.length > 0) {

          if (operator) {
              input.deleteCharAt(input.length - 1)
              input.append('*')
              result?.let { result!!.text = (input.toString()) }
              // operator = false;
          } else {
              val previous = input[input.length - 1]


              if (previous == '.')
                input.append(0)

              if (previous != '(') {

                  input.append('*')

                  result?.let { result!!.text = (input.toString()) }

                  operator = true
                  currentcomma = false
                  discontinue = false
              }
          }
      }

      return
  }

  fun plus(view: View) {

      if (input.length > 0) {


          // replaces existing operator with this one
          if (operator) {
              input.deleteCharAt(input.length - 1)
              input.append('+')
              result?.let { result!!.text = (input.toString()) }
              // operator = false;

          } else {
              val previous = input[input.length - 1]

              if (previous == '.')// autofills missing input e.g. "0.+" -> "0.0+"
                  input.append(0)

              if (previous != '(') {

                  input.append('+')

                  result?.let { result!!.text = (input.toString()) }

                  operator = true
                  currentcomma = false
                  discontinue = false
              }
          }
      }
      return
  }

  fun minus(view: View) {

      if (input.length > 0) {
          val current = input[input.length - 1]
          if (operator) {


              // TODO: check for <operator> followed by - situation
              input.deleteCharAt(input.length - 1)


              input.append('-')

              if (result != null)
                  result!!.text = (input.toString())

          } else {
              val previous = input[input.length - 1]


              if (previous == '.')
                  input.append(0)

              if (previous != '(') {

                  input.append('-')

                  result?.let { result!!.text = (input.toString()) }

                  operator = true
                  currentcomma = false
                  discontinue = false
              }
          }
      }

      /*
      else {

          input.append('-')
      }*/


      return
  }

  fun divide(view: View) {

      if (input.length > 0) {

          if (operator) {
              input.deleteCharAt(input.length - 1)
              input.append('/')
              result?.let { result!!.text = (input.toString()) }
              // operator = false;
          } else {
              val previous = input[input.length - 1]


              if (previous == '.')
                  input.append(0)

              if (previous != '(') {

                  input.append('/')

                  result?.let { result!!.text = (input.toString()) }

                  operator = true
                  currentcomma = false
                  discontinue = false
              }
          }
      }

      return
  }

  fun Comma(view: View) {


      if (input.length > 0) {
          val test = input[input.length - 1]

          if (test != '.' && !currentcomma) {

              //  Was "." already entered before?

              // Insert missing digit in advance e.g. "1+." -> "1+0."

              if (test == '+' || test == '-' || test == '/' || test == '*' || test == '(' || test == ')')
                  input.append(0)

              input.append('.')

              result?.let { result!!.text = (input.toString()) }

              currentcomma = true
              allcomma = true
              countcomma++
              discontinue = false

          }
      } else { // "." -> "0."
          input.append(0).append('.')

          result?.let { result!!.text = (input.toString()) }

          currentcomma = true
          allcomma = true
          countcomma++
          discontinue = false
      }

      if (operator)
          operator = false //  entry was no operator, ergo false.

      return
  }

  fun result(view: View) {

      // TODO: https://en.wikipedia.org/wiki/Shunting_yard_algorithm

      // test(view);

      if (input.length > 0) {
          if(input[input.length - 1] == '.') { // if the last input is an empty "x." -expression -> fill with 0
              input.append('0')
          }
      }

      while (opened != closed) {
          input.append(')')
          closed++
      }

      var done = false

      val infix: List<String> = infix(input.toString())

      val rpn: List<String> = rpn(infix)

      val it = rpn.listIterator()

      input.delete(0, input.length)

      val queue: Stack<String?> = Stack()

      while (!done) {
          if (it.hasNext()) {
              val current = it.next()


              val isOperator = (current == "+" || current == "-" || current == "*" || current == "/")

              if (!isOperator) {
                  queue.push(current)
              }
              else {

                  val case = Operandspresent(queue)

                  // initial declarations
                  var operand2 = BigDecimalOrInfinite("0")
                  operand2.setInfinite(false)
                  var operand1 = BigDecimalOrInfinite("0")
                  operand1.setInfinite(false)


                  when(case) {
                      0 -> { // when both operand are not given
                          operand2 = BigDecimalOrInfinite("0")
                          operand1 = BigDecimalOrInfinite("0")
                      }
                      1 ->{ // only 1 operand given
                          val op1 = queue.pop()

                          if (isNumeric(op1)) {
                              operand2 = BigDecimalOrInfinite("0")
                              operand1 = BigDecimalOrInfinite(op1!!)

                          }

                          if (isInfinite(op1))
                              operand1.setInfinite(true)

                      }
                      2 ->{ // both operands given
                          val op2 = queue.pop()
                          val op1 = queue.pop()
                          if (isNumeric(op2) && isNumeric(op1)) { // TODO: what if one is notNumeric? or Infinity
                              operand2 = BigDecimalOrInfinite(op2!!)
                              operand1 = BigDecimalOrInfinite(op1!!)
                          }

                          if (isInfinite(op1))
                              operand1.setInfinite(true)

                          if (isInfinite(op2))
                              operand2.setInfinite(true)


                      }
                  }


              when(current) {
                  "+" -> { // TODO: implement infinity handling

                      if (operand1.isInfinite()||operand2.isInfinite())
                         queue.push("Infinity")
                      else {
                          val result = operand1.add(operand2)
                          queue.push(result.toString())
                      }
                  }
                  "-" -> { // TODO: implement ∞ handling
                      if (operand1.isInfinite() && operand2.isInfinite()) // ∞ - ∞
                          queue.push("NaN")

                      if (!operand1.isInfinite() && operand2.isInfinite()) // TODO: x - ∞

                      if (operand1.isInfinite() && !operand2.isInfinite()) // ∞ - x
                          queue.push("Infinity")

                      if (!operand1.isInfinite() && !operand2.isInfinite()) { // x - y

                          val result = operand1.subtract(operand2)
                          queue.push(result.toString())
                      }

                  }
                  "*" -> { // TODO: implement ∞ handling

                      // ∞ × ∞ = ∞
                      //
                      //-∞ × -∞ = ∞
                      //
                      //-∞ × ∞ = -∞

                      if (operand1.isInfinite() && operand2.isInfinite()) // ∞ * ∞
                          queue.push("Infinity")

                      if (!operand1.isInfinite() && operand2.isInfinite()) // TODO: x * ∞

                      if (operand1.isInfinite() && !operand2.isInfinite()) // ∞ * x
                          queue.push("Infinity")

                      if (!operand1.isInfinite() && !operand2.isInfinite()) { // x * y

                          val result = operand1.multiply(operand2)
                          queue.push(result.toString())
                      }
                  }
                  "/" -> { // TODO: implement ∞ handling

                      try {
                            // ∞ ÷ ∞ = NaN
                            // x ÷ ∞ = 0
                            // ∞ ÷ x = ∞

                          if (operand1.isInfinite() && operand2.isInfinite()) // ∞ ÷ ∞ = NaN
                              queue.push("NaN")

                          if (!operand1.isInfinite() && operand2.isInfinite()) // x ÷ ∞ = 0
                              queue.push("0")

                          if (operand1.isInfinite() && !operand2.isInfinite()) // ∞ ÷ x = ∞
                                  queue.push("Infinity")

                          if (!operand1.isInfinite() && !operand2.isInfinite()) { // x ÷ y

                              if (operand2.compareTo(BigDecimal.ZERO)==0) { // division by 0

                                  if (operand1.compareTo(BigDecimal.ZERO)==0)
                                     queue.push("NaN") // 0 ÷ 0

                                  else
                                     queue.push("Infinity")
                              }
                              else {

                                  val result = operand1.divide(operand2)
                                  queue.push(result.toString())
                              }
                          }

                          /*
                          else {
                              val result = operand1.divide(operand2) // TODO: how to handle 6/(-2)?
                              queue.push(result.toString())
                          }
                          */
                      } catch (e:ArithmeticException) {
                          Toast.makeText(this, "error in calculation!", Toast.LENGTH_SHORT).show()
                          reset()
                          done= true
                          queue.clear()
                      }
                  }
              }

              }


              /*
              when (current) {
                  "+" -> { //TODO: what when 1 operand empty?/missing

                      val case = Operandspresent(queue)

                      when (case) {
                         0 -> {}
                         1 -> {}
                         2 -> {

                         }


                      }

                      val op2 = queue.pop()
                      val op1 = queue.pop()
                      if (isNumeric(op2) && isNumeric(op1)) {
                          val operand2 = BigDecimal(op2!!)
                          val operand1 = BigDecimal(op1!!)
                          val result = operand1.add(operand2)
                          queue.push(result.toString())
                      }
                  }
                  "-" -> {
                      val op2 = queue.pop()
                      val op1 = queue.pop()
                      if (isNumeric(op2) && isNumeric(op1)) {
                          val operand2 = BigDecimal(op2!!)
                          val operand1 = BigDecimal(op1!!)
                          val result = operand1.subtract(operand2)
                          queue.push(result.toString())
                      }
                  }
                  "*" -> {
                      val op2 = queue.pop()
                      val op1 = queue.pop()
                      if (isNumeric(op2) && isNumeric(op1)) {
                          val operand2 = BigDecimal(op2!!)
                          val operand1 = BigDecimal(op1!!)
                          val result = operand1.multiply(operand2)
                          queue.push(result.toString())
                      }
                  }
                  "/" -> {
                      val op2 = queue.pop()
                      val op1 = queue.pop()
                      if (isNumeric(op2) && isNumeric(op1)) {
                          val operand2 = BigDecimal(op2!!)
                          val operand1 = BigDecimal(op1!!)

                          try {

                              if (operand2.compareTo(BigDecimal.ZERO)==0) {
                                  val result = Double.POSITIVE_INFINITY
                                  queue.push(result.toString())
                              }
                              else {
                                  val result = operand1.divide(operand2) // TODO: how to handle 6/(-2)?
                                  queue.push(result.toString())
                              }
                          } catch (e:ArithmeticException) {
                              Toast.makeText(this, "error in calculation!", Toast.LENGTH_SHORT).show()
                              reset()
                              done= true
                              queue.clear()
                          }


                          /*
                          lateinit var result:String

                          if (operand2 == 0.0)
                              result = DecimalFormatSymbols.getInstance().infinity
                          else
                              result = (operand1 / operand2).toString()

                           */
                          /* falls Division durch 0 erfolgt ist
                          if x/0 occured



                      if(Float.isInfinite(result))
                          isinfinite=true;
                      else {

                          if (Float.isNaN(result))
                              isNan = true;
                      }*/
                        //  queue.push(result)
                      }
                  }
                  else -> queue.push(current)
              }*/
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

          val wholenumber = queue.pop()
          //conversion from double to int
          val temp = wholenumber!!.toDouble()

          if (temp % 1 == 0.0) { // is result really an int?
              val number = Math.round(temp)
              input.append(number)
          } else {
              input.append(temp)
          }

          result?.let { result!!.text = (input.toString()) }


          // TODO: remove if/else?


          /*

          if (allcomma) { //if result is double

              val wholenumber = queue.pop()
              //conversion from double to int
              val temp = wholenumber!!.toDouble()

              if (temp % 1 == 0.0) { // is result really an int?
                  val number = Math.round(temp)
                  input.append(number)
              } else {
                  input.append(temp)
              }

              result?.let { result!!.text = (input.toString()) }

          } else { //result might be an int
              val wholenumber = queue.pop()
              //conversion from double to int
              val temp = wholenumber!!.toDouble()

              if (temp % 1 == 0.0) { // is result really an int?
                  val number = Math.round(temp)
                  input.append(number)
              } else {
                  input.append(temp)
              }
              result?.let { result!!.text = (input.toString()) }
          }*/
      } else {
          if (result != null)
              result!!.text = ("Error")
      }

      reset()

      input.append(result!!.text)

      return
  }

  private fun Operandspresent(stack: Stack<String?>):Int {
    if (stack.empty())
        return 0

    var op2:String? = stack.pop()
    var op1:String? = null

    try {
        op1 = stack.pop()
    } catch(e: EmptyStackException) {
        return 1
    }

    //putting back on stack after checking
    stack.push(op1)
    stack.push(op2)

    return 2 // if all error cases did not occur, we have both operands on the stack

  }

  private fun isTruncatable(str: String?): Boolean {
      if (str != null)
          return str.matches(Regex("-?\\d+(\\.0+)?(E\\d+)*"))

      else
          return false

  }

  private fun isInfinite(str: String?): Boolean {
      if (str != null)
          return str.contains("Infinity")

      else
          return false

  }

  private fun isNumeric(str: String?): Boolean {

      if (str != null)
        return str.matches(Regex("-?\\d+(\\.\\d+)?(E\\d+)*")) //match a number with optional '-' and decimal. TODO: or Infinity?

      else
          return false
  }

  private fun isExpression(str: String?):Boolean {

      "-?(*\\d+(\\.\\d+)?(E\\d+)?)*"
      if (str != null)
          return str.matches(Regex("-?\\d+(\\.\\d+)?(E\\d+)*[-+*/]?")) //match a number with optional '-' and decimal.

      else
          return false

  }

  private fun isFloat(str: String): Boolean {
      return str.matches(Regex("-?\\d+\\.{1}\\d+"))
  }

  private fun reset() {

      opened = 0
      closed = 0
      countcomma = 0
      currentcomma = false
      allcomma = false
      operator = false
      discontinue = false
      notazero.setLength(0)
      input.setLength(0)
  }

  /**
   * Transform given input (infix form) into reverse polish notation
   *
   * @params list Contains every component (parentheses, numeric, operation) in a List<String>.
   * @return List<String>
   * List<String> containing every component, but in RPN(Reverse Polish Notation) order.
   *
   * 2 + 5 -> 2 5 +
   *
   * source: https://digitalcommons.unl.edu/cgi/viewcontent.cgi?article=1045&context=mathmidexppap
   *
   */
  fun rpn(list: List<String>): List<String> {
      val result = 0.0

      // contains parentheses & operators only.
      val operator: Stack<String> = Stack()
      val postfix: MutableList<String> = ArrayList() //output
      val parts = list.listIterator()

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

                  if (operator.peek() != "(")
                    condition = false

                  else operator.push(current)

                  // compares current operator +/- with operator stack until condition is met or end of stack reached.
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

                  //end of stack reached but final operator not yet inserted
                  if (operator.size == 0 && !condition)
                    operator.push(current)
              }

              "*", "/" -> if (operator.size == 0) {
                  operator.push(current)
              } else {
                  var condition = true

                  if (operator.peek() != "(")
                    condition = false

                  else operator.push(current)

                  //check operator stack contents
                  while (operator.size > 0 && !condition) {
                      compare = operator.peek()
                      if (compare == "(" || compare == ")") {
                          operator.push(current)
                          condition = true
                      }

                      if (compare == "*" || compare == "/") {
                          compare = operator.pop()
                          postfix.add(compare)
                      }
                      else {
                          operator.push(current)
                          condition = true
                      }
                  }

                  //end of stack reached but final operator not yet inserted
                  if (operator.size == 0 && !condition)
                    operator.push(current)
              }
              "(" -> operator.push(current)
              ")" -> {
                  var done = true

                  if (operator.peek() != "(")
                    done = false

                  else operator.pop()

                  // add all operator within ( ) expression into the postfix
                  while (operator.size > 0 && !done) { //
                      compare = operator.pop()
                      if (compare == "(")
                        done = true

                      else
                        postfix.add(compare)

                  }
              }
              else -> postfix.add(current as String)
          }
      }

      // add remaining entries
      while (operator.size > 0) {
          postfix.add(operator.pop())
      }
      return postfix
  }


  private fun shuntingyardalgorithm(raw:String):String {
      val output: Stack<String> = Stack()

      val operator: Stack<Char?> = Stack()


      for (i in 0 until raw.length) {
          val current = raw[i]

          when (current) {
            '+','-' -> {}

            '*','/' -> {}

            '.' -> {}

            '(' -> {
                operator.push(current)
            }

            ')' -> {}

            else -> {
                output.push(current.toString())

            }


          }


      }

      return ""
  }


  /**
   * takes a given expression and seperates the output (   *
   *
   * @param String a legit expression stored as a whole String raw. eg. (2+5)-1
   *
   * @return List<String> where each element (operand, operator, parentheses) is listed seperately.
   */
  private fun infix(raw: String): List<String> {
      val output: MutableList<String> = ArrayList()
      
      val operator: List<Char?> = ArrayList()
      var tester: Char
      val opexist = false
      var parentheses = false

      var negative = false
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

          when (current) {


              '+','-','*','/' -> {
                  if (number.length != 0)
                      output.add(number.toString())

                  output.add(current.toString())
                  // operator.add('+');
                  // tester = '+';
                  //  opexist = true;
                  number.setLength(0) //reset the operand variable

              }

              '(' -> {
                  if (number.length != 0)
                      output.add(number.toString())

                  output.add(current.toString())
                  number.setLength(0) //reset the operand variable

                  opened++

              }

              ')' -> {
                  ++closed
                  if (number.length != 0)
                      output.add(number.toString())

                  output.add(current.toString())
                  number.setLength(0) //reset the operand variable

              }

              else -> {

                  number.append(current)
              }



          }


          /*
          if (current == '-') {

              // is - an operator or a negative sign?
              if (i > 0) {
                val wasOperator = (raw[i-1] == '+' || raw[i-1] == '-' || raw[i-1] == '*' || raw[i-1] == '/')

                if (wasOperator) {
                    negative = true

                }
                else {

                    negative = false
                }


              }

          }

          if (current == '+' || current == '*' || current == '/') {




              if (number.length != 0)
                output.add(number.toString())

              output.add(current.toString())
              // operator.add('+');
              // tester = '+';
              //  opexist = true;
              number.setLength(0) //reset the operand variable

          } else {
              if (current == '(') {
                  if (number.length != 0)
                    output.add(number.toString())

                  output.add(current.toString())
                  number.setLength(0) //reset the operand variable

                  opened++
              } else {
                  if (current == ')') {
                    ++closed
                      if (number.length != 0)
                        output.add(number.toString())

                      output.add(current.toString())
                      number.setLength(0) //reset the operand variable

                  } else {
                      number.append(current)
                  }
              }
          }*/
      }

      if (number.length != 0) //
        output.add(number.toString())

      if (output.size > 0) {
          val lastnotdigit = (output.last() == "+") ||
                  (output.last() == "-") ||
                  (output.last() == "*") ||
                  (output.last() == "/") ||
                  (output.last() == "(")
          if (lastnotdigit)
              output.removeAt(output.size-1)

      }

      return output
  }

  public fun getOutput():String {
      return input.toString()
  }


}
