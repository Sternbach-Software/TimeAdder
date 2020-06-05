fun main() {
    test1@ for (i in 1..5) {
        println("test1 $i")
        if(i==3) break@test1
    }
    test2@ for(i in 1..5){
        println("test2 $i")
    }
    println("finished")
}