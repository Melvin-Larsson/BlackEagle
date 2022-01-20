import java.util.*

fun getCurrentDay() : Long{
    return millisToDays(Date().time)
}

fun millisToDays(millis : Long) : Long{
    return millis / (1000 * 3600 * 24)
}

