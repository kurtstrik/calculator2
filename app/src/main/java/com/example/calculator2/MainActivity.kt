package com.example.calculator2

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal

import kotlin.collections.ArrayList
import java.util.*
import kotlin.system.exitProcess


//TODO: test with firebase - https://developer.android.com/studio/test/test-in-android-studio
class MainActivity : AppCompatActivity(), View.OnClickListener {

  //MutableLiveData?

  val processor = Processor()

  //counter for "(" & ")"
  private var opened = 0
  private var closed = 0

  private var countcomma = 0 // Keeps track of "." if some of them get deleted.

  private var currentcomma:Boolean = false //  Is there a "." in current expression?

  private var allcomma:Boolean = false // Is there a "." at all?

  private var operator:Boolean = false // Was previous entry an operator?

  private var discontinue:Boolean = false // checks if further 0 input is allowed

  private var negative:Boolean = false // is the current index to be negative

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
      processor.reset()
      result?.let { result!!.text = (processor.getInput()) }
  }

  /**
   * Deletes the last entered letter and updates conditions accordingly
   * */
  fun delete(view: View) {
      processor.delete()
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun Klammer_auf(view: View) {
      processor.openingP()
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun Klammer_zu(view: View) {
      processor.closingP()
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_zero(view: View) {

      // TODO: inputprocess with 0

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      }
      else {
          processor.processzero()
          result?.let { result!!.text = (processor.getInput()) }
      }
  }

  fun click_one(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.
         // if (input.contains("Infinity") && !operator) reset()
          processor.inputprocess('1')
      }
      result?.let { result!!.text = (processor.getInput()) }

  }

  fun click_two(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('2')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_three(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('3')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_four(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('4')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_five(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('5')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_six(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('6')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_seven(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('7')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_eight(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('8')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun click_nine(view: View) {

      if (processor.checkTermlength() > 14 ) {
          Toast.makeText(this, R.string.digitlimit, Toast.LENGTH_SHORT).show()
      } else { // Is current index a "0"? if so -> replace with new given input, else just add it to the String.

          processor.inputprocess('9')
      }
      result?.let { result!!.text = (processor.getInput()) }
  }

  fun multiply(view: View) {

      processor.multiply()
      result?.let { result!!.text = (processor.getInput()) }

  }

  fun plus(view: View) {

      processor.plus()
      result?.let { result!!.text = (processor.getInput()) }

  }

  fun minus(view: View) {
      processor.minus()
      result?.let { result!!.text = (processor.getInput()) }

      /*
      if (input.length <= 0) {
          input.append('-')
          return
      }

      val current = input[input.length - 1]
      if (operator) {
          input.deleteCharAt(input.length - 1)
          input.append('-')

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
          result?.let { result!!.text = (input.toString()) }

      } else { // number, . , ( , )
          val previous = input[input.length - 1]

          if (previous == '(') return
          if (previous == '.') input.append(0)

          input.append('-')
          result?.let { result!!.text = (input.toString()) }
          operator = true
          currentcomma = false
          discontinue = false
          //negative = false // TODO: what if 2*-(5-3) ???
      }
      return*/
  }

  fun divide(view: View) {
      processor.divide()
      result?.let { result!!.text = (processor.getInput()) }

  }

  fun Comma(view: View) {
      processor.comma()
      result?.let { result!!.text = (processor.getInput()) }

  }

  fun result(view: View) {

      // TODO: https://en.wikipedia.org/wiki/Shunting_yard_algorithm

      val currentinput = processor.getInput()

      if (currentinput.length > 0) {
          if(currentinput[currentinput.length - 1] == '.') processor.addInput("0") // if the last input is an empty "x." -expression -> fill with 0
      }

      processor.closeup()

      /*
      while (opened != closed) {
          processor.addInput(")")
          closed++
      }*/

      var done = false

      val infix: List<String> = infix(processor.getInput())

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
                  val givenoperands = Operandspresent(queue)

                  // initial declarations
                  var operand2 = BigDecimalOrInfinite("0")
                  operand2.setInfinite(false)
                  var operand1 = BigDecimalOrInfinite("0")
                  operand1.setInfinite(false)

                  when(givenoperands) {
                      0 -> { // when both operand are not given
                              operand2 = BigDecimalOrInfinite("0")
                              operand1 = BigDecimalOrInfinite("0")
                           }
                      1 -> { // only 1 operand given
                              val op1 = queue.pop()

                              if (isNumeric(op1)) {
                                  operand2 = BigDecimalOrInfinite("0")
                                  operand1 = BigDecimalOrInfinite(op1!!)
                              }

                              if (isInfinite(op1)) operand1.setInfinite(true)
                           }
                      2 -> { // both operands given
                              val op2 = queue.pop()
                              val op1 = queue.pop()
                              if (isNumeric(op2) && isNumeric(op1)) { // TODO: what if one is notNumeric? or Infinity
                                  operand2 = BigDecimalOrInfinite(op2!!)
                                  operand1 = BigDecimalOrInfinite(op1!!)
                              }

                              if (isInfinite(op1)) operand1.setInfinite(true)
                              if (isInfinite(op2)) operand2.setInfinite(true)
                          }
                  }

                when(current) {
                  "+" -> {
                             if (operand1.isInfinite() || operand2.isInfinite()) {
                                 queue.push("Infinity")
                             } else {
                                 val result = operand1.add(operand2)
                                 queue.push(result.toString())
                             }
                         }
                  "-" -> { // TODO: implement ∞ handling
                             if (operand1.isInfinite() && operand2.isInfinite()) queue.push("NaN") // ∞ - ∞

                             if (!operand1.isInfinite() && operand2.isInfinite()) // TODO: x - ∞

                             if (operand1.isInfinite() && !operand2.isInfinite()) queue.push("Infinity") // ∞ - x

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

                             if (operand1.isInfinite() && operand2.isInfinite()) queue.push("Infinity") // ∞ * ∞

                             if (!operand1.isInfinite() && operand2.isInfinite()) // TODO: (-)x * ∞

                             if (operand1.isInfinite() && !operand2.isInfinite()) queue.push("Infinity") // ∞ * x

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

                          if (operand1.isInfinite() && operand2.isInfinite()) queue.push("NaN") // ∞ ÷ ∞ = NaN

                          if (!operand1.isInfinite() && operand2.isInfinite())  queue.push("0") // x ÷ ∞ = 0

                          if (operand1.isInfinite() && !operand2.isInfinite()) queue.push("Infinity") // TODO: ∞ ÷ (-)x = ∞

                          if (!operand1.isInfinite() && !operand2.isInfinite()) { // TODO: x ÷ (-)y
                              if (operand2.compareTo(BigDecimal.ZERO)==0) { // division by 0
                                  // 0 ÷ 0
                                  if (operand1.compareTo(BigDecimal.ZERO)==0) queue.push("NaN")
                                  else queue.push("Infinity")
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
                          processor.reset()
                          input.setLength(0)

                          //reset()
                          done= true
                          queue.clear()
                      }
                  }
                }
              }
          } else done = true
          // input.append(current+" ");
      }

      if (queue.size > 0) {

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

      } else {
          if (result != null) result!!.text = ("Error")
      }

      //reset()
      input.setLength(0)
      processor.reset()
      input.append(result!!.text)
      processor.addInput(result!!.text.toString())

      return
  }

  private fun Operandspresent(stack: Stack<String?>):Int {
    if (stack.empty()) return 0

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

  private fun isInfinite(str: String?): Boolean {
      if (str != null) return str.contains("Infinity")
      else return false
  }

  private fun isNumeric(str: String?): Boolean {
      if (str != null) return str.matches(Regex("-?\\d+(\\.\\d+)?(E\\d+)*")) //match a number with optional '-' and decimal.
      else return false
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

              "+" -> if (operator.size <= 0) { //1st entry
                        operator.push(current)
                    } else {
                        var condition = true

                        if (operator.peek() != "(") condition = false
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
                        if (operator.size <= 0 && !condition) operator.push(current)
                    }

              "-" -> if (operator.size <= 0) { //1st entry
                         operator.push(current)
                     } else {
                         var condition = true

                         if (operator.peek() != "(") condition = false
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
                         if (operator.size <= 0 && !condition) operator.push(current)
                     }

              "*", "/" -> if (operator.size <= 0) {
                              operator.push(current)
                          } else {
                              var condition = true

                              if (operator.peek() != "(") condition = false
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
                              if (operator.size <= 0 && !condition) operator.push(current)
                          }
              "(" -> operator.push(current)
              ")" -> {
                  var done = true

                  if (operator.peek() != "(") done = false
                  else operator.pop()

                  // add all operator within ( ) expression into the postfix
                  while (operator.size > 0 && !done) { //
                      compare = operator.pop()
                      if (compare == "(") done = true
                      else postfix.add(compare)
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

      // TODO: how to process an already negative number?!?

      for (i in 0 until raw.length) {
          var current:String = raw[i].toString()

          when (current) {

              "+","-","*","/" -> {
                  if (number.length != 0) output.add(number.toString())

                  output.add(current)
                  // operator.add('+');
                  // tester = '+';
                  //  opexist = true;
                  number.setLength(0) //reset the operand variable
              }

              "(" -> {

                  //TODO: check for - sign
                  var thirdlast = false

                  if (i >= 2) {
                      thirdlast = (raw[i-2] == '+' || raw[i-2] == '-' || raw[i-2] == '*' || raw[i-2] == '/')
                  }

                  var secondlast = false

                  if (i >= 1) {
                      secondlast = (raw[i-1] == '-')
                  }

                  if(thirdlast && secondlast) {

                      output.removeAt(output.size-1) // e.g. 6*- -> 6* -1, so the last - is removed and added seperately to the following negative number
                      output.add("(")
                      output.add("-1")
                      output.add(")")
                      output.add("*")
                      output.add(current)
                  }
                  else {
                      if (number.length != 0) output.add(number.toString())

                      output.add(current)
                  }

                  number.setLength(0) //reset the operand variable
                  opened++
              }

              ")" -> {
                  ++closed
                  if (number.length != 0) output.add(number.toString())
                  output.add(current)
                  number.setLength(0) //reset the operand variable
              }

              else -> {
                  //TODO: check for - sign
                  var thirdlast = false

                  if (i >= 2) {
                      thirdlast = (raw[i-2] == '+' || raw[i-2] == '-' || raw[i-2] == '*' || raw[i-2] == '/')
                  }

                  var secondlast = false

                  if (i >= 1) {
                      secondlast = (raw[i-1] == '-')
                  }

                  if(thirdlast && secondlast) {
                      output.removeAt(output.size-1)
                      current = '-'+current
                  }

                  // negative number not adjacent to an operator (e.g. -x or (-y
                  if(!thirdlast && secondlast) {
                      output.removeAt(output.size-1)
                      current = '-'+current
                  }


                  number.append(current)
              }
          }

          /*
          if (current == '-') { TODO

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

      if (number.length != 0) output.add(number.toString()) // add the last number of the input

      if (output.size > 0) {
          val lastnotdigit = (output.last() == "+") ||
                  (output.last() == "-") ||
                  (output.last() == "*") ||
                  (output.last() == "/") ||
                  (output.last() == "(")

          if (lastnotdigit) output.removeAt(output.size-1)
      }

      return output
  }

}
