object MainFSM {
    var hhCmmCssAMPM = "[0-9]+:[0-9]+:?[0-9]+? [AaPpMm]{2}" //such as "3:22:16 PM"
    var hhCmmCss = "-?[0-9]+:-?[0-9]+:-?[0-9]+" //such as "15:22:16"
    var hhhCmm = "h-?[0-9]+:-?[0-9]+" //such as "h15:22"
    var hhh = "h-?[0-9]+" //such as "h15"
    var hhCmm = "-?[0-9]+:-?[0-9]+" //such as "15:22"
    var mmmCss = "m-?[0-9]+:-?[0-9]+" //such as "m25:16"
    var mmm = "m-?[0-9]+" //such as "m25"
    var mmCss = "-?[0-9]+:-?[0-9]+" //such as "22:26"
    var mm = "-?[0-9]+" //only to be used in Minute Mode(2)
    var sss = "s-?[0-9]+" //such as "s25"
    var ss = "-?[0-9]+?" //only to be used in Second Mode(1)

    sealed class State {
        object MainMenu : State()
        class SubmitInput(
            val message: (index: Int) -> String
        ) : State()
        object End : State()
    }

    open class Event {

        object EVENT_TIME_ADD_SS : OnGoToAddTime(
            ss,
            1,
            "Enter numbers in the following format: \"116\" (i.e. 16 sec) or \"-116\" to add or subtract 116 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds.",
            {
                Triple(0, 0, it.toInt())
            })

        object EVENT_TIME_ADD_MM : OnGoToAddTime(
            mm,
            2,
            "Enter numbers in the following format: \"222\" (i.e. 222 min) or \"-222\" to add or subtract 222 minutes from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds.",
            {
                Triple(0, it.toInt(), 0)
            })
        object EVENT_TIME_ADD_MM_C_SS : OnGoToAddTime(
            mmCss,
            3,
            "Enter numbers in the following format: \"222:16\" (i.e. 222 min 16 sec) or \"222:-16\", etc. to add 222 minutes and add/subtract 16 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds.",
            {
                val times = it.split(":")
                Triple(0, times[0].toInt(), times[1].toInt())
            })
        object EVENT_TIME_ADD_HH_C_MM : OnGoToAddTime(
            hhCmm,
            4,
            "Enter numbers in the following format: \"115:22\" (i.e. 115 hr 22 min) or \"115:-22\", etc. to add 115 hours and add/subtract 22 minutes from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds.",
            {
                val times = it.split(":")
                Triple(times[0].toInt(), times[1].toInt(), 0)
            })
        object EVENT_TIME_ADD_HH_C_MM_C_SS : OnGoToAddTime(
            hhCmmCss,
            5,
            "Enter numbers in the following format: \"115:22:116\" (i.e. 115 hr 22 min 116 sec) or \"-115:22:-116\", etc. to add/subtract 115 hours, add 22 minutes, and add/subtract 116 seconds from the total, respectively. The maximum computable, cumulative time is 596523 hours, 14 minutes, 7 seconds.",
            {
                val times = it.split(":")
                Triple(times[0].toInt(), times[1].toInt(), times[2].toInt())
            })


        object OnAddTime : Event()
        open class OnGoToAddTime(
            val regex: String,
            val mode: Int,
            val message: String,
            val getHrMinSecFromInput: (String) -> Triple<Int, Int, Int>
        ) : Event()

        object OnGoToExcel : Event()
        object OnReturnToMainMenu : Event()
        object OnDone : Event()
        object OnReset : Event()
        object OnUndo : Event()
        object OnPrintTotal : Event()
    }

    sealed class SideEffect {
        object PrintTotal : SideEffect()
        object Reset : SideEffect()
        object Undo : SideEffect()
        object TakeExcel : SideEffect()
    }

    init {
        var index = 1
        val stateMachine = StateMachine.create<State, Event, SideEffect> {
            initialState(State.MainMenu)
            state<State.MainMenu> {
//                on(Event.EVENT_TIME_ADD_SS) {
//                    println(Event.EVENT_TIME_ADD_SS.message)
//                    transitionTo(State.SubmitInput("Enter time"))
//                }
            }
            state<State.SubmitInput> {
//                on<Event.OnAddTime> {
//                    transitionTo(State.Solid, SideEffect.LogFrozen)
//                }
//                on<Event.OnVaporized> {
//                    transitionTo(State.Gas, SideEffect.LogVaporized)
//                }
            }
//            state<State.Gas> {
//                on<Event.OnCondensed> {
//                    transitionTo(State.Liquid, SideEffect.LogCondensed)
//                }
//            }
            onTransition {
                val validTransition = it as? StateMachine.Transition.Valid ?: return@onTransition
                if(it.toState is State.SubmitInput) {
                    index++
                    println(it.toState.message(index))
                    it.event
                }
                when (validTransition.sideEffect) {
//                    SideEffect.LogMelted -> logger.log(ON_MELTED_MESSAGE)
//                    SideEffect.LogFrozen -> logger.log(ON_FROZEN_MESSAGE)
//                    SideEffect.LogVaporized -> logger.log(ON_VAPORIZED_MESSAGE)
//                    SideEffect.LogCondensed -> logger.log(ON_CONDENSED_MESSAGE)
                }
            }
        }


        // When
        val transition = stateMachine.transition(Event.EVENT_TIME_ADD_SS)

// Then
//        assertThat(stateMachine.state).isEqualTo(Liquid)
//        assertThat(transition).isEqualTo(
//            StateMachine.Transition.Valid(Solid, OnMelted, Liquid, LogMelted)
//        )
//        then(logger).should().log(ON_MELTED_MESSAGE)
    }
}