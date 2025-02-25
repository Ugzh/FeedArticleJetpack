package com.example.feedarticlejetpack.utils

import android.util.Log
import java.text.SimpleDateFormat

fun parsedDate(dateAsString: String): String{
    val parserDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    val formatterDate = SimpleDateFormat("dd/MM/yyyy")
    return formatterDate.format(parserDate.parse(dateAsString)!!);
}