package com.example.calculator2

import java.math.BigDecimal


/**
 * This class serves as an extension of BigDecimal and checks for Infinity*/
class BigDecimalOrInfinite(input:String?):BigDecimal(input) {
    private var infinite = false
    private var negative = false

    fun setInfinite(value:Boolean) {
        infinite = value
    }

    fun isInfinite():Boolean {
        return infinite

        /*
        if(input!=null)
            return input.contains("Infinity")
        else
            return false */
    }

    fun setNegative(value:Boolean) {
        negative = value
    }

    fun isNegative():Boolean {
        return negative
    }
    


    override fun toByte(): Byte {
        TODO("Not yet implemented")
    }

    override fun toChar(): Char {

        TODO("Not yet implemented")
    }

    override fun toShort(): Short {
        TODO("Not yet implemented")
    }


}