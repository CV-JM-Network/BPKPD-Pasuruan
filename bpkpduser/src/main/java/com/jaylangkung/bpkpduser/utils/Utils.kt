package com.jaylangkung.bpkpduser.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object Utils {

    fun capitalizeFirstCharacter(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }
    }

    fun formatDate(date: String): String {
        val dateArray = date.split(" ")
        val dateArray2 = dateArray[0].split("-")
        return "${dateArray2[2]}-${dateArray2[1]}-${dateArray2[0]}"
    }

    fun loadFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        container: Int,
        transition: Transition = Transition.FADE,
        backstack: Boolean = false,
    ) {
        val transaction = fragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(container, fragment)
            .setTransition(transition.value)

        if (backstack) {
            val backStackName = fragment.javaClass.name
            transaction.addToBackStack(backStackName)
        }

        transaction.commit()
    }
}

enum class Transition(val value: Int) {
    FADE(FragmentTransaction.TRANSIT_FRAGMENT_FADE),
    OPEN(FragmentTransaction.TRANSIT_FRAGMENT_OPEN),
    CLOSE(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE),
}