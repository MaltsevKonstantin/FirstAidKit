package ru.konstantin.maltsev.firstaidkit.interfaces

import ru.konstantin.maltsev.firstaidkit.adapters.NameHolder

interface OnSelectNameListener {
    fun onSelectNameResponse(name: NameHolder)
}