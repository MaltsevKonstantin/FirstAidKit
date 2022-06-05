package ru.konstantin.maltsev.firstaidkit.realm

import io.realm.RealmConfiguration
import ru.konstantin.maltsev.firstaidkit.objects.Group
import ru.konstantin.maltsev.firstaidkit.objects.Manufacturer
import ru.konstantin.maltsev.firstaidkit.objects.Medicine
class Configuration {
    fun getDefault() : RealmConfiguration{
        return RealmConfiguration.Builder(schema = setOf(Medicine::class, Group::class, Manufacturer::class)).build()
    }
}