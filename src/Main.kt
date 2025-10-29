data class Medicine(val name: String, var tablets: Int)

data class User(val name: String, val medicines: MutableList<Medicine>) {
    fun printMedicines() {
        println("$name ning dorilari:")
        medicines.forEach { medicine ->
            println(" - ${medicine.name}: ${medicine.tablets} ta tabletka")
        }
        println()
    }
}

class Apteka {
    val users = mutableListOf<User>()

    fun addUser(user: User) {
        users.add(user)
    }

    fun showUsers() {
        println("\n Barcha foydalanuvchilar va ularning dorilari ")
        users.forEach { it.printMedicines() }
    }

    fun swapMedicines(user1: User, user2: User, medicine1: Medicine, medicine2: Medicine) {
        val index1 = user1.medicines.indexOfFirst { it.name.equals(medicine1.name, ignoreCase = true) }
        val index2 = user2.medicines.indexOfFirst { it.name.equals(medicine2.name, ignoreCase = true) }

        if (index1 == -1 || index2 == -1) {
            println("\n Dori topilmadi!")
            return
        }

        val med1 = user1.medicines[index1]
        val med2 = user2.medicines[index2]

        if (med1.tablets <= 0 || med2.tablets <= 0) {
            println("\n Xatolik: Dorilardan biri 0 ta tabletkaga ega, almashish mumkin emas!")
            return
        }

        val exchangeAmount = minOf(med1.tablets, med2.tablets)

        if (exchangeAmount == 0) {
            println("\n Almashish uchun yetarli tabletka yo‘q!")
            return
        }

        med1.tablets -= exchangeAmount
        med2.tablets -= exchangeAmount


        val user1Existing = user1.medicines.find { it.name.equals(med2.name, ignoreCase = true) }
        if (user1Existing != null) {
            user1Existing.tablets += exchangeAmount
        } else {
            user1.medicines.add(Medicine(med2.name, exchangeAmount))
        }

        val user2Existing = user2.medicines.find { it.name.equals(med1.name, ignoreCase = true) }
        if (user2Existing != null) {
            user2Existing.tablets += exchangeAmount
        } else {
            user2.medicines.add(Medicine(med1.name, exchangeAmount))
        }

        println("\n ${user1.name} va ${user2.name} ${exchangeAmount} tadan dori almashishdi!")
        println("(${user1.name}: ${med1.name}<=>${user2.name}: ${med2.name})")

        if (med1.tablets == 0) user1.medicines.removeAt(index1)
        if (med2.tablets == 0) user2.medicines.removeAt(index2)
    }

    fun getUserMedicines(): List<Medicine> {
        val medicines = mutableListOf<Medicine>()
        println("Dorilarni kiriting (to‘xtatish uchun 'stop' deb yozing):")
        while (true) {
            println("Dori nomini kiriting :")
            val name = readLine()!!
            if (name.lowercase() == "stop") break

            println("Tabletkalar sonini kiriting:")
            val tablets = readLine()!!.toInt()


            if (tablets <= 0) {
                println(" Xatolik: Dori miqdori 0 yoki manfiy bo‘lishi mumkin emas!")
                continue
            }

            medicines.add(Medicine(name, tablets))
        }
        return medicines
    }
}

fun main() {
    val apteka = Apteka()

    apteka.addUser(User("Ali", mutableListOf(
        Medicine("Paratsetamol", 8),
        Medicine("Sitramon", 5),
        Medicine("Ibuprofen", 10),
        Medicine("Aspirin", 6),
        Medicine("Nurofen", 4),
        Medicine("Nimesil", 7)
    )))

    apteka.addUser(User("Madina", mutableListOf(
        Medicine("Mezim", 5),
        Medicine("No-shpa", 8),
        Medicine("Paratsetamol", 3),
        Medicine("Vitamin C", 12),
        Medicine("Suprastin", 6),
        Medicine("Introjirmina", 4)
    )))

    apteka.addUser(User("Doston", mutableListOf(
        Medicine("Aspirin", 5),
        Medicine("Ibuprofen", 3),
        Medicine("Mukaltin", 4),
        Medicine("Noshpa", 6),
        Medicine("Amoksiklav", 7)
    )))

    apteka.addUser(User("Aziza", mutableListOf(
        Medicine("Aspirin", 6),
        Medicine("Paratsetamol", 10),
        Medicine("TeraFlu", 4),
        Medicine("Mezim", 5),
        Medicine("Nurofen", 3),
        Medicine("Suprastin", 9)
    )))

    apteka.addUser(User("Kamron", mutableListOf(
        Medicine("Sitramon", 4),
        Medicine("No-shpa", 6),
        Medicine("Ibuprofen", 5),
        Medicine("Nurofen", 8),
        Medicine("Mukaltin", 3),
        Medicine("Amoksiklav", 4)
    )))

    apteka.addUser(User("Dilnoza", mutableListOf(
        Medicine("Paratsetamol", 6),
        Medicine("Vitamin C", 10),
        Medicine("Analgin", 5),
        Medicine("Sitramon", 7),
        Medicine("TeraFlu", 4),
        Medicine("Nurofen", 5)
    )))

    println("O'zingizning ismingizni kiriting:")
    val userName = readLine()!!
    val userMedicines = apteka.getUserMedicines()
    val currentUser = User(userName, userMedicines.toMutableList())
    apteka.addUser(currentUser)

    apteka.showUsers()

    while (true) {
        println("\nDori almashish jarayoni (to‘xtatish uchun 'stop' deb yozing):")
        println("Qaysi foydalanuvchi bilan almashmoqchisiz?")
        val partnerName = readLine()!!

        if (partnerName.lowercase() == "stop") {
            println("\n Dori almashish yakunlandi.")
            break
        }

        val partner = apteka.users.find { it.name.equals(partnerName, ignoreCase = true) }
        if (partner == null) {
            println(" Bunday foydalanuvchi topilmadi!")
            continue
        }

        println("\nSizning dorilaringiz:")
        currentUser.printMedicines()

        println("${partner.name}ning dorilari:")
        partner.printMedicines()

        println("Siz qaysi dori bilan almashmoqchisiz?")
        val med1Name = readLine()!!
        if (med1Name.lowercase() == "stop") break
        val med1 = currentUser.medicines.find { it.name.equals(med1Name, ignoreCase = true) }

        println("${partner.name} qaysi dorisini beradi?")
        val med2Name = readLine()!!
        if (med2Name.lowercase() == "stop") break
        val med2 = partner.medicines.find { it.name.equals(med2Name, ignoreCase = true) }

        if (med1 != null && med2 != null) {
            apteka.swapMedicines(currentUser, partner, med1, med2)
            println("\n Almashishdan keyingi holat:")
            apteka.showUsers()
        } else {
            println("Bunday dori topilmadi!")
        }
    }
}
