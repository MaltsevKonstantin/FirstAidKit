package ru.konstantin.maltsev.firstaidkit.objects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class Group : RealmObject {
    @PrimaryKey
    var id : Long = 0
    var name : String = ""
}