package com.pankaj6apr.currencyconversion.data.repository

object MockClock {
    private var delay: Long = 0;

    fun currentTimeMillis() : Long {
        return delay + System.currentTimeMillis()
    }

    fun mockDelay(d: Long) {
        delay = d
    }
}