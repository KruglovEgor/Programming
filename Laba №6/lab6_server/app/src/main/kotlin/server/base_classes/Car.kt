package base_classes

class Car(val name: String? = null) {
    override fun toString(): String
    {
        return name.toString()
    }
}