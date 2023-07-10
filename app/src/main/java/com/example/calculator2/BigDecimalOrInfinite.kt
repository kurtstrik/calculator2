package com.example.calculator2

import java.math.BigDecimal

class BigDecimalOrInfinite(input:String?):BigDecimal(input) {
    private var infinite = false

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