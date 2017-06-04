
package engine

//
class Input constructor(nm: String) : Statement {
    val varname = nm

    // INPUT հարամանի ինտերպրետացիան
    override fun execute(env: Environment)
    {
        // արտածել ներմուծման հրավերքը
        print("? ")
        // ստեղծել կարդացող օբյեկտը
        val scan = java.util.Scanner(System.`in`)
        // եթե փոփոխականը տեքստային է, ...
        if( varname.endsWith('$') ) {
            // ապա կարդալ մեկ տող
            val text = scan.nextLine()
            // և գրանցել կատարման միջավայրում
            env.put(varname, Value.Text(text))
        }
        else {
            // հակառակ դեպքում՝ կարդալ մեկ իրական թիվ
            val number = scan.nextDouble()
            // գրանցել կատարման միջավայրում
            env.put(varname, Value.Number(number))
        }
    }

    //
    override fun toString() : String =
        "INPUT $varname"
}