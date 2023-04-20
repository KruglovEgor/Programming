package base_classes

class Coordinates (private val x: Double = 0.0, private val y: Float = 0.0F){
    override fun toString() : String{
        return "[$x; $y]"
    }

    /**
     * Get coordinates
     *
     * @return List of 'x' and 'y' fields
     */
    fun getCoordinates():List<Number>{
        return listOf(x, y)
    }
    constructor(list: List<Number?>?) :this((list?.get(0) as? Number)?.toDouble() ?: 0.0, (list?.get(1) as? Number ?: 0.0).toFloat())
}