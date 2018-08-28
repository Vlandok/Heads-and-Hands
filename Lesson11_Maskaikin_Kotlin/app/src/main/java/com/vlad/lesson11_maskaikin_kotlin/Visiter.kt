package com.vlad.lesson11_maskaikin_kotlin

import java.util.*

data class Visiter (val date: Long, var timeInMinute: Int = Random().nextInt(MainActivity.MAX_TIME_VISITE)+1)